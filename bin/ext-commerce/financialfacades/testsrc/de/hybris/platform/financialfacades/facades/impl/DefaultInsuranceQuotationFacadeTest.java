package de.hybris.platform.financialfacades.facades.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.quotation.QuotationItemRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationItemResponseData;
import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationResponseData;
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;
import de.hybris.platform.financialfacades.strategies.QuotationPricingStrategy;
import de.hybris.platform.financialfacades.strategies.impl.TravelInsuranceQuotationPricingStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@UnitTest
public class DefaultInsuranceQuotationFacadeTest
{
	private static String TRAVEL_PROVIDER_NAME = "travelInsurance";
	private static String NUMBER_OF_TRAVELLER = "Travellers";
	private static String NUMBER_OF_DAYS = "NoOfDays";

	protected static String UNICODE_KEY = "travel.quotation.data.unicode";
	protected static String DATA_FILE_PATH_KEY = "travel.quotation.data.pathname";
	protected static String DATA_KEY = "travel.quotation.data.key";

	@Mock
	private Configuration configuration;

	@Mock
	private ConfigurationService configurationService;

	private DefaultQuotationPricingFacade quotationFacade;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		/*
		 * ... setting up of non-mocks must come after.
		 */
		quotationFacade = new DefaultQuotationPricingFacade();

		final TravelInsuranceQuotationPricingStrategy quotationStrategy = new TravelInsuranceQuotationPricingStrategy();

		final TravelInsuranceQuotationPricingStrategy spy = Mockito.spy(quotationStrategy);

		final Map<String, QuotationPricingStrategy> strategies = new HashMap<String, QuotationPricingStrategy>();
		strategies.put("travelInsurance", spy);
		quotationFacade.setStrategies(strategies);

