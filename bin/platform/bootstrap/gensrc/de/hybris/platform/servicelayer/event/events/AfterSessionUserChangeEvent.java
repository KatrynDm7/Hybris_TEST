/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 18.04.2016 18:26:58
 * ----------------------------------------------------------------
 *
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
package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class AfterSessionUserChangeEvent  extends AbstractEvent {

	/** <i>Generated property</i> for <code>AfterSessionUserChangeEvent.previousUserUID</code> property defined at extension <code>core</code>. */
	private String previousUserUID;
	
	public AfterSessionUserChangeEvent()
	{
		super();
	}

	public AfterSessionUserChangeEvent(final Serializable source)
	{
		super(source);
	}
	
		
	public void setPreviousUserUID(final String previousUserUID)
	{
		this.previousUserUID = previousUserUID;
	}
	
			
	public String getPreviousUserUID() 
	{
		return previousUserUID;
	}
		
}