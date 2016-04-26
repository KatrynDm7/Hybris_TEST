/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.processengine.actionstrategy;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;


public class ThrowErrorAction extends AbstractProceduralAction
{
	public int calls = 0;
	public BusinessProcessModel process;

	@Override
	public void executeAction(final BusinessProcessModel process) throws RetryLaterException, Exception
	{
		throw new Exception("error action throws exception");
	}
}
