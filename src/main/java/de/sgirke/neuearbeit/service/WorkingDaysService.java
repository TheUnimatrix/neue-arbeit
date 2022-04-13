package de.sgirke.neuearbeit.service;

import java.util.Calendar;

/**
 * Service-Klasse zum Berechnen der Arbeitstage für einen bestimmten Zeitraum
 * in Jahren oder mit Datumsangaben sowie zum Erstellen einer XML mit den
 * angeforderten Daten.
 * @author Sebastian Girke
 */
public interface WorkingDaysService {
	
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
	
	/**
	 * Setter-Methode für ein Service-Objekt zum Erstellen einer XML als
	 * String-Repräsentation.
	 * @param xmlService Service-Objekt zum Erstellen einer XML als
	 * 		String-Repräsentation
	 */
	public void setXmlService(XmlService xmlService);
	
	/**
	 * Getter-Methode für ein Service-Objekt zum Erstellen einer XML als
	 * String-Repräsentation.
	 * @return Service-Objekt zum Erstellen einer XML als String-Repräsentation
	 */
	public XmlService getXmlService();
	
	/**
	 * Methode zum Berechnen aller Arbeitstage aus einem gewählten Zeitraum.
	 * @param startYear Anfangsjahr des gewählten Zeitraums
	 * @param endYear Endjahr des gewählten Zeitraums
	 * @return String-Repräsentation einer XML mit allen relevanten Daten zu
	 * 		den Arbeitstagen innerhalb des gewählten Zeitraums
	 */
	public String calculateWorkingDays(Integer startYear, Integer endYear);
	
	/**
	 * Methode zum Berechnen aller Arbeitstage aus einem spezifischen Zeitraum.
	 * @param startDate Anfangsdatum des gewählten Zeitraums
	 * @param endDate Enddatum des gewählten Zeitraums
	 * @return String-Repräsentation einer XML mit allen relevanten Daten zu
	 * 		den Arbeitstagen innerhalb des spezifischen Zeitraums
	 */
	public String calculateWorkingDays(Calendar startDate, Calendar endDate);
	
}