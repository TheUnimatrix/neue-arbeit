package de.sgirke.neuearbeit.utils;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.MimeConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Utility-Klasse zum Senden von PDF-Dateien an den Browser, sodass diese dem Benutzer zum Einsehen oder ggf. zum
 * Herunterladen zur Verfügung gestellt werden können.
 * @author Sebastian Girke
 */
public class JsfUtils {
	
	/** Logger-Objekt */
	private static final Log LOG = LogFactory.getLog(JsfUtils.class);
	
	/**
	 * Methode zum Versenden einer Datei an den Browser, sodass diese dem Benutzer zum Einsehen oder ggf. zum
	 * Herunterladen zur Verfügung gestellt werden kann.
	 * @param pdfOutStream PDF-Dokument als Datei-Stream
	 * @param fileName Name der Datei
	 */
	public static void sendFile(ByteArrayOutputStream pdfOutStream, String fileName) {
		// Hole benötigte Instanzen
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse)externalContext.getResponse();
		
		// Setze HTML-Header
		response.setContentType(MimeConstants.MIME_PDF);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.setContentLength(pdfOutStream.size());
		
		ServletOutputStream outStream = null;
		
		try {
			// Schreibe Datei per Stream an den Browser
			outStream = response.getOutputStream();
			outStream.write(pdfOutStream.toByteArray());
			outStream.flush();
		} catch (IOException ioe) {
			if (LOG.isErrorEnabled()) {
				LOG.error(ioe.getMessage(), ioe);
			}
		} finally {
			if (outStream != null) {
				try {
					// Schließe den Stream zum Browser, sofern dieser zuvor erfolgreich initialisiert wurde
					outStream.close();
				} catch (IOException ioe) {
					if (LOG.isErrorEnabled()) {
						LOG.error(ioe.getMessage(), ioe);
					}
				}
			}
		}
		
		facesContext.responseComplete();
	}
}