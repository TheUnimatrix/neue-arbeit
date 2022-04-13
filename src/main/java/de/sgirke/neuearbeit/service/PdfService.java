package de.sgirke.neuearbeit.service;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;

/**
 * Service-Interface, welches Methoden zum Generieren von PDF-Dokumenten zur
 * Verfügung stellt. Diese PDF-Dokumente enthalten alle Arbeitstage für einen
 * bestimmten Zeitraum.
 * @author Sebastian Girke
 */
public interface PdfService {

	/**
	 * Name des Context-Parameters, welcher den relativen Pfad zur XSL-Datei
	 * zum Generieren des PDF-Dokuments für den unbestimmten Zeitraum enthält
	 */
	String CONTEXT_PARAM_XSL_YEARS_PDF_FILE =
			"de.sgirke.neuearbeit.XSL_YEARS_PDF_FILE";
	
	/**
	 * Name des Context-Parameters, welcher den relativen Pfad zur XSL-Datei
	 * zum Generieren des PDF-Dokuments für den spezifischen Zeitraum enthält
	 */
	String CONTEXT_PARAM_XSL_DATES_PDF_FILE =
			"de.sgirke.neuearbeit.XSL_DATES_PDF_FILE";
	
	/**
	 * Methode zum Generien eines PDF-Streams aus einer XML mit den Angaben zu
	 * Arbeitszeiten sowie einer XSL-Datei mit dem Layout des PDF-Dokuments.
	 * Diese Methode wird verwendet, wenn das PDF-Dokument für den unbestimmten
	 * Zeitraum in Jahren generiert werden soll.
	 * @param xmlWorkingDays String-Repräsentation einer XML mit allen Angaben
	 * 		zu den Arbeitszeiten zu dem gewählten Zeitraum in Jahren
	 * @return Stream mit allen relevanten Daten für ein PDF-Dokument
	 * @throws FOPException Exception, die beim Generieren des PDF-Dokuments
	 * 		auftreten kann
	 * @throws TransformerException Exception, die beim Generieren des
	 * 		PDF-Dokuments auftreten kann
	 */
	ByteArrayOutputStream generateWorkingDaysPdfNonspecificSpaceOfTime(String xmlWorkingDays)
			throws FOPException, TransformerException;
	
	/**
	 * Methode zum Generien eines PDF-Streams aus einer XML mit den Angaben zu
	 * Arbeitszeiten sowie einer XSL-Datei mit dem Layout des PDF-Dokuments.
	 * Diese Methode wird verwendet, wenn das PDF-Dokument für den spezifischen
	 * Zeitraum mit Datumsangaben generiert werden soll.
	 * @param xmlWorkingDays String-Repräsentation einer XML mit allen Angaben
	 * 		zu den Arbeitszeiten zu dem gewählten Zeitraum mit Datumsangaben
	 * @return Stream mit allen relevanten Daten für ein PDF-Dokument
	 * @throws FOPException Exception, die beim Generieren des PDF-Dokuments
	 * 		auftreten kann
	 * @throws TransformerException Exception, die beim Generieren des
	 * 		PDF-Dokuments auftreten kann
	 */
	ByteArrayOutputStream generateWorkingDaysPdfSpecificSpaceOfTime(String xmlWorkingDays)
			throws FOPException, TransformerException;
	
}