package de.sgirke.neuearbeit.beans;

import de.sgirke.neuearbeit.service.PdfService;
import de.sgirke.neuearbeit.service.ValidationService;
import de.sgirke.neuearbeit.service.WorkingDaysService;
import de.sgirke.neuearbeit.utils.JsfUtils;
import jakarta.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Bean-Klasse für das Modul "Arbeitstage" mit Methoden zum Auslesen der Daten
 * aus dem Formular sowie der anschließenden Überprüfung der Eingaben.
 * @author Sebastian Girke
 */
public class WorkingDaysBean {
	
	/** Logger-Objekt */
	private static final Log LOG = LogFactory.getLog(WorkingDaysBean.class);
	
	/** Konstante für die RadioButtons für den Fall, dass der unbestimmte Zeitraum in Jahren ausgewählt wurde */
	private static final String VALUE_SPACE_OF_TIME_YEARS =
			"spaceOfTime_Years";
	
	/** Konstante für die RadioButtons für den Fall, dass der spezifische Zeitraum mit Datumsangaben ausgewählt wurde */
	private static final String VALUE_SPACE_OF_TIME_DATES =
			"spaceOfTime_Dates";
	
	/**
	 * Navigationsregel, die angibt, dass wieder auf die Seite dieses Moduls weitergeleitet wird, wenn der Button
	 * "Generieren" geklickt wurde
	 */
	private static final String NAVIGATION_RULE_WORKING_DAYS = "workingDays";

	/**
	 * Liste mit sämtlichen Fehlermeldungen, die während der Validierung der Eingaben oder dem Generieren
	 * des PDF-Dokuments auftreten können
	 */
	private List<String> errorList;
	
	/**
	 * Objekt zum Speichern des ausgewählten Zeitraums (entweder unbestimmter Zeitraum oder spezifischer Zeitraum),
	 * standardmäßig ist "Unbestimmter Zeitraum in Jahren" ausgewählt
	 */
	private String spaceOfTimeSelection = VALUE_SPACE_OF_TIME_YEARS;
	
	/**
	 * Anfangsjahr für den unbestimmten Zeitraum
	 */
	private Integer startYear;
	
	/**
	 * Endjahr für den unbestimmten Zeitraum
	 */
	private Integer endYear;
	
	/**
	 * Anfangsdatum für den spezifischen Zeitraum 
	 */
	private String startDate;
	
	/**
	 * Enddatum für den spezifischen Zeitraum
	 */
	private String endDate;
	
	/**
	 * Service-Objekt zum Generieren von PDF-Dokumenten
	 */
	private PdfService pdfService;
	
	/**
	 * Service-Objekt zum Validieren von benutzerspezifischen Eingaben
	 */
	private ValidationService validationService;
	
	/**
	 * Service-Objekt zum Berechnen der Arbeitstage für einen gewählten Zeitraum
	 */
	private WorkingDaysService workingDaysService;
	
	/**
	 * Getter-Methode für Liste mit allen Jahren vom aktuellen Jahr sowie der nächsten 9 Jahre.
	 * @return Liste mit allen Jahren vom aktuellen Jahr sowie der nächsten 9 Jahre
	 */
	public List<SelectItem> getYearList() {
		// Liste mit allen Jahren - beginnend mit dem aktuellen Jahr sowie den nächsten 9 Jahren,
		// sodass ingesamt 10 Jahre zur Auswahl stehen
		List<SelectItem> yearList = new ArrayList<>();
		yearList.add(new SelectItem(-1, "---"));
		
		int currentYear = new GregorianCalendar().get(Calendar.YEAR);
		
		for (int i = currentYear; i < currentYear + 10; i++) {
			yearList.add(new SelectItem(i));
		}

		return yearList;
	}
	
	/**
	 * Getter-Methode für Liste mit den Auswahlmöglichkeiten, welcher Zeitraum bei den Radio-Buttons ausgewählt werden soll.
	 * @return Liste mit den Auswahlmöglichkeiten für die Radio-Buttons
	 */
	public List<SelectItem> getSpaceOfTimeList() {
		// Liste mit den Auswahlmöglichkeiten der Radio-Buttons für die mögliche Auswahl des gewünschten Zeitraums
		// (entweder in Jahren oder mit Datumsangaben)
		List<SelectItem> spaceOfTimeList = new ArrayList<>();
		spaceOfTimeList.add(new SelectItem(VALUE_SPACE_OF_TIME_YEARS, "Unbestimmter Zeitraum in Jahren"));
		spaceOfTimeList.add(new SelectItem(VALUE_SPACE_OF_TIME_DATES, "Spezifischer Zeitraum mit Datumsangaben"));
		return spaceOfTimeList;
	}
	
	/**
	 * Getter-Methode mit sämtlichen Fehlermeldungen, die während der Verarbeitung der benutzerspezifischen Eingaben
	 * auftreten können.
	 * @return Liste mit Fehlermeldungen
	 */
	public List<String> getErrorList() {
		return errorList;
	}
	
