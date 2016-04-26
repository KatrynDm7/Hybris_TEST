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
package de.hybris.platform.sap.core.odata.util;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;


/**
 *
 *
 *
 */
public class ODataClientService
{
	private static final String PROXY_HOST = "proxyhost";
	private static final String PROXY_PORT = "proxyport";
	private static final String SEPARATOR = "/";

	private static final String HTTP_METHOD_PUT = "PUT";
	private static final String HTTP_METHOD_POST = "POST";
	private static final String HTTP_METHOD_GET = "GET";
	private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HTTP_HEADER_ACCEPT = "Accept";
	/**
	 * application/xml
	 */
	public static final String APPLICATION_XML = "application/xml";

	private int connectionTimeout;
	private int readTimeout;

	private static final Logger LOG = Logger.getLogger(ODataClientService.class.getName());

	private final Properties properties = new Properties();
	
	private HttpURLConnection initializeConnection(final String absoluteUri, final String contentType, final String httpMethod, final String user, final String password)
			throws IOException
	{
		HttpURLConnection connection = null;
		try
		{
			final URL url = new URL(absoluteUri);
			if (getProxyHost() != null)
			{
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getProxyHost(), getProxyPort()));
				connection = (HttpURLConnection) url.openConnection(proxy);
			}
			else
			{
				connection = (HttpURLConnection) url.openConnection();
			}
			// TODO: connection.setRequestProperty("Accept-Language", getLanguage() + ";q=0.5" );
			connection.setRequestProperty(HTTP_HEADER_ACCEPT, contentType);
			connection.setRequestMethod(httpMethod); //throws ProtocolException, subclass of IOException
			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
		}
		catch (final IOException IOe)
		{
			throw new IOException("Error initializing connection to backend system", IOe);
		}


		if (HTTP_METHOD_POST.equals(httpMethod) || HTTP_METHOD_PUT.equals(httpMethod))
		{
			connection.setDoOutput(true);
			connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
		}

		if (user != null)
		{
			String authorization = "Basic ";
			authorization += new String(Base64.encodeBase64((user + ":" + password).getBytes()));
			connection.setRequestProperty("Authorization", authorization);
		}
		return connection;
	}

	/**
	 * @return PROXY_HOST
	 */
	protected String getProxyHost()
	{
		return properties.getProperty(PROXY_HOST);
	}

	/**
	 * @return PROXY_PORT
	 */
	protected int getProxyPort()
	{
		final String value = properties.getProperty(PROXY_PORT);
		return Integer.parseInt(value);
	}

	private InputStream execute(final String relativeUri, final String contentType, final String httpMethod, final String user, final String password) throws IOException
	{
		InputStream content = null;
		final HttpURLConnection connection = initializeConnection(relativeUri, contentType, httpMethod, user, password);
		try
		{
			connection.connect();

			final HttpStatusCodes httpStatusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
			if (400 <= httpStatusCode.getStatusCode() && httpStatusCode.getStatusCode() <= 599)
			{
				final String msg = "Http Connection failed with status " + httpStatusCode.getStatusCode() + " "
						+ httpStatusCode.toString();
				throw new IOException(msg);
			}
			content = connection.getInputStream();
		}
		catch (final IOException e)
		{
			throw new IOException("Failed to connect to backend system", e);
		}
		return content;
	}
	
	
	public ODataEntry readEntry(final String serviceUri, final String contentType, final String entitySetName, final String select, final String filter, 
			final String expand, final String keyValue, final String user, final String password)
			throws ODataException, URISyntaxException, IOException
	{

		final String absoluteUri = this.createUri(serviceUri, entitySetName, keyValue, expand, select, filter, null); 
				
		InputStream content = null;
		ODataEntry oDE = null;
		try
		{
			final Edm edm = this.readEdm(serviceUri, user, password);
			final EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
			content = execute(absoluteUri, APPLICATION_XML, HTTP_METHOD_GET, user, password);
			oDE = EntityProvider.readEntry(contentType, entityContainer.getEntitySet(entitySetName), content,
					EntityProviderReadProperties.init().build());			
		}
		catch (final MalformedURLException e)
		{
			LOG.error("HTTP Destination is not configured correctly", e);
			throw new ODataException("HTTP Destination is not configured correctly", e);
		}
		catch (final SocketTimeoutException e)
		{
			LOG.error("Connection to the backend system has timed-out. System not reachable.", e);
			throw new ODataException("HTTP Destination is not configured correctly", e);
		}
		if (content != null){
			content.close();
		}
		return oDE;
	}


	/**
	 * @param serviceUri
	 * @param contentType
	 * @param entitySetName
	 * @param user 
	 * @param password 
	 * @return ODataFeed
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public ODataFeed readFeed(final String serviceUri, final String contentType, final String entitySetName, final String user, final String password)
			throws ODataException, URISyntaxException, IOException
	{
		return readFeed(serviceUri, contentType, entitySetName, null, null, null, null, user, password);
	}

	/**
	 * @param serviceUri
	 * @param contentType
	 * @param entitySetName
	 * @param select
	 * @param filter
	 * @param expand
	 * @param orderby
	 * @param user 
	 * @param password 
	 * @return OdataFeed
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public ODataFeed readFeed(final String serviceUri, final String contentType, final String entitySetName, final String expand,
			final String select, final String filter, final String orderby, final String user, final String password) throws ODataException, URISyntaxException, IOException
	{
		EdmEntityContainer entityContainer = null;
		InputStream content = null;
		ODataFeed oDF = null;
		try
		{
			final String absoluteUri = createUri(serviceUri, entitySetName, null, expand, select, filter, orderby);
			final Edm edm = this.readEdm(serviceUri, user, password);
			entityContainer = edm.getDefaultEntityContainer();
			content = execute(absoluteUri, contentType, HTTP_METHOD_GET, user, password);
			oDF = EntityProvider.readFeed(contentType, entityContainer.getEntitySet(entitySetName), content,
					EntityProviderReadProperties.init().build());

		}
		finally
		{
			if (content != null)
			{
				try
				{
					content.close();
				}
				catch (final IOException e)
				{
					LOG.error("", e);
				}
			}
		}
		return oDF;
	}


	private Edm readEdm(final String serviceUrl, final String user, final String password) throws ODataException, IOException
	{
		InputStream content = null;
		Edm edm = null;
		try
		{
			content = execute(serviceUrl + SEPARATOR + "$metadata", APPLICATION_XML, HTTP_METHOD_GET, user, password); //throw IOException
			edm = EntityProvider.readMetadata(content, false);//throw EntityProviderException
		}
		catch (final ODataException e)
		{
			if (content != null)
			{
				content.close();
			}
			throw new ODataException("HTTP Destination is not configured correctly", e);
		}
		if (content != null)
		{
			content.close();
		}		

		return edm;

	}

	private String createUri(final String serviceUri, final String entitySetName, final String id, final String expand,
			final String select, final String filter, final String orderby) throws URISyntaxException
	{
		UriBuilder uriBuilder = null;
		
		if (id == null)
		{
			uriBuilder = UriBuilder.serviceUri(serviceUri, entitySetName);
		}
		else
		{
			uriBuilder = UriBuilder.serviceUri(serviceUri, entitySetName, id);
		}
		uriBuilder = uriBuilder.addQuery("$expand", expand);
		uriBuilder = uriBuilder.addQuery("$select", select);
		uriBuilder = uriBuilder.addQuery("$filter", filter);
		uriBuilder = uriBuilder.addQuery("$orderby", orderby);

		final String absoluteURI = new URI(null, uriBuilder.build(), null).toASCIIString();

		return absoluteURI;
	}
	
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	
	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	
	/**
	 * UriBuilder Class
	 *
	 */
	private static class UriBuilder
	{
		private final StringBuilder uri;
		private final StringBuilder query;

		private UriBuilder(final String serviceUri, final String entitySetName)
		{
			uri = new StringBuilder(serviceUri).append(SEPARATOR).append(entitySetName);
			query = new StringBuilder();
		}

		public static UriBuilder serviceUri(final String serviceUri, final String entitySetName)
		{
			return new UriBuilder(serviceUri, entitySetName);
		}
		
		public static UriBuilder serviceUri(final String serviceUri, final String entitySetName, final String id)
		{
			final UriBuilder b = new UriBuilder(serviceUri, entitySetName);
			return b.id(id);
		}

		private UriBuilder id(final String id)
		{
			if (id == null)
			{
				throw new IllegalArgumentException("Null is not an allowed id");
			}
			uri.append("(").append(id).append(")");
			return this;
		}
		
		public UriBuilder addQuery(final String queryParameter, final String value)
		{
			if (value != null)
			{
				if (query.length() == 0)
				{
					query.append("/?");
				}
				else
				{
					query.append("&");
				}
				query.append(queryParameter).append("=").append(value);
			}
			return this;
		}

		public String build()
		{
			return uri.toString() + query.toString();
		}
	}
}
