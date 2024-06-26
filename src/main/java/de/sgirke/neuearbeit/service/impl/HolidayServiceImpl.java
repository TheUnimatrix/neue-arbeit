package de.sgirke.neuearbeit.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.sgirke.neuearbeit.service.HolidayService;

import de.sgirke.neuearbeit.model.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service-Implementierung zum Berechnen der gesetzlichen Feiertage in Thüringen und Erstellen einer Liste mit
 * {@link Holiday}-Objekten zur weiteren Verwendung innerhalb der Applikation
 * @author Sebastian Girke
 */
@Service
public class HolidayServiceImpl implements HolidayService {
	
	/** Logger-Objekt */
	private static final Logger logger = LoggerFactory.getLogger(HolidayServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.HolidayService#getAllHolidays(int, int)
	 */
	public List<Holiday> getAllHolidays(int startYear, int endYear) {
		List<Holiday> holidayList = new ArrayList<>();
		
		// Iteriere über ausgewählten Zeitraum
		for (int year = startYear; year <= endYear; year++) {
			
			// Füge feste Feiertage hinzu
			holidayList.add(new Holiday("Neujahrstag", new GregorianCalendar(year, Calendar.JANUARY, 1)));
			holidayList.add(new Holiday("Maifeiertag", new GregorianCalendar(year, Calendar.MAY, 1)));
			holidayList.add(new Holiday("Weltkindertag", new GregorianCalendar(year, Calendar.SEPTEMBER, 20)));
			holidayList.add(new Holiday("Tag der Deutschen Einheit", new GregorianCalendar(year, Calendar.OCTOBER, 3)));
			holidayList.add(new Holiday("Reformationstag", new GregorianCalendar(year, Calendar.OCTOBER, 31)));
			holidayList.add(new Holiday("Erster Weihnachtsfeiertag", new GregorianCalendar(year, Calendar.DECEMBER, 25)));
			holidayList.add(new Holiday("Zweiter Weihnachtsfeiertag", new GregorianCalendar(year, Calendar.DECEMBER, 26)));
			
			// Füge bewegliche Feiertage (abhängig von Ostersonntag) hinzu
			holidayList.add(new Holiday("Karfreitag", getGoodFriday(year)));
			holidayList.add(new Holiday("Ostermontag", getEasterMonday(year)));
			holidayList.add(new Holiday("Christi Himmelfahrt", getAscensionDay(year)));
			holidayList.add(new Holiday("Pfingstmontag", getWhitMonday(year)));
			
			/*
			 * Füge Heiligabend und Silvester separat hinzu (keine gesetzlichen Feiertage, aber zählen in der
			 * "Neuen Arbeit Neustadt (Orla) e.V." wie gesetzliche Feiertage
			 */
			holidayList.add(new Holiday("Heiligabend", new GregorianCalendar(year, Calendar.DECEMBER, 24)));
			holidayList.add(new Holiday("Silvester", new GregorianCalendar(year, Calendar.DECEMBER, 31)));
		}

		logger.debug("Liste mit allen Feiertagen in Thüringen erstellt (startYear='{}; endYear='{}; holidayList.size='{}')",
				startYear, endYear, holidayList.size());
		return holidayList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.HolidayService#getGoodFriday(int)
	 */
	public Calendar getGoodFriday(int year) {
		Calendar goodFriday = getEasterSunday(year);
		goodFriday.add(Calendar.DAY_OF_MONTH, -2);
		return goodFriday;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.HolidayService#getEasterMonday(int)
	 */
	public Calendar getEasterMonday(int year) {
		Calendar easterMonday = getEasterSunday(year);
		easterMonday.add(Calendar.DAY_OF_MONTH, 1);
		return easterMonday;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.HolidayService#getAscensionDay(int)
	 */
	public Calendar getAscensionDay(int year) {
		Calendar ascensionDay = getEasterSunday(year);
		ascensionDay.add(Calendar.DAY_OF_MONTH, 39);
		return ascensionDay;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.HolidayService#getWhitMonday(int)
	 */
	public Calendar getWhitMonday(int year) {
		Calendar whitMonday = getEasterSunday(year);
		whitMonday.add(Calendar.DAY_OF_MONTH, 50);
		return whitMonday;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.HolidayService#getEasterSunday(int)
	 */
	public Calendar getEasterSunday(int year) {
		Calendar easterSunday;
		
		// Berechnung des Ostersonntags nach der Gaußschen Osterformel
		int a = year % 19;
		int b = year % 4;
		int c = year % 7;
		int k = year / 100;
		
		int p = (8 * k + 13) / 25;
		int q = k / 4;
		
		int m = (15 + k - p - q) % 30;
		int n = (4 + k - q) % 7;
		
		int d = (19 * a + m) % 30;
		int e = (2 * b + 4 * c + 6 * d + n) % 7;
		
		int easterNumber = 22 + d + e;
		
		if (easterNumber < 32) {
			// Ostersonntag liegt im März
			easterSunday = new GregorianCalendar(year, Calendar.MARCH, easterNumber);
		} else {
			// Ostersonntag liegt im April
			easterSunday = new GregorianCalendar(year, Calendar.APRIL, easterNumber - 31);
		}
		
		return easterSunday;
	}
}