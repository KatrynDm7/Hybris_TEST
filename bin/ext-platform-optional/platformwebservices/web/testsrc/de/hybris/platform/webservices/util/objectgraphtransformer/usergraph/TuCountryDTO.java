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
package de.hybris.platform.webservices.util.objectgraphtransformer.usergraph;

import de.hybris.platform.webservices.util.objectgraphtransformer.GraphNode;


@GraphNode(target = TuCountryModel.class)
public class TuCountryDTO
{
	private String code = null;

	public TuCountryDTO()
	{
		//
	}

	public TuCountryDTO(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

}
