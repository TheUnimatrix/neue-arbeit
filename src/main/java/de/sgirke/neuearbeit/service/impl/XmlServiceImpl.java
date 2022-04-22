package de.sgirke.neuearbeit.service.impl;

import de.sgirke.neuearbeit.service.XmlService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service-Implementierung zum Generieren von XML-Dateien als String-Repräsentation. Dafür werden Methoden zur
 * Verfügung gestellt, mit denen einzelne Knoten sowie entsprechende Inhalte an die Knoten angehängt werden können.
 * @author Sebastian Girke
 */
public class XmlServiceImpl implements XmlService {
	
	/** Logger-Objekt */
	private static final Log LOG = LogFactory.getLog(XmlServiceImpl.class);
	
	/** StringBuffer-Objekt zum Speichern der XML-Daten als String-Repräsentation */
	private StringBuffer xmlBuffer;
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.XmlService#addStartTag(java.lang.String)
	 */
	public void addStartTag(String tagName) {
		xmlBuffer.append("<").append(tagName).append(">");
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.XmlService#addEndTag(java.lang.String)
	 */
	public void addEndTag(String tagName) {
		xmlBuffer.append("</").append(tagName).append(">");
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.XmlService#addRootStartTag()
	 */
	public void addRootStartTag() {
		this.addStartTag("root");
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.XmlService#addRootEndTag()
	 */
	public void addRootEndTag() {
		this.addEndTag("root");
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.XmlService#addContentToNode(java.lang.String, java.lang.Object)
	 */
	public void addContentToNode(String tagName, Object content) {
		if (content == null) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Inhalt für Knoten '" + tagName + "' ist NULL");
			}
		}
		
		this.addStartTag(tagName);
		xmlBuffer.append(content);
		this.addEndTag(tagName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.XmlService#initXml()
	 */
	public void initXml() {
		xmlBuffer = new StringBuffer();
		xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.sgirke.neuearbeit.service.XmlService#getXML()
	 */
	public String getXML() {
		return xmlBuffer == null ? null : xmlBuffer.toString();
	}
	
}