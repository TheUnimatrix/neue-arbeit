package de.sgirke.neuearbeit.service.impl;

import de.sgirke.neuearbeit.service.PdfService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Service-Implementierung zum Generieren eines PDF-Dokuments, welches alle Arbeitstage eines bestimmten
 * Zeitraums enthält.
 * @author Sebastian Girke
 */
@Service
public class PdfServiceImpl implements PdfService {

	/** Logger-Objekt */
	private static final Log log = LogFactory.getLog(PdfServiceImpl.class);

	public ByteArrayOutputStream generateWorkingDaysPdfNonspecificSpaceOfTime(String xmlWorkingDays) throws FOPException, TransformerException {
		
		// Hole externen Context zum Holen des Pfades zur XSL-Datei
//		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		// Hole Pfad zur XSL-Datei
//		String pathXslPdfFile = context.getInitParameter(CONTEXT_PARAM_XSL_YEARS_PDF_FILE);

//		if (log.isDebugEnabled()) {
//			log.debug("Pfad zur XSL-Datei geholt: '" + pathXslPdfFile + "'");
//		}

		// Generiere PDF-Dokument als Stream und gebe diesen zurück
//		return this.generatePdfStream(pathXslPdfFile, xmlWorkingDays, context);
		return this.generatePdfStream("pathXslPdfFile", xmlWorkingDays);
	}


	public ByteArrayOutputStream generateWorkingDaysPdfSpecificSpaceOfTime(String xmlWorkingDays) throws FOPException, TransformerException {
		/*
		// Hole externen Context zum Holen des Pfades zur XSL-Datei
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		// Hole Pfad zur XSL-Datei
		String pathXslPdfFile = context.getInitParameter(CONTEXT_PARAM_XSL_DATES_PDF_FILE);

		if (log.isDebugEnabled()) {
			log.debug("Pfad zur XSL-Datei geholt: '" + pathXslPdfFile + "'");
		}
		
		// Generiere PDF-Dokument als Stream und gebe diesen zurück
//		return this.generatePdfStream(pathXslPdfFile, xmlWorkingDays, context);
		return this.generatePdfStream(pathXslPdfFile, xmlWorkingDays);
		 */
		return null;
	}
	
	/**
	 * Methode zum Generieren eines PDF-Dokuments als Stream, welches aus einer XML und einer XSL entsteht.
	 * @param pathXslPdfFile Pfad zur jeweils benötigten XSL-Datei
	 * @param xmlFile XML als String-Repräsentation mit erforderlichen Daten
//	 * @param context externer Context zum Laden der XSL-Datei
	 * @return generiertes PDF-Dokument als Stream
	 * @throws TransformerException eine Exception beim Generieren des Formulars
	 * @throws FOPException eine Exception beim Generieren des Formulars
	 */
//	private ByteArrayOutputStream generatePdfStream(String pathXslPdfFile, String xmlFile, ExternalContext context)
//			throws FOPException, TransformerException  {
	private ByteArrayOutputStream generatePdfStream(String pathXslPdfFile, String xmlFile) throws TransformerException, FOPException {
		
		ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

		// Hole benötigte Instanzen
		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		TransformerFactory factory = TransformerFactory.newInstance();

		// Generiere FOP-Instanz
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, pdfStream);

		// Hole Quelldaten als Streams (XSL-Datei + XML als String)
//		Source xslSrc = new StreamSource(context.getResourceAsStream(pathXslPdfFile));
		Source xslSrc = new StreamSource(new File("src/main/resources/static/xslt/workingDays_Years.pdf.xsl"));
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