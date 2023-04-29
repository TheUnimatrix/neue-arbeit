package de.sgirke.neuearbeit.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;

/**
 * Service-Interface, welches Methoden zum Generieren von PDF-Dokumenten zur Verfügung stellt. Diese PDF-Dokumente
 * enthalten alle Arbeitstage für einen bestimmten Zeitraum.
 * @author Sebastian Girke
 */
public interface PdfService {

	/**
	 * Methode zum Generien eines PDF-Streams aus einer XML-Datei mit den Angaben zu Arbeitszeiten sowie einer XSL-Datei
	 * mit dem Layout des PDF-Dokuments. Diese Methode wird verwendet, wenn das PDF-Dokument für den unbestimmten
	 * Zeitraum in Jahren generiert werden soll.
	 * @param xmlWorkingDays String-Repräsentation einer XML-Datei mit allen Angaben zu den Arbeitszeiten zu dem
	 *                          gewählten Zeitraum in Jahren
	 * @return Stream mit allen relevanten Daten für ein PDF-Dokument
	 * @throws FOPException Exception, die beim Generieren des PDF-Dokuments auftreten kann
	 * @throws TransformerException Exception, die beim Generieren des PDF-Dokuments auftreten kann
	 * @throws IOException Exception, die beim Suchen der XSL-Datei auftreten kann
	 */
	ByteArrayOutputStream generateWorkingDaysPdfNonspecificSpaceOfTime(String xmlWorkingDays)
            throws FOPException, TransformerException, IOException;
	
	/**
	 * Methode zum Generien eines PDF-Streams aus einer XML-Datei mit den Angaben zu Arbeitszeiten sowie einer XSL-Datei
	 * mit dem Layout des PDF-Dokuments. Diese Methode wird verwendet, wenn das PDF-Dokument für den spezifischen
	 * Zeitraum mit Datumsangaben generiert werden soll.
	 * @param xmlWorkingDays String-Repräsentation einer XML-Datei mit allen Angaben zu den Arbeitszeiten zu dem
	 *                         gewählten Zeitraum mit Datumsangaben
	 * @return Stream mit allen relevanten Daten für ein PDF-Dokument
	 * @throws FOPException Exception, die beim Generieren des PDF-Dokuments auftreten kann
	 * @throws TransformerException Exception, die beim Generieren des PDF-Dokuments auftreten kann
	 * @throws IOException Exception, die beim Suchen der XSL-Datei auftreten kann
	 */
	ByteArrayOutputStream generateWorkingDaysPdfSpecificSpaceOfTime(String xmlWorkingDays)
			throws FOPException, TransformerException, IOException;
	
}