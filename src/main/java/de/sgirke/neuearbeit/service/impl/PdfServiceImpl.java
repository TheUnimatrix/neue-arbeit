package de.sgirke.neuearbeit.service.impl;

import de.sgirke.neuearbeit.service.PdfService;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Service-Implementierung zum Generieren eines PDF-Dokuments, welches alle Arbeitstage eines bestimmten
 * Zeitraums enthält.
 * @author Sebastian Girke
 */
@Service
public class PdfServiceImpl implements PdfService {

	/** Logger-Objekt */
	private static final Logger logger = LoggerFactory.getLogger(PdfServiceImpl.class);

	public ByteArrayOutputStream generateWorkingDaysPdfNonspecificSpaceOfTime(String xmlWorkingDays)
			throws FOPException, TransformerException, IOException {
		return this.generatePdfStream("static/xslt/workingDays_Years.pdf.xsl", xmlWorkingDays);
	}

	public ByteArrayOutputStream generateWorkingDaysPdfSpecificSpaceOfTime(String xmlWorkingDays)
			throws FOPException, TransformerException, IOException {
		return this.generatePdfStream("static/xslt/workingDays_Dates.pdf.xsl", xmlWorkingDays);
	}
	
	/**
	 * Methode zum Generieren eines PDF-Dokuments als Stream, welches aus einer XML-Datei und einer XSL entsteht.
	 * @param pathXslPdfFile Pfad zur jeweils benötigten XSL-Datei
	 * @param xmlFile XML als String-Repräsentation mit erforderlichen Daten
	 * @return generiertes PDF-Dokument als Stream
	 * @throws TransformerException eine Exception beim Generieren des Formulars
	 * @throws FOPException eine Exception beim Generieren des Formulars
	 * @throws IOException Exception, die beim Suchen der XSL-Datei auftreten kann
	 */
	private ByteArrayOutputStream generatePdfStream(String pathXslPdfFile, String xmlFile)
			throws TransformerException, FOPException, IOException {
		
		ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

		// Hole benötigte Instanzen
		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		TransformerFactory factory = TransformerFactory.newInstance();

		// Generiere FOP-Instanz
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, pdfStream);

		// Hole Quelldaten als Streams (XSL-Datei + XML als String)
		ClassPathResource cpr = new ClassPathResource(pathXslPdfFile);
		Source xslSrc = new StreamSource(cpr.getFile());
		Source xmlSrc = new StreamSource(new StringReader(xmlFile));
		logger.debug("Benötigte Quell-Dateien (XSL,XML) geholt");

		Transformer transformer = factory.newTransformer(xslSrc);
		Result res = new SAXResult(fop.getDefaultHandler());

		// Transformiere Daten in das gewünschte Format
		transformer.transform(xmlSrc, res);
		logger.info("PDF-Generierung erfolgreich");

		return pdfStream;
	}
}