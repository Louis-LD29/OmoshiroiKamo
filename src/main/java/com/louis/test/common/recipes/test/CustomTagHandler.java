package com.louis.test.common.recipes.test;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface CustomTagHandler {

    boolean endElement(String uri, String localName, String qName) throws SAXException;

    boolean startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException;

    void configProcessed();
}
