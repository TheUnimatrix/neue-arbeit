package de.sgirke.neuearbeit.service;

/**
 * Service-Klasse zum Generieren von XML-Dateien als String-Repräsentation.
 * Dafür werden Methoden zur Verfügung gestellt, mit denen einzelne Knoten
 * sowie entsprechende Inhalte an die Knoten angehängt werden können. 
 * @author Sebastian Girke
 */
public interface XmlService {
	
	/**
	 * Methode zum Hinzufügen eines Anfangsknotens.
	 * @param tagName Name des Knotens
	 */
	public void addStartTag(String tagName);
	
	/**
	 * Methode zum Hinzufügen eines Endknotens.
	 * @param tagName Name des Knotens
	 */
	public void addEndTag(String tagName);
	
	/**
	 * Methode zum Hinzufügen des Anfangsknotens für den Wurzelknoten.
	 */
	public void addRootStartTag();
	
	/**
	 * Methode zum Hinzufügen des Endknotens für den Wurzelknoten.
	 */
	public void addRootEndTag();
	
	/**
	 * Methode zum Anhängen von Inhalt an einen bestimmten Knoten.
	 * @param tagName Name des Knotens
	 * @param content Inhalt für den entsprechenden Knoten
	 */
	public void addContentToNode(String tagName, Object content);
	
	/**
	 * Methode zum Initialisieren der XML; dafür wird zu Beginn eine
	 * XML-Deklaration hinzugefügt.
	 */
	public void initXml();
	
	/**
	 * Methode zum Auslesen der erstellten XML-Datei als String-Repräsentation.
	 * @return XML als String-Repräsentation
	 */
	public String getXML();
	
}