	/**
	 * Setter-Methode für das auswählbare Anfangsjahr, sofern der unbestimmte Zeitraum in Jahren ausgewählt wurde.
	 * @param startYear Anfangsjahr für unbestimmten Zeitraum
	 */
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}
	
	/**
	 * Getter-Methode für das auswählbare Anfangsjahr, sofern der unbestimmte Zeitraum in Jahren ausgewählt wurde.
	 * @return Anfangsjahr für unbestimmten Zeitraum
	 */
	public Integer getStartYear() {
		return startYear;
	}
	
	/**
	 * Setter-Methode für das auswählbare Endjahr, sofern der unbestimmte Zeitraum in Jahren ausgewählt wurde.
	 * @param endYear Endjahr für unbestimmten Zeitraum
	 */
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	
	/**
	 * Getter-Methode für das auswählbare Endjahr, sofern der unbestimmte Zeitraum in Jahren ausgewählt wurde.
	 * @return Endjahr für unbestimmten Zeitraum
	 */
	public Integer getEndYear() {
		return endYear;
	}
	
	/**
	 * Setter-Methode für das Anfangsdatum, sofern der spezifische Zeitraum ausgewählt wurde.
	 * @param startDate Anfangsdatum für spezifischen Zeitraum
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Getter-Methode für das Anfangsdatum, sofern der spezifische Zeitraum ausgewählt wurde.
	 * @return Anfangsdatum für spezifischen Zeitraum
	 */
	public String getStartDate() {
		return startDate;
	}
	
	/**
	 * Setter-Methode für das Enddatum, sofern der spezifische Zeitraum ausgewählt wurde.
	 * @param endDate Enddatum für spezifischen Zeitraum
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Getter-Methode für das Enddatum, sofern der spezifische Zeitraum ausgewählt wurde.
	 * @return Enddatum für spezifischen Zeitraum
	 */
	public String getEndDate() {
		return endDate;
	}
	
	/**
	 * Setter-Methode für die Auswahl des vom Benutzer ausgewählten Zeitraums (entweder unbestimmter Zeitraum
	 * oder spezifischer Zeitraum).
	 * @param spaceOfTimeSelection Objekt zum Speichern der Auswahl des vom Benutzer ausgewählten Zeitraums
	 */
	public void setSpaceOfTimeSelection(String spaceOfTimeSelection) {
		this.spaceOfTimeSelection = spaceOfTimeSelection;
	}
	
	/**
	 * Getter-Methode für die Auswahl des vom Benutzer ausgewählten Zeitraums (entweder unbestimmter Zeitraum
	 * oder spezifischer Zeitraum).
	 * @return Objekt zum Speichern der Auswahl des vom Benutzer ausgewählten Zeitraums
	 */
	public String getSpaceOfTimeSelection() {
		return spaceOfTimeSelection;
	}
	
	/**
	 * Setter-Methode für Service-Objekt zum Generieren von PDF-Dokumenten.
	 * @param pdfService Service-Objekt zum Generieren von PDF-Dokumenten
	 */
	public void setPdfService(PdfService pdfService) {
		this.pdfService = pdfService;
	}
	
	/**
	 * Getter-Methode für Service-Objekt zum Generieren von PDF-Dokumenten.
	 * @return Service-Objekt zum Generieren von PDF-Dokumenten
	 */
	public PdfService getPdfService() {
		return pdfService;
	}
	
	/**
	 * Setter-Methode für Service-Objekt zum Validieren der benutzerspezifischen Eingaben.
	 * @param validationService Service-Objekt zum Validieren der benutzerspezifischen Eingaben
	 */
	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}
	
	/**
	 * Getter-Methode für Service-Objekt zum Validieren der benutzerspezifischen Eingaben.
	 * @return Service-Objekt zum Validieren der benutzerspezifischen Eingaben
	 */
	public ValidationService getValidationService() {
		return validationService;
	}
	
	/**
	 * Setter-Methode für Service-Objekt zum Berechnen der Arbeitstage für einen gewählten Zeitraum.
	 * @param workingDaysService Service-Objekt zum Berechnen der Arbeitstage für einen gewählten Zeitraum
	 */
	public void setWorkingDaysService(WorkingDaysService workingDaysService) {
		this.workingDaysService = workingDaysService;
	}
	
	/**
	 * Getter-Methode für Service-Objekt zum Berechnen der Arbeitstage für einen gewählten Zeitraum.
	 * @return Service-Objekt zum Berechnen der Arbeitstage für einen gewählten Zeitraum
	 */
	public WorkingDaysService getWorkingDaysService() {
		return workingDaysService;
	}
	
	/**
	 * Methode zum Validieren der benutzerspezifischen Daten sowie Berechnen der Arbeitstage und anschließendem
	 * Absenden eines PDF-Dokuments mit den gewünschten Ergebnissen. Die Methode wird ausgeführt, wenn auf den Button
	 * "Generieren" geklickt wurde.
	 * @return Navigationsregel, die angibt, dass wieder auf die Seite dieses Moduls weitergeleitet wird, wenn
	 * 		der Button geklickt wurde
	 */
	public String validateInput() {
		errorList = new ArrayList<>();
		
		// Wurde "Unbestimmter Zeitraum in Jahren" ausgewählt?
		if (spaceOfTimeSelection.equals(VALUE_SPACE_OF_TIME_YEARS)) {
			this.sendPdfSpaceOfTimeYears();
		}
		// Wurde "Spezifischer Zeitraum mit Datumsangaben" ausgewählt?
		else if (spaceOfTimeSelection.equals(VALUE_SPACE_OF_TIME_DATES)) {
			this.sendPdfSpaceOfTimeDates();
		}
		
		return NAVIGATION_RULE_WORKING_DAYS;
	}
	
	/**
	 * Methode zum Validieren der benutzerspezifischen Daten sowie Berechnen der Arbeitstage und anschließendem
	 * Absenden eines PDF-Dokuments mit den gewünschten Ergebnissen, wenn der unbestimmte Zeitraum in Jahren vom
	 * Benutzer ausgewählt wurde.
	 */
	private void sendPdfSpaceOfTimeYears() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Wahl des Benutzers: Unbestimmter Zeitraum in Jahren");
		}
		
		// Validiere benutzerspezifische Eingaben
		validationService.validateYears(startYear, endYear, errorList);
		
		if (errorList.isEmpty()) {
			try {
				// Hole XML mit Arbeitstagen für gewünschten Zeitraum
				String workingDaysXml = workingDaysService.calculateWorkingDays(startYear, endYear);
				
				// Hole PDF-Dokument als Stream
				ByteArrayOutputStream workingDaysPdfStream =
						pdfService.generateWorkingDaysPdfNonspecificSpaceOfTime(workingDaysXml);
				
				// Erstelle Dateinamen und sende Datei an Browser
				String fileName = "Arbeitstage.pdf";
				JsfUtils.sendFile(workingDaysPdfStream, fileName);
			} catch (FOPException fope) {
				if (LOG.isErrorEnabled()) {
					LOG.error(fope.getMessage(), fope);
				}
				errorList.add("Beim Generieren des PDF-Dokuments ist ein "
						+ "kritischer Fehler aufgetreten.");
			} catch (TransformerException tce) {
				if (LOG.isErrorEnabled()) {
					LOG.error(tce.getMessage(), tce);
				}
				errorList.add("Beim Generieren des PDF-Dokuments ist ein "
						+ "kritischer Fehler aufgetreten.");
			}
		}
	}
	
	/**
	 * Methode zum Validieren der benutzerspezifischen Daten sowie Berechnen der Arbeitstage und anschließendem
	 * Absenden eines PDF-Dokuments mit den gewünschten Ergebnissen, wenn der spezifische Zeitraum mit Datumsangaben
	 * vom Benutzer ausgewählt wurde.
	 */
	private void sendPdfSpaceOfTimeDates() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Wahl des Benutzers: Spezifischer Zeitraum mit Datumsangaben");
		}
		
		// Validiere benutzerspezifische Eingaben
		validationService.validateDates(startDate, endDate, errorList);
		
		if (errorList.isEmpty()) {
			try {
				// Wandle Datumsangaben in Calendar-Objekte um
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				Date startingDate = sdf.parse(startDate);
				Date endingDate = sdf.parse(endDate);
				
				Calendar startDateCalendar = new GregorianCalendar();
				Calendar endDateCalendar = new GregorianCalendar();
				
				startDateCalendar.setTime(startingDate);
				endDateCalendar.setTime(endingDate);
				
				// Hole XML mit Arbeitstagen für gewünschten Zeitraum
				String workingDaysXml = workingDaysService.calculateWorkingDays(startDateCalendar, endDateCalendar);
				
				// Hole PDF-Dokument als Stream
				ByteArrayOutputStream workingDaysPdfStream =
						pdfService.generateWorkingDaysPdfSpecificSpaceOfTime(workingDaysXml);
				
				// Erstelle Dateinamen und sende Datei an Browser
				String fileName = "Arbeitstage.pdf";
				JsfUtils.sendFile(workingDaysPdfStream, fileName);
			} catch (ParseException pe) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(pe.getMessage(), pe);
				}
			} catch (FOPException fope) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(fope.getMessage(), fope);
				}
				errorList.add("Beim Generieren des PDF-Dokuments ist ein kritischer Fehler aufgetreten.");
			} catch (TransformerException tce) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(tce.getMessage(), tce);
				}
				errorList.add("Beim Generieren des PDF-Dokuments ist ein kritischer Fehler aufgetreten.");
			}
		}
	}
}