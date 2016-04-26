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
package de.hybris.platform.xyformsfacades.strategy.preprocessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;


/**
 * Processor that applies XSLT Transformations to a formData
 */
public class XsltTransformerYFormPreprocessorStrategy extends TransformerYFormPreprocessorStrategy
{
	// To support XSLT 2.0 and XPATH 2.0 the following TransformerFactory is defined
	private static final String TRANSFORMER_FACTORY = "net.sf.saxon.TransformerFactoryImpl";

	private String xsltContent;

	@Override
	protected String transform(final String xmlContent, final Map<String, Object> params) throws YFormProcessorException
	{
		return transform(IOUtils.toInputStream(xsltContent), IOUtils.toInputStream(xmlContent));
	}

	protected String transform(final InputStream xsltStream, final InputStream xmlStream) throws YFormProcessorException
	{
		final Map<String, Object> transformationParameters = new HashMap<String, Object>();
		final String output = transform(xsltStream, xmlStream, transformationParameters);
		return output;
	}

	protected String transform(final InputStream xsltStream, final InputStream xmlStream,
			final Map<String, Object> transformParameters) throws YFormProcessorException
	{
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try
		{
			final TransformerFactory transformerFactory = TransformerFactory.newInstance(TRANSFORMER_FACTORY, null);
			final Transformer transformer = transformerFactory.newTransformer(new StreamSource(xsltStream));

			for (final String tpKey : transformParameters.keySet())
			{
				transformer.setParameter(tpKey, transformParameters.get(tpKey));
			}
			transformer.transform(new StreamSource(xmlStream), new StreamResult(os));
			return os.toString("UTF-8");
		}
		catch (final TransformerException | UnsupportedEncodingException e)
		{
			throw new YFormProcessorException(e);
		}
		finally
		{
			IOUtils.closeQuietly(os);
		}
	}

	public void setXsltStream(final InputStream xsltStream) throws IOException
	{
		this.xsltContent = IOUtils.toString(xsltStream, "UTF-8");
	}
}
