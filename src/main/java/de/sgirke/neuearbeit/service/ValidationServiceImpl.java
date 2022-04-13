package de.sgirke.neuearbeit.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.sgirke.neuearbeit.model.Holiday;

/**
 * Service-Implementierung zum Validieren benutzerspezifischer Eingaben mit
 * Methoden zum Überprüfen von Jahresangaben sowie von eingegebenen
 * Datumsangaben.
 * @author Sebastian Girke
 */
public class ValidationServiceImpl implements ValidationService {
	
	/** Logger-Objekt */
	private static final Log LOG =
			LogFactory.getLog(ValidationServiceImpl.class);
	
	/**
	 * Service-Objekt zum Holen aller gesetzlichen Feiertage in Thüringen in
	 * Listenform
	 */
	private HolidayService holidayService;

	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.ValidationService#validateYears(int, int, java.util.List)
	 */
	public void validateYears(int startYear, int endYear, List<String> errorList) {
		// Wurde Anfangsjahr ausgewählt?
		if (startYear == -1) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Kein Anfangsjahr ausgewählt");
			}
			errorList.add("Es wurde kein Anfangsjahr ausgewählt.");
		}
		
		// Wurde Endjahr ausgewählt?
		if (endYear == -1) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Kein Endjahr ausgewählt");
			}
			errorList.add("Es wurde kein Endjahr ausgewählt.");
		}
		
		// Liegt Anfangsjahr nach Endjahr?
		if (startYear != -1 && endYear != -1 && startYear > endYear) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Anfangsjahr liegt nach dem Endjahr");
			}
			errorList.add("Das ausgewählte Anfangsjahr liegt nach dem "
					+ "ausgewählten Endjahr.");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.ValidationService#validateDates(java.lang.String, java.lang.String, java.util.List)
	 */
	public void validateDates(String startDateString, String endDateString,
			List<String> errorList) {
		// Parse die eingegebenen Daten in das Format "TT.MM.JJJJ"
		Date startDate = parseDate(startDateString, "Anfangsdatum", errorList);
		Date endDate = parseDate(endDateString, "Enddatum", errorList);
		
		// Validiere das Anfangsdatum
		if (startDate != null) {
			validateDate(startDate, "Anfangsdatum", errorList);
		}
		
		// Validiere das Enddatum
		if (endDate != null) {
			validateDate(endDate, "Enddatum", errorList);
		}
		
		// Liegt Anfangsdatum nach Enddatum
		if (startDate != null && endDate != null && startDate.after(endDate)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Anfangsdatum liegt nach Enddatum");
			}
			errorList.add("Das eingegebene Anfangsdatum liegt nach dem "
					+ "eingegebenen Enddatum.");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.ValidationService#setHolidayService(de.sgirke.neuearbeit.service.HolidayService)
	 */
	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.ValidationService#getHolidayService()
	 */
	public HolidayService getHolidayService() {
		return holidayService;
	}
	
	/**
	 * Methode zum Überprüfen und Parsen eines Datums aus einem String in ein
	 * gültiges {@link Date}-Objekt. Dabei wird vorher überprüft, ob ein Datum
	 * eingegeben wurde und ggf. passende Fehlermeldungen erstellt.
	 * @param date zu überprüfendes Datum
	 * @param dateDesc Bezeichnung des Datums (Anfangs- oder Enddatum)
	 * @param errorList Liste mit Fehlermeldungen, die beim Überprüfen des
	 * 		Datums auftreten können
	 * @return das eingegebene Datum als entsprechendes {@link Date}-Objekt bzw.
	 * 		<code>null</code>, falls bei der Überprüfung ein Fehler aufgetreten
	 * 		ist
	 */
	private Date parseDate(String date, String dateDesc, List<String> errorList) {
		// Erlaube keine ungültigen Angaben (z.B. "30.02.2013")
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		sdf.setLenient(false);
		
		Date parsedDate;
		
		// Wurde ein Datum eingegeben?
		if (date.trim().equals("")) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Kein " + dateDesc + " eingegeben");
			}
			errorList.add("Es wurde kein " + dateDesc + " eingegeben.");
			parsedDate = null;
		} else {
			try {
				// Parse Datum in das Format "TT.MM.JJJJ"
				parsedDate = sdf.parse(date);
			} catch (ParseException pe) {
				if (LOG.isDebugEnabled()) {
					LOG.debug(pe.getMessage(), pe);
					LOG.debug("Ungültiges " + dateDesc + " eingegeben");
				}
				errorList.add("Es wurde kein gültiges " + dateDesc + " im "
						+ "Format \"TT.MM.JJJJ\" eingegeben.");
				parsedDate = null;
			}
		}
		return parsedDate;
	}
	
	/**
	 * Methode zum Validieren eines {@link Date}-Objekts. Dabei wird überprüft,
	 * ob das zu überprüfende Datum auf einem Wochenend- oder Feiertag liegt,
	 * wobei es sich bei dem Feiertag um einen gesetzlichen Feiertag in
	 * Thüringen handelt.
	 * @param date das zu überprüfende Datum
	 * @param dateDesc Bezeichnung des Datums (Anfangs- oder Enddatum)
	 * @param errorList Liste mit Fehlermeldungen, die beim Überprüfen des
	 * 		Datums auftreten können
	 */
	private void validateDate(Date date, String dateDesc, List<String> errorList) {
		// Wandle Datum in Calendar-Objekt um
		Calendar dateCalendar = new GregorianCalendar();
		dateCalendar.setTime(date);
		
		// Hole Wochentag des Datums
		int dayOfWeek = dateCalendar.get(Calendar.DAY_OF_WEEK);
		
		// Liegt Datum auf einem Wochenendtag?
		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
			String dayOfWeekName =
					dayOfWeek == Calendar.SATURDAY ? "Samstag" : "Sonntag";
			if (LOG.isDebugEnabled()) {
				LOG.debug(dateDesc + " liegt auf Wochenende ("
						+ dayOfWeekName + ")");
			}
			errorList.add("Das eingegebene " + dateDesc + " liegt auf einem "
					+ "Wochenendtag (" + dayOfWeekName + ").");
		} else {
			// Hole Liste mit allen Feiertagen aus dem Jahr des Datums
			int year = dateCalendar.get(Calendar.YEAR);
			List<Holiday> holidayList =
					holidayService.getAllHolidays(year, year);
			
			// Überprüfe, ob Datum auf einem Feiertag liegt
			for (Holiday holiday : holidayList) {
				if (dateCalendar.equals(holiday.getDate())) {
					if (LOG.isDebugEnabled()) {
						LOG.debug(dateDesc + " liegt auf Feiertag ("
								+ holiday.getName() + ")");
					}
					errorList.add("Das eingegebene " + dateDesc + " liegt auf "
							+ "einem Feiertag (" + holiday.getName() + ").");
					break;
				}
			}
		}
	}
	
}