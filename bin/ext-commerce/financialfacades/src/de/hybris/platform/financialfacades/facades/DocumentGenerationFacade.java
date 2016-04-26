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
package de.hybris.platform.financialfacades.facades;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;


/**
 * Document Generation Facade
 */
public interface DocumentGenerationFacade
{
	public static final String PDF_DOCUMENT = "PDF";

	/**
	 * Retrieve the byte[] with desired policy data and document type and write to the response if not null.
	 */
	byte[] generate(final String documentType, final InsurancePolicyData policyData, final HttpServletResponse response)
			throws IOException;

	/**
	 * Retrieve the byte[] with desired itemRefId and document type and write to the response if not null.
	 */
	byte[] generate(final String documentType, final String itemRefId, final HttpServletResponse response) throws IOException;
}
