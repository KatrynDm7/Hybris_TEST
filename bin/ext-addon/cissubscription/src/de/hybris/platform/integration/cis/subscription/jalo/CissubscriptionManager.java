/*
 *  
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
 */
package de.hybris.platform.integration.cis.subscription.jalo;

import de.hybris.platform.integration.cis.subscription.constants.CissubscriptionConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class CissubscriptionManager extends GeneratedCissubscriptionManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( CissubscriptionManager.class.getName() );
	
	public static final CissubscriptionManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CissubscriptionManager) em.getExtension(CissubscriptionConstants.EXTENSIONNAME);
	}
	
}
