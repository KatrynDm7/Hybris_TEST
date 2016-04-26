/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.core.common;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;


/**
 * The class contains utility methods for the DocumentBuilderFactory.
 *
 */
public class DocumentBuilderFactoryUtil
{

	/**
	 * Logger.
	 */
	private static final Logger lOG = Logger.getLogger(DocumentBuilderFactoryUtil.class.getName());

	/**
	 * An XML parser should be configured securely so that it does not allow external entities as part of an incoming XML
	 * document. To avoid XML External Entities attacks (XXE injections) the following properties should be set for an
	 * XML factory, parser or reader:
	 *
	 * Xerces 1 & Xerces 2: 
	 * - "http://xml.org/sax/features/external-general-entities" 
	 * - "http://xml.org/sax/features/external-parameter-entities"
	 *
	 * Xerces 2:
	 * - "http://apache.org/xml/features/disallow-doctype-decl"
	 *
	 * The method tries to set the feature for the given DocumentBuilderFactory instance. A log entry will be created if
	 * the feature could not be set.
	 *
	 * @param documentBuilderFactory
	 * The DocumentBuilderFactory instance for which the security features will be set.
	 *
	 */
	public static void setSecurityFeatures(final DocumentBuilderFactory documentBuilderFactory)
	{     
		// As suggested by the Fortify tool
		documentBuilderFactory.setNamespaceAware(true);
		setSecurityFeature(documentBuilderFactory,XMLConstants.FEATURE_SECURE_PROCESSING, true);
		
		// The settings below are suggested by the OWASP site https://owasp.org/index.php/XML_External_Entity_%28XXE%29_Processing
	    documentBuilderFactory.setXIncludeAware(false);
		documentBuilderFactory.setExpandEntityReferences(false);

		// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
		// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
		// A fatal error is thrown if the incoming document contains a DOCTYPE declaration. 
		setSecurityFeature(documentBuilderFactory,"http://apache.org/xml/features/disallow-doctype-decl", true);
		
    	// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
    	// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
		// 	Do not include external general entities. 
		setSecurityFeature(documentBuilderFactory,"http://xml.org/sax/features/external-general-entities", false);
		
		// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
    	// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
		// Do not include external parameter entities or the external DTD subset. 
		setSecurityFeature(documentBuilderFactory,"http://xml.org/sax/features/external-parameter-entities", false);		
	  
	}
	
	/**
	 * An XML parser should be configured securely so that it does not allow external entities as part of an incoming XML
	 * document. To avoid XML External Entities attacks (XXE injections) the following properties should be set for an
	 * XML factory, parser or reader:
	 *
	 * Xerces 1 & Xerces 2: 
	 * - "http://xml.org/sax/features/external-general-entities" 
	 * - "http://xml.org/sax/features/external-parameter-entities"
	 *
	 * Xerces 2:
	 * - "http://apache.org/xml/features/disallow-doctype-decl"
	 *
	 * The method tries to set the feature for the given DocumentBuilderFactory instance. A log entry will be created if
	 * the feature could not be set.
	 * 
	 * @param xmlReader
	 * The XMLReader instance for which the security features will be set.
	 */
	public static void setSecurityFeatures(final XMLReader xmlReader)
	{
		
		// As suggested by the Fortify tool
	    setSecurityFeature(xmlReader,XMLConstants.FEATURE_SECURE_PROCESSING, true);

		// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
		// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
		// A fatal error is thrown if the incoming document contains a DOCTYPE declaration.
		setSecurityFeature(xmlReader, "http://apache.org/xml/features/disallow-doctype-decl", true);

		// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
		// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
		// 	Do not include external general entities.
		setSecurityFeature(xmlReader, "http://xml.org/sax/features/external-general-entities", false);

		// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
		// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
		// Do not include external parameter entities or the external DTD subset.
		setSecurityFeature(xmlReader, "http://xml.org/sax/features/external-parameter-entities", false);

	}
	
	/**
	 * To set a security feature
	 * @param documentBuilderFactory
	 * @param feature
	 * @param value
	 */
	protected static void setSecurityFeature( final DocumentBuilderFactory documentBuilderFactory, final String feature, boolean enabled){
		
		 try{
			 documentBuilderFactory.setFeature(feature, enabled);		    	    		    				
		    }
		    catch(ParserConfigurationException e)
		    {		    
		    	lOG.error("The feature " + feature + " could not be set for the current DocumentBuilderFactory! "+ e.getMessage());
		    }
		
	}

	/**
	 * To set a security feature
	 * @param xmlReader
	 * @param feature
	 * @param enabled
	 */
	protected static void setSecurityFeature(final XMLReader xmlReader, final String feature, final boolean enabled)
	{
		try
		{
			xmlReader.setFeature(feature, enabled);
		}
		catch (SAXNotRecognizedException | SAXNotSupportedException e)
		{
			lOG.error("The feature " + feature + " could not be set for the current XMLReader! " + e.getMessage());
		}

	}


}
