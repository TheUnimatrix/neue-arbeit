package de.sgirke.neuearbeit.model;

import java.util.Calendar;

/**
 * Modell-Klasse für einen Feiertag.
 * @author Sebastian Girke
 */
public class Holiday {
	
	/**
	 * Name des Feiertags
	 */
	private String name;
	
	/**
	 * Datum des Feiertags als {@link Calendar}-Objekt
	 */
	private Calendar date;
	
	/**
	 * Konstruktor zum Erstellen einer {@link Holiday}-Instanz, der den Namen
	 * und das Datum des Feiertags entgegennimmt.
	 * @param name Name des Feiertags
	 * @param date Datum des Feiertags
	 */
	public Holiday(String name, Calendar date) {
		this.name = name;
		this.date = date;
	}
	
	/**
	 * Getter-Methode für Datum des Feiertags
	 * @return Datum des Feiertags
	 */
	public Calendar getDate() {
		return date;
	}
	
	/**
	 * Getter-Methode für Name des Feiertags
	 * @return Name des Feiertags
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter-Methode für Datum des Feiertags
	 * @param date Datum des Feiertags
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	/**
	 * Setter-Methode für Name des Feiertags
	 * @param name Name des Feiertags
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}