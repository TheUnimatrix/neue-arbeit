package de.sgirke.neuearbeit.service.impl;

import de.sgirke.neuearbeit.service.PdfService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;

/**
 * Service-Implementierung zum Generieren eines PDF-Dokuments, welches alle Arbeitstage eines bestimmten
 * Zeitraums enthält.
 * @author Sebastian Girke
 */
public class PdfServiceImpl implements PdfService {

	/** Logger-Objekt */
	private static final Log log = LogFactory.getLog(PdfServiceImpl.class);

	public ByteArrayOutputStream generateWorkingDaysPdfNonspecificSpaceOfTime(String xmlWorkingDays) throws FOPException, TransformerException {
		
		// Hole externen Context zum Holen des Pfades zur XSL-Datei
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		// Hole Pfad zur XSL-Datei
		String pathXslPdfFile = context.getInitParameter(CONTEXT_PARAM_XSL_YEARS_PDF_FILE);

		if (log.isDebugEnabled()) {
			log.debug("Pfad zur XSL-Datei geholt: '" + pathXslPdfFile + "'");
		}

		// Generiere PDF-Dokument als Stream und gebe diesen zurück
		return this.generatePdfStream(pathXslPdfFile, xmlWorkingDays, context);
	}
	
	public ByteArrayOutputStream generateWorkingDaysPdfSpecificSpaceOfTime(String xmlWorkingDays) throws FOPException, TransformerException {
		
		// Hole externen Context zum Holen des Pfades zur XSL-Datei
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		// Hole Pfad zur XSL-Datei
		String pathXslPdfFile = context.getInitParameter(CONTEXT_PARAM_XSL_DATES_PDF_FILE);

		if (log.isDebugEnabled()) {
			log.debug("Pfad zur XSL-Datei geholt: '" + pathXslPdfFile + "'");
		}
		
		// Generiere PDF-Dokument als Stream und gebe diesen zurück
		return this.generatePdfStream(pathXslPdfFile, xmlWorkingDays, context);
	}
	
	/**
	 * Methode zum Generieren eines PDF-Dokuments als Stream, welches aus einer XML und einer XSL entsteht.
	 * @param pathXslPdfFile Pfad zur jeweils benötigten XSL-Datei
	 * @param xmlFile XML als String-Repräsentation mit erforderlichen Daten
	 * @param context externer Context zum Laden der XSL-Datei
	 * @return generiertes PDF-Dokument als Stream
	 * @throws TransformerException eine Exception beim Generieren des Formulars
	 * @throws FOPException eine Exception beim Generieren des Formulars
	 */
	private ByteArrayOutputStream generatePdfStream(String pathXslPdfFile, String xmlFile, ExternalContext context)
			throws FOPException, TransformerException  {
		
		ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

		// Hole benötigte Instanzen
		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		TransformerFactory factory = TransformerFactory.newInstance();

		// Generiere FOP-Instanz
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, pdfStream);

		// Hole Quelldaten als Streams (XSL-Datei + XML als String)
		Source xslSrc = new StreamSource(context.getResourceAsStream(pathXslPdfFile));
		Source xmlSrc = new StreamSource(new StringReader(xmlFile));

		if (log.isDebugEnabled()) {
			log.debug("Benötigte Quell-Dateien (XSL,XML) geholt");
		}

		Transformer transformer = factory.newTransformer(xslSrc);

		Result res = new SAXResult(fop.getDefaultHandler());

		// Transformiere Daten in das gewünschte Format
		transformer.transform(xmlSrc, res);

		if (log.isInfoEnabled()) {
			log.info("PDF-Generierung erfolgreich");
		}

		return pdfStream;
	}
}