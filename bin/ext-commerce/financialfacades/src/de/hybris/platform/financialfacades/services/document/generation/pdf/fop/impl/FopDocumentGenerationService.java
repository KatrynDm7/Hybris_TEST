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
package de.hybris.platform.financialfacades.services.document.generation.pdf.fop.impl;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.financialfacades.services.document.generation.pdf.fop.DocumentGenerationService;
import de.hybris.platform.financialfacades.services.document.generation.pdf.fop.PolicyDocumentContextFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.annotation.Resource;
import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;


/**
 * FOP Document Generation Service for policies by using FOP
 */
public class FopDocumentGenerationService implements DocumentGenerationService
{
	protected static final Logger LOG = Logger.getLogger(FopDocumentGenerationService.class);
	@Resource
	private RendererService rendererService;
	@Resource
	PolicyDocumentContextFactory policyDocumentContextFactory;

	@Override
	public byte[] generatePdf(final InsurancePolicyData policyData)
	{
		// Source for dynamic variables
		final StreamSource source = new StreamSource(new StringReader("<root></root>"));
		// Source for file template xsl-fo
		StreamSource transformSource;
		// FOP Factory instance
		final FopFactory fopFactory = FopFactory.newInstance();
		// FOP Agent for transformation
		final FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		final String templateId = policyData.getCategoryData().getCode();
		final String body = getPolicyBody(policyData, templateId);
		if (body == null)
		{
			throw new RuntimeException("No content found for template " + templateId);
		}
		transformSource = new StreamSource(new StringReader(body));
		try
		{
         	final Transformer xslTransfromer = getXSLTransformerWithoutSecureFeature(transformSource);
            final Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outputStream);
			final Result res = new SAXResult((fop.getDefaultHandler()));
            xslTransfromer.transform(source, res);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				outputStream.close();
			}
			catch (final IOException e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
		LOG.info("PDF generated successfully !");
		return outputStream.toByteArray();
	}

	/**
	 * Get the xml transformer by given stream source, also the factory set the secure processing feature to false.
	 * 
	 * @param source
	 *           the stream source
	 * @return xml transformer
	 * @throws TransformerConfigurationException
	 */
	protected Transformer getXSLTransformerWithoutSecureFeature(StreamSource source) throws TransformerConfigurationException
    {
		final TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		return factory.newTransformer(source);
	}

	protected String getPolicyBody(final InsurancePolicyData policyData, final String templateId)
	{
		final RendererTemplateModel bodyTemplate = getRendererService().getRendererTemplateForCode(templateId);

		final StringWriter renderedBody = new StringWriter();
		final VelocityContext context = getPolicyDocumentContextFactory().create(policyData, bodyTemplate);
		getRendererService().render(bodyTemplate, context, renderedBody);

		return renderedBody.getBuffer().toString();
	}

	protected RendererService getRendererService()
	{
		return rendererService;
	}

	public void setRendererService(final RendererService rendererService)
	{
		this.rendererService = rendererService;
	}

	protected PolicyDocumentContextFactory getPolicyDocumentContextFactory()
	{
		return policyDocumentContextFactory;
	}

	public void setPolicyDocumentContextFactory(final PolicyDocumentContextFactory policyDocumentContextFactory)
	{
		this.policyDocumentContextFactory = policyDocumentContextFactory;
	}
}
