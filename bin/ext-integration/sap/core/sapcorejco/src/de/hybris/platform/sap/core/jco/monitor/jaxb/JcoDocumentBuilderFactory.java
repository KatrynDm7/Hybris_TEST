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
package de.hybris.platform.sap.core.jco.monitor.jaxb;

import javax.xml.parsers.DocumentBuilderFactory;

import de.hybris.platform.sap.core.common.DocumentBuilderFactoryUtil;


public class JcoDocumentBuilderFactory {
	
	private static volatile DocumentBuilderFactory instance = null;
	 
	private JcoDocumentBuilderFactory() { }
 
	public static DocumentBuilderFactory getInstance() {

		if (instance != null) {
			
			return instance;
			
		}else{
			
			synchronized(JcoDocumentBuilderFactory.class){
				
				if (instance != null) {
					
					return instance;
					
				}else{
					
					instance = DocumentBuilderFactory.newInstance();
					
					DocumentBuilderFactoryUtil.setSecurityFeatures(instance);
					
					return instance;
					
				}
			}
		}

	}
		 	
}


