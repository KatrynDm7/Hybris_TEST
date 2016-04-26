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
 */
package de.hybris.platform.xyformsservices.utils;

import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.mock.web.MockHttpServletResponse;


/**
 * {@link HttpServletResponse} own implementation for getting output stream back from an http connection.
 */
public class YHttpServletResponse extends MockHttpServletResponse
{
	private final ServletOutputStream outputStream;

	public YHttpServletResponse(final OutputStream os)
	{
		super();
		this.outputStream = new DelegatingServletOutputStream(os);
	}

	@Override
	public ServletOutputStream getOutputStream()
	{
		return this.outputStream;
	}
}
