package de.sgirke.neuearbeit.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.sgirke.neuearbeit.model.Holiday;

/**
 * Service-Implementierung zum Berechnen der Arbeitstage für einen bestimmten Zeitraum in Jahren oder mit Datumsangaben
 * sowie zum Erstellen einer XML mit den angeforderten Daten.
 * @author Sebastian Girke
 */
public class WorkingDaysServiceImpl implements WorkingDaysService {
	
	/** Logger-Objekt */
	private static final Log LOG = LogFactory.getLog(WorkingDaysServiceImpl.class);
	
	/** Liste mit den Namen aller Monate */
	private static final List<String> monthList;
	
	/** Service-Objekt zum Holen aller gesetzlichen Feiertage in Thüringen in Listenform */
	private HolidayService holidayService;
	
	/** Service-Objekt zum Erstellen einer XML als String-Repräsentation */
	private XmlService xmlService;
	
	// Befüllen der statischen Liste mit allen Monatsnamen
	static {
		monthList = new ArrayList<>();
		monthList.add("Januar");
		monthList.add("Februar");
		monthList.add("März");
		monthList.add("April");
		monthList.add("Mai");
		monthList.add("Juni");
		monthList.add("Juli");
		monthList.add("August");
		monthList.add("September");
		monthList.add("Oktober");
		monthList.add("November");
		monthList.add("Dezember");
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.WorkingDaysService#setHolidayService(de.sgirke.neuearbeit.service.HolidayService)
	 */
	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.WorkingDaysService#getHolidayService()
	 */
	public HolidayService getHolidayService() {
		return holidayService;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.WorkingDaysService#setXmlService(de.sgirke.neuearbeit.service.XmlService)
	 */
	public void setXmlService(XmlService xmlService) {
		this.xmlService = xmlService;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.WorkingDaysService#getXmlService()
	 */
	public XmlService getXmlService() {
		return xmlService;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.WorkingDaysService#calculateWorkingDays(java.lang.Integer, java.lang.Integer)
	 */
	public String calculateWorkingDays(Integer startYear, Integer endYear) {
		// Hole Liste mit allen Feiertagen für das aktuelle Jahr
		List<Holiday> holidayList = holidayService.getAllHolidays(startYear, endYear);
		
		// Initialisiere Arbeitstage-XML
		xmlService.initXml();
		xmlService.addRootStartTag();
		
		// Laufe über den ausgewählten Zeitraum
		for (int currentYear = startYear; currentYear <= endYear; currentYear++) {
			// Füge Knoten für aktuelles Jahr hinzu
			xmlService.addStartTag("currentYear");
			xmlService.addContentToNode("year", currentYear);
			
			// Laufe über alle Monate des aktuellen Jahres
			for (int currentMonth = 0; currentMonth < 12; currentMonth++) {
				// Füge Knoten zu dem aktuellen Monat hinzu
				xmlService.addStartTag("month");
				xmlService.addContentToNode(
						"monthName", monthList.get(currentMonth));
				
				// Hilfsvariable für Anzahl der Arbeitstage des aktuellen Monats
				int localCountDays = 0;

				// Festlegen von Anfangs- und Enddatum
				Calendar startDate = new GregorianCalendar(currentYear, currentMonth, 1);
				Calendar endDate = new GregorianCalendar(currentYear, currentMonth, startDate.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				// Iteriere über Zeitraum von aktuellem Monat
				while (!startDate.after(endDate)) {
					// Liegt aktuelles Datum auf dem Wochenende?
					if (!(startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {

						// Flag zum Überprüfen, ob es ein Feiertag ist
						boolean isHoliday = false;

						// Iteriere über alle Feiertage
						for (Holiday holiday : holidayList) {

							// Liegt aktueller Tag auf einem Feiertag?
							if (startDate.equals(holiday.getDate())) {
								isHoliday = true;
								break;
							}
						}

						// Nachträgliche Prüfung, ob kein Feiertag
						if (!isHoliday) {
							localCountDays++;
						}
					}
					
					// Addiere einen Tag
					startDate.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				// Füge errechneten Wert der Arbeitstage des Monats hinzu
				xmlService.addContentToNode("numberOfWorkingDays", localCountDays);
				xmlService.addEndTag("month");
			}
			
			// Beende das laufende Jahr
			xmlService.addEndTag("currentYear");
		}
		xmlService.addRootEndTag();
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("XML für unbestimmten Zeitraum:\n" + xmlService.getXML());
		}
		
		return xmlService.getXML();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.WorkingDaysService#calculateWorkingDays(java.util.Calendar, java.util.Calendar)
	 */
	public String calculateWorkingDays(Calendar startDate, Calendar endDate) {
		// Hole Feiertage für kompletten Zeitraum
		List<Holiday> holidayList = holidayService.getAllHolidays(startDate.get(Calendar.YEAR), endDate.get(Calendar.YEAR));
		
		Calendar currentDate = (Calendar)startDate.clone();
		
		int oldMonth = startDate.get(Calendar.MONTH);
		int localCountWorkingDays = 0;
		
		// Initialisiere XML
		xmlService.initXml();
		xmlService.addRootStartTag();
		
		// Solange Iteration über den Zeitraum nicht komplett...
		while (!currentDate.after(endDate)) {
			int currentMonth = currentDate.get(Calendar.MONTH);
			
			// Fängt ein neuer Monat an?
			if (oldMonth != currentMonth) {
				oldMonth = currentMonth;
				
				Calendar endDateCurrentMonth = (Calendar)currentDate.clone();
				endDateCurrentMonth.add(Calendar.DAY_OF_MONTH, -1);
				
				Calendar startDateCurrentMonth = new GregorianCalendar(
						endDateCurrentMonth.get(Calendar.YEAR),
						endDateCurrentMonth.get(Calendar.MONTH),
						endDateCurrentMonth.getActualMinimum(Calendar.DAY_OF_MONTH));
				
				// Überprüfe, ob erster Monat des Zeitraums abgelaufen ist
				if (startDateCurrentMonth.before(startDate)) {
					startDateCurrentMonth = startDate;
				}
				
				// Erstelle Datumsangaben für den jeweiligen Monat mit
				// ausgeschriebenem Monatsnamen
				SimpleDateFormat sdf = new SimpleDateFormat("d. MMMM yyyy");
				String durationStart = sdf.format(startDateCurrentMonth.getTime());
				String durationEnd = sdf.format(endDateCurrentMonth.getTime());
				
				// Schreibe Angaben zum jeweiligen Monat in die XML
				xmlService.addStartTag("month");
				xmlService.addContentToNode("durationStart", durationStart);
				xmlService.addContentToNode("durationEnd", durationEnd);
				xmlService.addContentToNode("workingDays", localCountWorkingDays);
				xmlService.addEndTag("month");
				
				// Setze Zähler zurück
				localCountWorkingDays = 0;
			}
			
			// Ist aktuelles Datum am Wochenende?
			if (!(currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
					currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
				boolean isHoliday = false;

				// Iteriere über alle Feiertage und überprüfe, ob aktuelles
				// Datum ein Feiertag ist
				for (Holiday holiday : holidayList) {
					if (currentDate.equals(holiday.getDate())) {
						isHoliday = true;
						break;
					}
				}

				// Füge +1 zum Zähler hinzu, wenn es kein Feiertag ist
				if (!isHoliday) {
					localCountWorkingDays++;
				}
			}
			
			// Ist das Enddatum erreicht?
			if (currentDate.equals(endDate)) {
				Calendar startDateCurrentMonth = new GregorianCalendar(
						currentDate.get(Calendar.YEAR),
						currentDate.get(Calendar.MONTH),
						currentDate.getActualMinimum(Calendar.DAY_OF_MONTH));

				// Erstelle Datumsangaben für den letzten Monat mit
				// ausgeschriebenem Monatsnamen
				SimpleDateFormat sdf = new SimpleDateFormat("d. MMMM yyyy");
				String durationStart = sdf.format(startDateCurrentMonth.getTime());
				String durationEnd = sdf.format(currentDate.getTime());
				
				// Schreibe Angaben zum letzten Monat in die XML
				xmlService.addStartTag("month");
				xmlService.addContentToNode("durationStart", durationStart);
				xmlService.addContentToNode("durationEnd", durationEnd);
				xmlService.addContentToNode("workingDays", localCountWorkingDays);
				xmlService.addEndTag("month");
			}
			
			// Füge einen Tag hinzu und iteriere weiter
			currentDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		xmlService.addRootEndTag();
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("XML für spezifischen Zeitraum:\n" + xmlService.getXML());
		}
		
		return xmlService.getXML();
	}
	
}