/*
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *  
 */
package de.hybris.platform.chinaaccelerator.social.jiathis.jalo;

import de.hybris.platform.chinaaccelerator.social.jiathis.constants.JiathisConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class JiathisManager extends GeneratedJiathisManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( JiathisManager.class.getName() );
	
	public static final JiathisManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (JiathisManager) em.getExtension(JiathisConstants.EXTENSIONNAME);
	}
	
}