		Mockito.doReturn(configurationService).when(spy).getConfigurationService();
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(Matchers.anyString(), Matchers.<String> any())).thenAnswer(
				AdditionalAnswers.returnsSecondArg());
		Mockito.when(configuration.getDouble(Matchers.anyString(), Matchers.<Double> any())).thenAnswer(
				AdditionalAnswers.returnsSecondArg());

		spy.initStrategy(); // initialize the maps for the strategy

	}

	@Test
	public void testGetQuoteProductPlan()
	{
		final QuotationRequestData requestData = buildQuotationRequest(TRAVEL_PROVIDER_NAME, "TRA_SINGLE_GOLD");

		// Call for the quotation Mock
		final QuotationResponseData responseData = quotationFacade.getQuote(requestData);

		//Assert status and the response data.
		Assert.assertEquals(responseData.getStatus().getReasonCode(), "SUCCESS");

		for (final QuotationItemResponseData data : responseData.getItems())
		{
			Assert.assertEquals(data.getId(), "TRA_SINGLE_GOLD");
			Assert.assertEquals(data.getPayNowPrice(), "56.0");
		}

	}

	@Test
	public void testGetQuoteProductAndOptions()
	{

		final QuotationRequestData requestData = buildQuotationRequest(TRAVEL_PROVIDER_NAME, "TRA_SINGLE_GOLD");

		final QuotationItemRequestData optionalData = buildQuotationItemRequest("TRA_GOLF");
		requestData.getItems().add(optionalData);

		// Call for the quotation Mock
		final QuotationResponseData responseData = quotationFacade.getQuote(requestData);

		//Assert status and the response data.
		Assert.assertEquals(responseData.getStatus().getReasonCode(), "SUCCESS");

		if (responseData.getItems() != null && responseData.getItems().size() != 2)
		{
			Assert.fail();
		}

		for (final QuotationItemResponseData data : responseData.getItems())
		{
			if ("TRA_SINGLE_GOLD".equals(data.getId()))
			{
				Assert.assertEquals(data.getPayNowPrice(), "56.0");
			}
			else if ("TRA_GOLF".equals(data.getId()))
			{
				Assert.assertEquals(data.getPayNowPrice(), "393.0");
			}
			else
			{
				Assert.fail();
			}
		}

	}

	@Test
	public void testGetQuoteNodata()
	{
		final QuotationRequestData requestData = new QuotationRequestData();
		final List<QuotationItemRequestData> productRequestDataList = Lists.newArrayList();
		final QuotationItemRequestData productRequestData = new QuotationItemRequestData();

		// Prepare invalid generic parameters
		final Map<String, String> property = new HashMap<String, String>();
		property.put(NUMBER_OF_TRAVELLER, "100");
		property.put(NUMBER_OF_DAYS, "45");

		requestData.setProviderName(TRAVEL_PROVIDER_NAME);
		requestData.setProperties(property);

		//Prepare product request data
		productRequestData.setId("TRA_SINGLE_GOLD");
		productRequestDataList.add(productRequestData);
		requestData.setItems(productRequestDataList);

		// Call for the quotation Mock
		final QuotationResponseData responseData = quotationFacade.getQuote(requestData);

		//Assert status and the response data.
		Assert.assertEquals(responseData.getStatus().getReasonCode(), "INVALID_REQUEST");
	}

	@Test
	public void testGetQuoteAndProductPlanSettingBadProperty()
	{
		Mockito.when(configuration.getDouble(Matchers.matches("factor.no.traveller"), Matchers.<Double> any())).thenReturn(
				new Double(750.1));

		final QuotationRequestData requestData = buildQuotationRequest(TRAVEL_PROVIDER_NAME, "TRA_SINGLE_GOLD");

		// Call for the quotation Mock
		final QuotationResponseData responseData = quotationFacade.getQuote(requestData);

		//Assert status and the response data.
		Assert.assertEquals(responseData.getStatus().getReasonCode(), "SUCCESS");

		for (final QuotationItemResponseData data : responseData.getItems())
		{
			Assert.assertEquals(data.getId(), "TRA_SINGLE_GOLD");
			Assert.assertEquals(data.getPayNowPrice(), "56.0");
		}
	}

	@Test
	public void testGetQuoteAndProductPlanSettingGoodProperty()
	{
		Mockito.when(
				configuration.getDouble(Matchers.matches(FinancialfacadesConstants.PROPERTY_NO_TRAVELLERS), Matchers.<Double> any()))
				.thenReturn(new Double(500.0));

		final QuotationRequestData requestData = buildQuotationRequest(TRAVEL_PROVIDER_NAME, "TRA_SINGLE_GOLD");

		// Call for the quotation Mock
		final QuotationResponseData responseData = quotationFacade.getQuote(requestData);

		//Assert status and the response data.
		Assert.assertEquals(responseData.getStatus().getReasonCode(), "SUCCESS");

		for (final QuotationItemResponseData data : responseData.getItems())
		{
			Assert.assertEquals(data.getId(), "TRA_SINGLE_GOLD");
			Assert.assertEquals(data.getPayNowPrice(), "44.0");
		}
	}

	private QuotationRequestData buildQuotationRequest(final String providerName, final String itemId)
	{
		final QuotationRequestData requestData = new QuotationRequestData();
		final List<QuotationItemRequestData> productRequestDataList = Lists.newArrayList();
		final QuotationItemRequestData productRequestData = new QuotationItemRequestData();

		//populating requested items
		productRequestData.setId(itemId);
		productRequestData.setProperties(populateItemProperteis());
		productRequestDataList.add(productRequestData);

		//add parameters to the request data.
		requestData.setProviderName(providerName);
		requestData.setProperties(populateCommonProperties());

		requestData.setItems(productRequestDataList);

		return requestData;
	}

	private QuotationItemRequestData buildQuotationItemRequest(final String itemId)
	{
		final QuotationItemRequestData productRequestData = new QuotationItemRequestData();
		//populating requested items
		productRequestData.setId(itemId);
		productRequestData.setProperties(populateExtraItemProperteis());
		return productRequestData;
	}

	private Map<String, String> populateItemProperteis()
	{
		final Map<String, String> coverageProperties = new HashMap<String, String>();
		coverageProperties.put("cancellation", "7500");
		coverageProperties.put("excesswaivercoverage", "100");
		return coverageProperties;
	}

	private Map<String, String> populateExtraItemProperteis()
	{
		final Map<String, String> coverageProperties = new HashMap<String, String>();
		coverageProperties.put("cancellation", "7500");
		coverageProperties.put("excesswaivercoverage", "100");
		coverageProperties.put("golfcover", "1500");
		return coverageProperties;
	}

	private Map<String, String> populateCommonProperties()
	{
		// Prepare generic parameters
		final Map<String, String> property = new HashMap<String, String>();
		property.put(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS, "2");
		property.put(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_DAYS, "31");
		property.put(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION, "UK");
		property.put(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES, "22,65");
		return property;
	}
}
