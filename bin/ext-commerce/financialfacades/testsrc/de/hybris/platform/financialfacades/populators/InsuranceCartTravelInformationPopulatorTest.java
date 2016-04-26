package de.hybris.platform.financialfacades.populators;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;


/**
 * The class of InsuranceCartEntryTravelInformationPopulatorTest.
 */
@UnitTest
public class InsuranceCartTravelInformationPopulatorTest
{

	@InjectMocks
	private InsuranceOrderQuotePopulator populator;

	@Mock
	private Converter<InsuranceQuoteModel, InsuranceQuoteData> insuranceQuoteConverter;

	private TravelDataPopulatorStrategy travelDataPopulatorStrategy;

	private static String EXPECT_DATE_FORMAT = "dd-MM-yyyy";

	@Before
	public void setup()
	{
		final List<InsuranceDataPopulatorStrategy> strategyList = new ArrayList<InsuranceDataPopulatorStrategy>();

		populator = new InsuranceOrderQuotePopulator();
		populator.setInsuranceDataPopulatorStrategies(strategyList);

		final TravelDataPopulatorStrategy travelDataPopulatorStrategy = new TravelDataPopulatorStrategy();
		travelDataPopulatorStrategy.setDateFormatForDisplay(EXPECT_DATE_FORMAT);
		strategyList.add(travelDataPopulatorStrategy);

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPopulate()
	{
		final String destination = "London";
		final Integer noOfTravellers = Integer.valueOf(2);
		final Integer noOfDays = Integer.valueOf(2);
		final String startDate = "18/09/2014";
		final String endDate = "22/09/2014";
		final String expStartDate = "18-09-2014";
		final String expEndDate = "22-09-2014";

		final DateTimeFormatter formatter = DateTimeFormat
				.forPattern(FinancialfacadesConstants.TEST_TRAVEL_INSURANCE_DATE_DISPLAY_FORMAT);
		final Date tripStartDate = formatter.parseDateTime(startDate).toDate();
		final Date tripEndDate = formatter.parseDateTime(endDate).toDate();

		final AbstractOrderModel orderModel = new AbstractOrderModel();
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();

		final InsuranceQuoteData quoteData = new InsuranceQuoteData();

		quoteData.setTripDestination(destination);
		quoteData.setTripStartDate(startDate);
		quoteData.setTripNoOfTravellers(noOfTravellers);
		quoteData.setTripEndDate(endDate);

		when(insuranceQuoteConverter.convert(quoteModel)).thenReturn(quoteData);

		final Map<String, Object> properties = Maps.newHashMap();
		properties.put(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION, destination);
		properties.put(FinancialfacadesConstants.TRIP_DETAILS_START_DATE, tripStartDate);
		properties.put(FinancialfacadesConstants.TRIP_DETAILS_END_DATE, tripEndDate);
		properties.put(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS, noOfTravellers);
		properties.put(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_DAYS, noOfDays);

		quoteModel.setProperties(properties);
		orderModel.setInsuranceQuote(quoteModel);

		final OrderData orderData = new OrderData();
		populator.populate(orderModel, orderData);

		Assert.assertEquals(destination, orderData.getInsuranceQuote().getTripDestination());
		Assert.assertEquals(noOfTravellers, orderData.getInsuranceQuote().getTripNoOfTravellers());
		Assert.assertEquals(expStartDate, orderData.getInsuranceQuote().getTripStartDate());
		Assert.assertEquals(expEndDate, orderData.getInsuranceQuote().getTripEndDate());

	}
}
