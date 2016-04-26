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
 */

package com.hybris.datahub.core.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Result of manipulating data items. Contains information about how many items were affected by the data change
 * operation.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultData implements Serializable
{
	private int numberProcessed;

	public ResultData()
	{
	}

	public int getNumberProcessed()
	{
		return numberProcessed;
	}

	public void setNumberProcessed(int numberProcessed)
	{
		this.numberProcessed = numberProcessed;
	}
}
