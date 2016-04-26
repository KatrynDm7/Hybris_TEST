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
package de.hybris.platform.atddrunner.service;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.Lifecycle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Configurable embedded servlet container.
 */
public class IntegrationServletContainer implements Lifecycle
{
    private static final Logger LOG = LoggerFactory.getLogger(IntegrationServletContainer.class);
    protected static final String RUNNER_FILENAME = "atdd-runner-server.jar";

    private String connectionPath;
    private String warPath;
    private Properties properties;
    private String logFilePath = "../../log/atddrunner.log";
    private String heartbeat = "";

    private volatile Process serverProcess;

    /**
     * Start servlet container with {@code warPath} deployed.
     */
    @Override
    public void start()
    {
        // TODO make thread-safe
        LOG.trace("Request to start application {}", warPath);
        if (serverProcess == null)
        {
            try
            {
                createServer();
                LOG.info("Application {} has started", warPath);
            }
            catch (final Exception e)
            {
                LOG.error("Error starting Web Server", e);
                throw new RuntimeException("Error staring application " + warPath, e);
            }
        }
        else
        {
            LOG.warn("Application {} has already started", warPath);
        }
    }

    protected void createServer() throws Exception
    {
        final String pathToJava = getJavaPath();
        final String pathToWar = getWar();
        final URI connectionURI = URI.create(connectionPath);
        final String contextPath = connectionURI.getPath();
        final String port = Integer.toString(connectionURI.getPort());
        final String pathToRunner = extractRunner();
        final String props = buildProperties();

        LOG.info("Initializing Web Server\n\tport = {}\n\tpath = {}\n\tServlet = {}\n\tlogging to {}",
                port, contextPath, pathToWar, new File(logFilePath).getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder(
                pathToJava, props, "-jar", pathToRunner,
                "--port", port,
                "--path", contextPath,
                pathToWar);
        final File output = new File(logFilePath).getAbsoluteFile();
        final File outputDir = output.getParentFile();
        if (!outputDir.exists())
        {
            outputDir.mkdirs();
        }
        processBuilder.redirectOutput(new File(logFilePath));
        serverProcess = processBuilder.start();

        waitForServiceToStart();
    }

    private String extractRunner() throws IOException
    {
        final String destinationPath = System.getProperty("java.io.tmpdir")
                + System.getProperty("file.separator")
                + RUNNER_FILENAME;
        try (final InputStream source = getClass().getClassLoader().getResourceAsStream("bin/jetty-runner.jar");
             final FileOutputStream destination = new FileOutputStream(destinationPath))
        {
            IOUtils.copy(source, destination);
        }
        catch (final IOException x)
        {
            if (new File(destinationPath).exists())
            {
                LOG.debug("File {} already exists. Using the existing version.");
                return destinationPath;
            }
            else
            {
                throw x;
            }
        }
        LOG.debug("runner copied to {}", destinationPath);
        return destinationPath;
    }

    private void waitForServiceToStart() throws IOException, InterruptedException
    {
        final HttpClient client = new HttpClient();
        final HostConfiguration hostConfig = new HostConfiguration();
        final URI connectionURI = URI.create(connectionPath);
        hostConfig.setHost(connectionURI.getHost(), connectionURI.getPort(), "http");
        client.setHostConfiguration(hostConfig);
        final HttpClientParams params = new HttpClientParams();
        params.setSoTimeout(1000);
        client.setParams(params);
        final GetMethod method = new GetMethod(heartbeat);
        int countdown = 120;
        while (true)
        {
            try
            {
                Thread.sleep(1000);
                if (!serverProcess.isAlive())
                {
                    throw new IllegalStateException("Failed to start server. Look for details in "
                            + new File(logFilePath).getAbsolutePath());
                }
                client.executeMethod(hostConfig, method);
                break;
            }
            catch (final HttpException hx)
            {
                LOG.info("Application responded with ", hx);
                break;
            }
            catch (final IOException ex)
            {
                // It's OK: application is still starting
            }
            if (countdown-- < 0)
            {
                serverProcess.destroy();
                serverProcess = null;
                throw new IllegalStateException("Timeout exceeded while waiting for Web Server startup");
            }
        }
    }

    protected String buildProperties()
    {
        return properties.keySet().stream()
                .map(it -> "-D" + it + '=' + properties.getProperty(it.toString()))
                .reduce("", (it, acc) -> acc = acc + ' ' + it);
    }

    protected String getJavaPath()
    {
        final String separator = System.getProperty("file.separator");
        return System.getProperty("java.home") + separator + "bin" + separator + "java";
    }

    /**
     * Stop servlet container.
     */
    @Override
    public void stop()
    {
        LOG.trace("Request to stop application {}", warPath);
        if (serverProcess != null)
        {
            serverProcess.destroy();
            serverProcess = null;
            LOG.info("Application {} was stopped", warPath);
        }
        else
        {
            LOG.warn("Application {} has not been started yet", warPath);
        }
    }

    @Override
    public boolean isRunning()
    {
        return serverProcess != null;
    }

    /**
     * The method constructs absolute path of the servlet managed.
     * Default implementation takes {@code warPath} as a path, relative to
     * location of the {@code IntegrationServletContainer} class itself.
     *
     * @return absolute path to WAR
     * @throws FileNotFoundException if given war file does not exist
     */
    protected String getWar() throws Exception
    {
        final File fullPath = new File(warPath).getAbsoluteFile();
        final Path parentDir = fullPath.getParentFile().toPath();
        try (final DirectoryStream<Path> paths = Files.newDirectoryStream(parentDir, fullPath.getName()))
        {
            final List<Path> filesFound = StreamSupport.stream(paths.spliterator(), false).collect(Collectors.toList());
            if (filesFound.isEmpty())
            {
                throw new FileNotFoundException("File not found: " + fullPath.toString());
            }
            if (filesFound.size() > 1)
            {
                throw new FileNotFoundException("Ambiguous file path " + fullPath.toString() + ": found \n"
                        + StringUtils.join(filesFound, '\n'));
            }
            return filesFound.get(0).toFile().getCanonicalFile().getAbsolutePath();
        }
    }

    public String getConnectionPath()
    {
        return connectionPath;
    }

    public String getWarPath()
    {
        return warPath;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public String getLogFilePath()
    {
        return logFilePath;
    }

    public String getHeartbeat()
    {
        return heartbeat;
    }

    public Process getServerProcess()
    {
        return serverProcess;
    }

    /**
     * @param properties properties to pass to servlet
     */
    public void setProperties(final Properties properties)
    {
        this.properties = properties;
    }

    /**
     * File where to store server console output. Absolute or relative to platform path.
     * <p>Directory provided must exist. If the file exists, log info will be appended.
     * If there is no such file, it will be created. No log rotation.</p>
     * <p>Default value is {@code ../../log/atddrunner.log}</p>
     * @param path path to log file
     */
    public void setLogFilePath(final String path)
    {
        logFilePath = path;
    }

    /**
     * Path to resource, which can indicate the servlet is alive.
     *
     * @param path absolute path to heartbeat resource
     */
    public void setHeartbeat(final String path)
    {
        heartbeat = path;
    }

    /**
     * @param connectionPath servlet end point URI
     */
    @Required
    public void setConnectionPath(final String connectionPath)
    {
        this.connectionPath = connectionPath;
    }

    /**
     * Path to servlet file. Can be absolute or relative to directory that contains this class
     * @param warPath path to servlet file
     */
    @Required
    public void setWarPath(final String warPath)
    {
        this.warPath = warPath;
    }
}
