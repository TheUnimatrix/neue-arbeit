package de.sgirke.neuearbeit.service;

import java.util.Calendar;
import java.util.List;

import de.sgirke.neuearbeit.model.Holiday;

/**
 * Service-Klasse zum Berechnen der beweglichen Feiertage sowie zum Anfordern der gesetzlichen Feiertage in Listenform
 * für ein gewähltes Jahr
 * @author Sebastian Girke
 */
public interface HolidayService {
	
	/**
	 * Getter-Methode zum Holen aller gesetzlichen Feiertage in Thüringen aus einem benutzerspezifischen Zeitraum.
	 * @param startYear Anfangsjahr des gewählten Zeitraums
	 * @param endYear Endjahr des gewählten Zeitraums
	 * @return Liste mit allen gesetzlichen Feiertagen in Thüringen
	 */
	List<Holiday> getAllHolidays(int startYear, int endYear);
	
	/**
	 * Getter-Methode zum Berechnen von Karfreitag.
	 * @param year ausgewähltes Jahr zur Berechnung von Karfreitag
	 * @return {@link Calendar}-Objekt mit den Daten zu Karfreitag
	 */
	Calendar getGoodFriday(int year);
	
	/**
	 * Getter-Methode zum Berechnen von Ostermontag.
	 * @param year ausgewähltes Jahr zur Berechnung von Ostermontag
	 * @return {@link Calendar}-Objekt mit den Daten zu Ostermontag
	 */
	Calendar getEasterMonday(int year);
	
	/**
	 * Getter-Methode zum Berechnen von Christi Himmelfahrt.
	 * @param year ausgewähltes Jahr zur Berechnung von Christi Himmelfahrt
	 * @return {@link Calendar}-Objekt mit den Daten zu Christi Himmelfahrt
	 */
	Calendar getAscensionDay(int year);
	
	/**
	 * Getter-Methode zum Berechnen von Pfingstmontag.
	 * @param year ausgewähltes Jahr zur Berechnung von Pfingstmontag
	 * @return {@link Calendar}-Objekt mit den Daten zu Pfingstmontag
	 */
	Calendar getWhitMonday(int year);
	
	/**
	 * Getter-Methode zum Berechnen von Ostersonntag nach der Gaußschen Osterformel.
	 * @param year ausgewähltes Jahr zur Berechnung von Ostersonntag
	 * @return {@link Calendar}-Objekt mit den Daten zu Ostersonntag
	 */
	Calendar getEasterSunday(int year);
	
}