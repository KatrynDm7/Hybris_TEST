package de.hybris.platform.yacceleratorordermanagement.actions.order;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class GeocodeShippingAddressActionTest
{
	@Mock
	private GeoWebServiceWrapper geoWebServiceWrapper;

	@Mock
	private Converter addressConverter;

	@Mock
	private ModelService modelService;

	@Mock
	private GPS gps;

	@Mock
	private OrderModel order;

	private AddressModel address;

	@InjectMocks
	private GeocodeShippingAddressAction action;

	@Before
	public void setUp()
	{
		address = new AddressModel();
	}

	@Test
	public void shouldObtainLatitudeAndLongitudeFromDeliveryAddress()
	{
		when(geoWebServiceWrapper.geocodeAddress(any(AddressData.class))).thenReturn(gps);
		when(gps.getDecimalLatitude()).thenReturn(47.00);
		when(gps.getDecimalLongitude()).thenReturn(35.50);
		when(order.getDeliveryAddress()).thenReturn(address);

		final OrderProcessModel orderProcess = setupBasicOrderProcessModel();
		action.executeAction(orderProcess);

		verify(geoWebServiceWrapper).geocodeAddress(any(AddressData.class));

		assertTrue(orderProcess.getOrder().getDeliveryAddress().getLatitude() == 47.00);
		assertTrue(orderProcess.getOrder().getDeliveryAddress().getLongitude() == 35.50);
	}

	@Test
	public void shouldNotFailWhenGeoWebServiceWrapperIsUnavailable()
	{
		when(geoWebServiceWrapper.geocodeAddress(any(AddressData.class))).thenThrow(new GeoServiceWrapperException());

		final OrderProcessModel orderProcess = setupBasicOrderProcessModel();
		action.executeAction(orderProcess);

		verify(geoWebServiceWrapper).geocodeAddress(any(AddressData.class));

		assertTrue(orderProcess.getOrder().getDeliveryAddress().getLatitude() == null);
		assertTrue(orderProcess.getOrder().getDeliveryAddress().getLongitude() == null);
	}

	private OrderProcessModel setupBasicOrderProcessModel()
	{
		final CountryModel unitedStates = new CountryModel();
		unitedStates.setIsocode("US");

		final AddressModel broadwayNewYork = new AddressModel();
		broadwayNewYork.setCountry(unitedStates);
		broadwayNewYork.setDistrict("New York");
		broadwayNewYork.setStreetname("Broadway avenue");
		broadwayNewYork.setPostalcode("100001");

		final OrderModel order = new OrderModel();
		order.setDeliveryAddress(broadwayNewYork);

		final OrderProcessModel orderProcess = new OrderProcessModel();
		orderProcess.setOrder(order);

		return orderProcess;
	}

}