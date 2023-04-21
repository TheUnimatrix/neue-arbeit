package de.sgirke.neuearbeit.service;

import java.util.Calendar;

/**
 * Service-Klasse zum Berechnen der Arbeitstage für einen bestimmten Zeitraum in Jahren oder mit Datumsangaben sowie
 * zum Erstellen einer XML-Datei mit den angeforderten Daten.
 * @author Sebastian Girke
 */
public interface WorkingDaysService {
	
	/**
	 * Methode zum Berechnen aller Arbeitstage aus einem gewählten Zeitraum.
	 * @param startYear Anfangsjahr des gewählten Zeitraums
	 * @param endYear Endjahr des gewählten Zeitraums
	 * @return String-Repräsentation einer XML-Datei mit allen relevanten Daten zu den Arbeitstagen innerhalb des gewählten Zeitraums
	 */
	String calculateWorkingDays(Integer startYear, Integer endYear);
	
	/**
	 * Methode zum Berechnen aller Arbeitstage aus einem spezifischen Zeitraum.
	 * @param startDate Anfangsdatum des gewählten Zeitraums
	 * @param endDate Enddatum des gewählten Zeitraums
	 * @return String-Repräsentation einer XML-Datei mit allen relevanten Daten zu den Arbeitstagen innerhalb des spezifischen Zeitraums
	 */
	String calculateWorkingDays(Calendar startDate, Calendar endDate);
	
}