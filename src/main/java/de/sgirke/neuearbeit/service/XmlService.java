package de.sgirke.neuearbeit.service;

/**
 * Service-Klasse zum Generieren von XML-Dateien als String-Repräsentation. Dafür werden Methoden zur Verfügung
 * gestellt, mit denen einzelne Knoten sowie entsprechende Inhalte an die Knoten angehängt werden können.
 * @author Sebastian Girke
 */
public interface XmlService {
	
	/**
	 * Methode zum Hinzufügen eines Anfangsknotens.
	 * @param tagName Name des Knotens
	 */
	void addStartTag(String tagName);
	
	/**
	 * Methode zum Hinzufügen eines Endknotens.
	 * @param tagName Name des Knotens
	 */
	void addEndTag(String tagName);
	
	/**
	 * Methode zum Hinzufügen des Anfangsknotens für den Wurzelknoten.
	 */
	void addRootStartTag();
	
	/**
	 * Methode zum Hinzufügen des Endknotens für den Wurzelknoten.
	 */
	void addRootEndTag();
	
	/**
	 * Methode zum Anhängen von Inhalt an einen bestimmten Knoten.
	 * @param tagName Name des Knotens
	 * @param content Inhalt für den entsprechenden Knoten
	 */
	void addContentToNode(String tagName, Object content);
	
	/**
	 * Methode zum Initialisieren der XML; dafür wird zu Beginn eine
	 * XML-Deklaration hinzugefügt.
	 */
	void initXml();
	
	/**
	 * Methode zum Auslesen der erstellten XML-Datei als String-Repräsentation.
	 * @return XML als String-Repräsentation
	 */
	String getXML();
	
}