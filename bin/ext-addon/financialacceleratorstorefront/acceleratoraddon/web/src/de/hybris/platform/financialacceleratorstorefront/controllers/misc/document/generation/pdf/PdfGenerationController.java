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
package de.hybris.platform.financialacceleratorstorefront.controllers.misc.document.generation.pdf;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.financialfacades.facades.DocumentGenerationFacade;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * PDFGenerationController
 */
@Controller("PdfGenerationController")
public class PdfGenerationController extends AbstractController
{
	protected static final Logger LOG = Logger.getLogger(PdfGenerationController.class);

	@Resource(name = "documentGenerationFacade")
	DocumentGenerationFacade documentGenerationFacade;

	/*
	 * http://financialservices.local:9001/yacceleratorstorefront/insurance/en/pdf/print?itemRefId=12345
	 */
	@RequestMapping(value = "/pdf/print", method = RequestMethod.GET)
	public void pdfPrint(@RequestParam("itemRefId") final String itemRefId, final HttpServletResponse response) throws IOException
	{
		LOG.info("Pdf Print with itemRefId - " + itemRefId);
		documentGenerationFacade.generate(DocumentGenerationFacade.PDF_DOCUMENT, itemRefId, response);
	}
}
