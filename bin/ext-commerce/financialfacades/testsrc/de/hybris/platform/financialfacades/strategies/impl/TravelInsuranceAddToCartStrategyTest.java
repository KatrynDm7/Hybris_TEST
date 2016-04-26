package de.hybris.platform.financialfacades.strategies.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.form.YFormService;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



/**
 * The class of TravelInsuranceAddToCartStrategyTest.
 */
public class TravelInsuranceAddToCartStrategyTest
{

	@InjectMocks
	private TravelInsuranceAddToCartStrategy travelInsuranceAddToCartStrategy;

	@Mock
	private ModelService modelService;

	@Mock
	private SessionService sessionService;

	@Mock
	private CartService cartService;

	@Mock
	private ProductService productService;

	@Mock
	private YFormService yFormService;

	@Before
	public void setup()
	{
		travelInsuranceAddToCartStrategy = new TravelInsuranceAddToCartStrategy();

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldPersistTravelInsuranceInformationForNewBundle() throws YFormServiceException
	{
		final String productCode = "testProductCode";
		final String formDataId = "8796125829317";
		final String destination = "London";
		final String startDate = "2014-09-18";
		final String endDate = "2014-09-23";
		final String noOfTravellers = "2";
		final List<String> ages = Arrays.asList("22", "25");

		final CartModel cart = new CartModel();
		final ProductModel product = new ProductModel();

		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();

		when(cartService.getSessionCart()).thenReturn(cart);
		when(cartService.hasSessionCart()).thenReturn(true);
		when(productService.getProductForCode(productCode)).thenReturn(product);
		when(modelService.create(InsuranceQuoteModel.class)).thenReturn(quoteModel);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_FORM_DATA_ID)).thenReturn(formDataId);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION)).thenReturn(destination);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_START_DATE)).thenReturn(startDate);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_END_DATE)).thenReturn(endDate);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS)).thenReturn(noOfTravellers);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES)).thenReturn(ages);

		travelInsuranceAddToCartStrategy.persistInsuranceInformation();

		Assert.assertEquals(destination, quoteModel.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION));
		Assert.assertEquals(noOfTravellers,
				MapUtils.getString(quoteModel.getProperties(), FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS));
		Assert.assertEquals(ages, quoteModel.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES));

		final DateTimeFormatter formatter = DateTimeFormat.forPattern(FinancialfacadesConstants.INSURANCE_GENERIC_DATE_FORMAT);

		Assert.assertEquals(formatter.parseDateTime(startDate).toDate(),
				quoteModel.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_START_DATE));
		Assert.assertEquals(formatter.parseDateTime(endDate).toDate(),
				quoteModel.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_END_DATE));

		verify(modelService, Mockito.times(2)).save(Mockito.any());
	}

	@Test
	public void shouldPersistTravelInsuranceInformationIntoExistingBundle() throws YFormServiceException
	{
		final String productCode = "testProductCode";
		final String formDataId = "8796125829317";
		final String destination = "London";
		final String startDate = "2014-09-18";
		final String endDate = "2014-09-23";
		final String noOfTravellers = "2";
		final List<String> ages = Arrays.asList("22", "25");

		final CartModel cart = new CartModel();
		final ProductModel product = new ProductModel();

		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();

		when(cartService.getSessionCart()).thenReturn(cart);
		when(cartService.hasSessionCart()).thenReturn(true);
		when(productService.getProductForCode(productCode)).thenReturn(product);
		when(modelService.create(InsuranceQuoteModel.class)).thenReturn(quoteModel);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_FORM_DATA_ID)).thenReturn(formDataId);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION)).thenReturn(destination);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_START_DATE)).thenReturn(startDate);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_END_DATE)).thenReturn(endDate);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS)).thenReturn(noOfTravellers);
		when(sessionService.getAttribute(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES)).thenReturn(ages);

		travelInsuranceAddToCartStrategy.persistInsuranceInformation();

		Assert.assertEquals(destination, quoteModel.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION));
		Assert.assertEquals(noOfTravellers,
				MapUtils.getString(quoteModel.getProperties(), FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS));
		Assert.assertEquals(ages, quoteModel.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES));

		final DateTimeFormatter formatter = DateTimeFormat.forPattern(FinancialfacadesConstants.INSURANCE_GENERIC_DATE_FORMAT);

		Assert.assertEquals(formatter.parseDateTime(startDate).toDate(),
				quoteModel.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_START_DATE));
		Assert.assertEquals(formatter.parseDateTime(endDate).toDate(),
				quoteModel.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_END_DATE));


		verify(modelService, Mockito.times(2)).save(Mockito.any());
	}
}
