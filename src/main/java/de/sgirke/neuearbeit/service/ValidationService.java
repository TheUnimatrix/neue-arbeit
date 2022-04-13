package de.sgirke.neuearbeit.service;

import java.util.List;

/**
 * Service-Klasse zum Validieren benutzerspezifischer Eingaben aus einem
 * Formular.
 * @author Sebastian Girke
 */
public interface ValidationService {
	
	/**
	 * Methode zum Validieren von Jahresangaben aus einem bestimmten Zeitraum.
	 * @param startYear Anfangsjahr des gewählten Zeitraums
	 * @param endYear Endjahr des gewählten Zeitraums
	 * @param errorList Liste mit Fehlermeldungen, die bei der Validierung
	 * 		auftreten können
	 */
	public void validateYears(int startYear, int endYear,
			List<String> errorList);
	
	/**
	 * Methode zum Validieren von benutzerspezifischen Datumsangaben aus einem
	 * bestimmten Zeitraum.
	 * @param startDate Anfangsdatum des gewählten Zeitraums
	 * @param endDate Enddatum des gewählten Zeitraums
	 * @param errorList Liste mit Fehlermeldungen, die bei der Validierung
	 * 		auftreten können
	 */
	public void validateDates(String startDate, String endDate,
			List<String> errorList);
	
	/**
	 * Setter-Methode für Service-Objekt zum Holen aller gesetzlichen Feiertage
	 * in Thüringen für einen bestimmten Zeitraum in Listenform.
	 * @param holidayService Service-Objekt zum Holen aller gesetzlichen
	 * 		Feiertage in Thüringen in Listenform
	 */
	public void setHolidayService(HolidayService holidayService);
	
	/**
	 * Getter-Methode für Service-Objekt zum Holen aller gesetzlichen Feiertage
	 * in Thüringen für einen bestimmten Zeitraum in Listenform.
	 * @return Service-Objekt zum Holen aller gesetzlichen Feiertage in
	 * 		Thüringen in Listenform
	 */
	public HolidayService getHolidayService();
	
}