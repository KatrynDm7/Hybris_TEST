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

package de.hybris.platform.emsclientatddtests.server;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Map;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Embedded Jetty server to spawn EMS for integration test purposes.
 */
public class EmsJetty
{
	private static final Logger LOG = Logger.getLogger(EmsJetty.class);
	private String connectionPath;
	private String warPath;
	private Server server;
	private Map<String,String> properties;

	/**
	 * Start EMS.
	 *
	 * @throws Exception
	 */
	public void start() throws Exception
	{
		if (server == null)
		{
			synchronized (this)
			{
				if (server == null)
				{
					server = new Server();
					final URI connectionURI = URI.create(connectionPath);

					final SocketConnector connector = new SocketConnector();
/*
					// Set some timeout options to make debugging easier.
					connector.setMaxIdleTime(1000 * 60 * 60);
					connector.setSoLingerTime(-1);
*/
					connector.setPort(connectionURI.getPort());
					server.setConnectors(new Connector[] {connector});

					final File file = new File(connectionURI.getPath());
					final String webApp = getWar();
					final String contextPath = file.getParent();
					LOG.info("Initializing Jetty for EMS\n\tPort = " + connectionURI.getPort() + "\n\tpath = " + contextPath +
							"\n\twar = " + webApp);
					final WebAppContext context = new WebAppContext(webApp, contextPath);

					context.setServer(server);
					server.setHandler(context);

					setSystemProperties();
				}
			}
		}
		server.start();
		LOG.info("EMS server started");
	}

	/**
	 * Stop EMS.
	 *
	 * @throws Exception
	 */
	public void stop() throws Exception
	{
		if (server != null)
		{
			server.stop();
			server.join();
			LOG.info("EMS server stopped");
		}
	}

	private String getWar()
	{
		final ProtectionDomain protectionDomain = EmsJetty.class.getProtectionDomain();
		final URL location = protectionDomain.getCodeSource().getLocation();
		return location.toExternalForm() + "/" + warPath;
	}

	private void setSystemProperties()
	{
		if (properties != null)
		{
			for (final Map.Entry<String, String> entry : properties.entrySet())
			{
				System.setProperty(entry.getKey(), entry.getValue());
			}
		}
	}

	@Required
	public void setConnectionPath(final String connectionPath)
	{
		this.connectionPath = connectionPath;
	}

	@Required
	public void setWarPath(final String warPath)
	{
		this.warPath = warPath;
	}

	public void setProperties(final Map<String, String> properties)
	{
		this.properties = properties;
	}
}
