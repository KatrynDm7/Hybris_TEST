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
package de.hybris.platform.acceleratorcms.component.renderer.impl;

import de.hybris.platform.acceleratorcms.component.cache.CmsCacheService;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.regioncache.key.CacheKey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.PageContext;

import org.springframework.beans.factory.annotation.Required;


public class CachingCMSComponentRenderer extends GenericViewCMSComponentRenderer
{
	protected String charEncoding;
	protected static final String DEFAULT_ENCODING = "UTF-8";

	protected CmsCacheService cmsCacheService;

	protected CmsCacheService getCmsCacheService()
	{
		return cmsCacheService;
	}

	@Required
	public void setCmsCacheService(final CmsCacheService cmsCacheService)
	{
		this.cmsCacheService = cmsCacheService;
	}

	@Override
	protected void renderView(final PageContext pageContext, final AbstractCMSComponentModel component, final String includePath)
			throws ServletException, IOException
	{
		final HttpServletRequest httpRequest = (HttpServletRequest) pageContext.getRequest();
		final boolean useCache = getCmsCacheService().useCache(httpRequest, component);

		if (useCache)
		{
			final String content;
			final CacheKey key = getCmsCacheService().getKey(httpRequest, component);

			if (key == null)
			{
				super.renderView(pageContext, component, includePath);
				return;
			}

			if ((content = getCmsCacheService().get(key)) != null)
			{
				pageContext.getOut().write(content);
			}
			else
			{
				final RequestDispatcher rd = httpRequest.getRequestDispatcher(includePath);
				final IncludeResponseWrapper irw = new IncludeResponseWrapper(pageContext);
				rd.include(httpRequest, irw);
				final String includedContent = irw.getString();
				getCmsCacheService().put(key, includedContent);
				irw.getWriter().flush();
			}
		}
		else
		{
			super.renderView(pageContext, component, includePath);
		}
	}

	protected class PrintWriterWrapper extends PrintWriter
	{
		private final StringWriter out;
		private final Writer parentWriter;

		public PrintWriterWrapper(final StringWriter out, final Writer parentWriter)
		{
			super(out);
			this.out = out;
			this.parentWriter = parentWriter;
		}

		@Override
		public void flush()
		{
			try
			{
				this.parentWriter.write(this.out.toString());
				final StringBuffer sb = this.out.getBuffer();
				sb.delete(0, sb.length());
			}
			catch (final IOException ex)
			{
				// Do Nothing
			}
		}
	}

	protected class IncludeResponseWrapper extends HttpServletResponseWrapper
	{
		private final StringWriter sw = new StringWriter();

		private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

		private final ServletOutputStream sos = new ServletOutputStream()
		{
			@Override
			public void write(final int b) throws IOException
			{
				bos.write(b);
			}
		};
		private boolean isWriterUsed;
		private boolean isStreamUsed;
		private int status = 200;
		private final PageContext pageContext;

		public IncludeResponseWrapper(final PageContext paramPageContext)
		{
			super((HttpServletResponse) paramPageContext.getResponse());
			this.pageContext = paramPageContext;
		}

		@Override
		public PrintWriter getWriter() throws IOException
		{
			if (this.isStreamUsed)
			{
				throw new IllegalStateException("IMPORT_ILLEGAL_STREAM");
			}
			this.isWriterUsed = true;
			return new PrintWriterWrapper(this.sw, this.pageContext.getOut());
		}

		@Override
		public ServletOutputStream getOutputStream()
		{
			if (this.isWriterUsed)
			{
				throw new IllegalStateException("IMPORT_ILLEGAL_WRITER");
			}
			this.isStreamUsed = true;
			return this.sos;
		}

		@Override
		public void setContentType(final String x)
		{
			// Empty
		}

		@Override
		public void setLocale(final Locale x)
		{
			// Empty
		}

		@Override
		public void setStatus(final int status)
		{
			this.status = status;
		}

		@Override
		public int getStatus()
		{
			return this.status;
		}

		public String getString() throws UnsupportedEncodingException
		{
			if (this.isWriterUsed)
			{
				return this.sw.toString();
			}
			if (this.isStreamUsed)
			{
				if ((CachingCMSComponentRenderer.this.charEncoding != null)
						&& (!(CachingCMSComponentRenderer.this.charEncoding.equals(""))))
				{
					return bos.toString(CachingCMSComponentRenderer.this.charEncoding);
				}
				return bos.toString(DEFAULT_ENCODING);
			}
			return "";
		}
	}
}
