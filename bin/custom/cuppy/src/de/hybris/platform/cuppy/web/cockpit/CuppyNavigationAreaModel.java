/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2010 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.cuppy.web.cockpit;

import de.hybris.platform.cockpit.components.navigationarea.DefaultNavigationAreaModel;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;



/**
 * Cuppy navigation area model.
 */
public class CuppyNavigationAreaModel extends DefaultNavigationAreaModel
{
	public CuppyNavigationAreaModel()
	{
		super();
	}

	public CuppyNavigationAreaModel(final AbstractUINavigationArea area)
	{
		super(area);
	}

	@Override
	public CuppyNavigationArea getNavigationArea()
	{
		return (CuppyNavigationArea) super.getNavigationArea();
	}
}
