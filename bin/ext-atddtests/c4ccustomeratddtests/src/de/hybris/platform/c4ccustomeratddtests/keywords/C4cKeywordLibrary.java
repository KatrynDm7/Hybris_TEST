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
package de.hybris.platform.c4ccustomeratddtests.keywords;

import com.hybris.datahub.core.rest.DataHubCommunicationException;
import com.hybris.datahub.core.rest.DataHubOutboundException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.systemsetup.datacreator.impl.EncodingsDataCreator;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.embeddedserver.api.EmbeddedServer;
import de.hybris.platform.embeddedserver.api.EmbeddedServerBuilder;
import de.hybris.platform.embeddedserver.base.EmbeddedExtension;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.RootRequestFilter;
import de.hybris.platform.util.Utilities;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * ATDD keyword library.
 */
@Configurable
public class C4cKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = Logger.getLogger(C4cKeywordLibrary.class);

	public static final String DATAHUB_TEST_HOST = "localhost";
	public static final int DATAHUB_TEST_PORT = 8080;
	public static final String DATAHUB_TEST_ROOT = "/datahub/v1";
	public static final String TARGET_SYSTEM_PUBLICATIONS_TARGET_SYSTEM_NAME_C4_CSOAP_TARGET_SYSTEM =
			"{\"targetSystemPublications\":[{\"targetSystemName\":\"C4CSoapTargetSystem\"}]}";

	private static int embeddedServerPort = 9001;
	public static final String WS_VERSION;

	static
	{
		final String currentTenantId = Registry.getCurrentTenant().getTenantID();
		String wsVersion = "ws410";
		if (!MasterTenant.MASTERTENANT_ID.equalsIgnoreCase(currentTenantId))
		{
			wsVersion += "_" + currentTenantId;
		}
		WS_VERSION = wsVersion;
	}

	@Autowired
	private AddressService addressService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private UserService userService;

	@Autowired
	private CronJobService cronJobService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private EmbeddedServerBuilder tomcatEmbeddedServerBuilder;

	private static EmbeddedServer embeddedServer;

	protected Client jerseyClient;

	protected Cookie tenantCookie;

	protected WebResource webResourceCompositions;
	protected WebResource webResourcePublications;


	private final PropertyConfigSwitcher dataHubUrlProperty = new PropertyConfigSwitcher("y2ysync.datahub.url");

	@Value("${c4c.payload.filename}") private String fileName;
	@Value("${c4c.url.filename}")private String urlFileName;

	/**
	 * Send data to datahub.
	 *
	 * @param feed feed name
	 * @param itemType model typename
	 * @param csv data to send
	 * @return number of lines processed
	 */
	public Integer exportToDatahub(final String feed, final String itemType, final String... csv)
	{
		// TODO call POST /data-feeds/y2ysync
		return 200;
	}

	/**
	 * Sends compositions REST-method to datahub.
	 *
	 * @return
	 */
	public void sendCompositions() throws DataHubCommunicationException, InterruptedException
	{
		final ClientResponse result = webResourceCompositions.cookie(tenantCookie)
				.header("X-tenantId", "single")
				.header("Content-Type", "application/json")
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).method("POST", ClientResponse.class);
		result.bufferEntity();
	}

	/**
	 * Creates address for customer with uid.
	 *
	 * @param uid user's uid
	 * @throws DataHubOutboundException
	 * @throws DataHubCommunicationException
	 */
	public void createAddressForCustomer(final String uid) throws DataHubOutboundException, DataHubCommunicationException
	{
		final UserModel user = userService.getUserForUID(uid);
		final AddressModel address = addressService.createAddressForUser(user);
		address.setStreetname("streetname");
		address.setTown("town");
		user.setDefaultShipmentAddress(address);
		modelService.saveAll();
	}

	/**
	 * Get customer by its uid.
	 *
	 * @param uid
	 * @return
	 */
	public UserModel getCustomerByUid(final String uid)
	{
		return userService.getUserForUID(uid);
	}

	/**
	 * Sends publications REST-method to datahub.
	 *
	 * @return
	 */
	public void sendPublications()
	{
		final ClientResponse result;
		result = webResourcePublications.cookie(tenantCookie)
				.header("X-tenantId", "single")
				.header("Content-Type", "application/json")
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class,
						TARGET_SYSTEM_PUBLICATIONS_TARGET_SYSTEM_NAME_C4_CSOAP_TARGET_SYSTEM);
		result.bufferEntity();
	}

	/**
	 * creates customer.
	 *
	 * @param uid user uid
     * @param name user name
	 * @throws DataHubOutboundException
	 * @throws DataHubCommunicationException
	 */
	public void createCustomer(final String uid, final String name) throws DataHubOutboundException, DataHubCommunicationException
	{
		final CustomerModel customer = new CustomerModel();
		customer.setUid(uid);
		customer.setName(name);
		customer.setCustomerID(uid);
		modelService.save(customer);
	}

	/**
	 * @return Content of XML SOAP request to C4C.
	 * @throws DataHubCommunicationException
	 * @throws IOException
	 */
	public String getURL() throws DataHubCommunicationException, IOException
	{
		final File file = retrieveUrlFile();
		return readData(file);
	}

	/**
	 * @return Content of XML SOAP request to C4C.
	 * @throws DataHubCommunicationException
	 * @throws IOException
	 */
	public String readData() throws DataHubCommunicationException, IOException
	{
		final File file = retrievePayloadFile();
		return readData(file);
	}

	protected  String readData(final File fileToRead) throws DataHubCommunicationException, IOException
	{
		for (int i = 0; i < 120; i++)
		{

			if (fileToRead.exists())
			{
				return readFile(fileToRead, Charset.forName("UTF-8"));
			}
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		throw new DataHubCommunicationException("C4C data is not available");
	}

	protected String readFile(final File file, final Charset encoding)
			throws IOException
	{
		final byte[] encoded = Files.readAllBytes(file.toPath());
		return new String(encoded, encoding);
	}

	/**
	 * Remove payload.xml file.
	 *
	 * @throws DataHubCommunicationException
	 */
	public void removeData() throws DataHubCommunicationException
	{
		final File file = retrievePayloadFile();
		file.delete();
	}

	/**
	 * Prepare environment.
	 *
	 * @throws Exception
	 */
	public void prepareEnvironment() throws Exception
	{
		ensureEmbeddedServerIsRunning();
		final ClientConfig config = new DefaultClientConfig();

		jerseyClient = Client.create(config);
		jerseyClient.addFilter(new LoggingFilter());

		tenantCookie = new Cookie(RootRequestFilter.DEFAULT_TENANTID_COOKIE_NAME, "junit");

		webResourceCompositions = jerseyClient.resource(UriBuilder.fromUri("http://" + DATAHUB_TEST_HOST + "/")
				.port(DATAHUB_TEST_PORT).path(DATAHUB_TEST_ROOT)
				.path("/pools/GLOBAL/compositions").build());

		webResourcePublications = jerseyClient.resource(UriBuilder.fromUri("http://" + DATAHUB_TEST_HOST + "/")
				.port(DATAHUB_TEST_PORT).path(DATAHUB_TEST_ROOT)
				.path("/pools/GLOBAL/publications").build());

		new EncodingsDataCreator().populateDatabase();
		ServicelayerTest.createCoreData();
		final Map<String, String> map = Collections.emptyMap();
		de.hybris.deltadetection.jalo.DeltadetectionManager.getInstance().createEssentialData(map, null);
		de.hybris.y2ysync.jalo.Y2ysyncManager.getInstance().createEssentialData(map, null);
	}

	/**
	 * Extract value from XML by XPath.
	 *
	 * @param xml the xml
	 * @param xPath the xpath
	 * @return
	 */
	public String extractDataFromXmlByXPath(final String xml, final String xPath) throws ParserConfigurationException, IOException,
			SAXException, XPathExpressionException
	{
		String result = "";
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document doc = builder.parse(new InputSource(new StringReader(xml)));

		// Create XPathFactory object
		final XPathFactory xpathFactory = XPathFactory.newInstance();

		// Create XPath object
		final XPath xpath = xpathFactory.newXPath();

		final XPathExpression expr = xpath.compile(xPath);

		result = (String) expr.evaluate(doc, XPathConstants.STRING);
		return result;
	}

	/**
	 * Triggers CronJob.
	 *
	 * @throws DataHubCommunicationException
	 * @throws InterruptedException
	 */
	public void performCronJob() throws DataHubCommunicationException, InterruptedException
	{
		final CronJobModel cronJob = getCronJob("c4cSyncToDataHubCustomersCronJob");
		cronJobService.performCronJob(cronJob, false);
		final boolean cronJobFinished = pollForFinishedCronJob(cronJob);
	}

	private CronJobModel getCronJob(final String code)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Y2YSyncCronJob} WHERE {code}=?code");
		fQuery.addQueryParameter("code", code);
		return flexibleSearchService.searchUnique(fQuery);
	}

	private boolean pollForFinishedCronJob(final CronJobModel cronJob) throws InterruptedException
	{
		final DateTime start = new DateTime().plusSeconds(120);
		while (new DateTime().isBefore(start))
		{
			modelService.refresh(cronJob);
			if (cronJobService.isFinished(cronJob))
			{
				return true;
			}
			Thread.sleep(1000);
		}
		return false;
	}

	private void ensureEmbeddedServerIsRunning()
	{
		if (embeddedServer == null)
		{
			final EmbeddedServerBuilder builder = getEmbeddedServerBuilder();
			embeddedServer = builder.needEmbeddedServer().runningOnPort(embeddedServerPort)
					.withApplication(new EmbeddedExtension(Utilities.getExtensionInfo("mediaweb")).withContext("/medias"))
					.build();
			embeddedServer.start();
		}

	}

	private EmbeddedServerBuilder getEmbeddedServerBuilder()
	{
		return tomcatEmbeddedServerBuilder;
	}


	/**
	 * Generates td.
	 *
	 * @return Generated td
	 */
	public String generateId()
	{
		return UUID.randomUUID().toString();
	}

	protected File retrievePayloadFile()
	{
		final File tmpdir = new File(System.getProperty("java.io.tmpdir"));
		final File file = new File(tmpdir, fileName);
		LOG.info("Trying to get PayloadFile: " + file.getAbsolutePath());
		return file;
	}
	protected File retrieveUrlFile()
	{
		final File tmpdir = new File(System.getProperty("java.io.tmpdir"));
		final File file = new File(tmpdir, getUrlFileName());
		LOG.info("Trying to get PayloadFile: " + file.getAbsolutePath());
		return file;
	}
	public String getUrlFileName()
	{
		return urlFileName;
	}

	public void setUrlFileName(final String urlFileName)
	{
		this.urlFileName = urlFileName;
	}
}
