/*
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *  
 */
package de.hybris.platform.scalanature.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.scalanature.constants.ScalanatureConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class ScalanatureManager extends GeneratedScalanatureManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( ScalanatureManager.class.getName() );
	
	public static final ScalanatureManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ScalanatureManager) em.getExtension(ScalanatureConstants.EXTENSIONNAME);
	}
	
}
