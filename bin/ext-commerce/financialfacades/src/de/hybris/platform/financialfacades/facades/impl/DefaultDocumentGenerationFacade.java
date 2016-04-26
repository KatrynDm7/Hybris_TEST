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
package de.hybris.platform.financialfacades.facades.impl;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.financialfacades.facades.DocumentGenerationFacade;
import de.hybris.platform.financialfacades.services.document.generation.pdf.fop.DocumentGenerationService;
import de.hybris.platform.financialfacades.services.document.generation.pdf.fop.PolicyDocumentDataProcessService;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Document Generation Facade
 */
public class DefaultDocumentGenerationFacade implements DocumentGenerationFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultDocumentGenerationFacade.class);
	DocumentGenerationService documentGenerationService;
	PolicyDocumentDataProcessService policyDocumentDataProcessService;

	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String INLINE_PDF = "inline; filename=";

	public byte[] generate(final String documentType, final InsurancePolicyData policyData, final HttpServletResponse response)
			throws IOException
	{
		if (documentType.equals(DocumentGenerationFacade.PDF_DOCUMENT))
		{
			final byte[] pdf = getDocumentGenerationService().generatePdf(policyData);
			if (response != null)
			{
				writePdfToResponse(pdf, response);
			}
			return pdf;
		}
		else
		{
			throw new NotImplementedException();
		}
	}

	@Override
	public byte[] generate(final String documentType, final String itemRefId, final HttpServletResponse response)
			throws IOException
	{
		final InsurancePolicyData policyData = getPolicyDocumentDataProcessService().getPolicyDocumentData(itemRefId);
		return generate(documentType, policyData, response);
	}

	protected void writePdfToResponse(final byte[] pdf, final HttpServletResponse response) throws IOException
	{
		if (pdf != null)
		{
			final Date date = new Date();

			final String filename = date.getTime() + ".pdf";
			response.setHeader(CONTENT_DISPOSITION, INLINE_PDF + filename);
			response.setContentLength(pdf.length);
			response.getOutputStream().write(pdf);
			response.getOutputStream().flush();
		}
	}

	protected DocumentGenerationService getDocumentGenerationService()
	{
		return documentGenerationService;
	}

	@Required
	public void setDocumentGenerationService(final DocumentGenerationService documentGenerationService)
	{
		this.documentGenerationService = documentGenerationService;
	}

	protected PolicyDocumentDataProcessService getPolicyDocumentDataProcessService()
	{
		return policyDocumentDataProcessService;
	}

	@Required
	public void setPolicyDocumentDataProcessService(final PolicyDocumentDataProcessService policyDocumentDataProcessService)
	{
		this.policyDocumentDataProcessService = policyDocumentDataProcessService;
	}
}
