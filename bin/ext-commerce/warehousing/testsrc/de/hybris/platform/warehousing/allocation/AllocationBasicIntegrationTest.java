package de.hybris.platform.warehousing.allocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.warehousing.constants.WarehousingTestConstants;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.util.OrderBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AllocationBasicIntegrationTest extends ServicelayerTransactionalTest
{
	private final static String SHINBASHI = "Shinbashi";
	private final static String NAKANO = "Nakano";

	@Resource
	private ProductService productService;
	@Resource
	private SourcingService sourcingService;
	@Resource
	private ModelService modelService;
	@Resource
	private AllocationService allocationService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private UserService userService;
	@Resource
	private PointOfServiceService pointOfServiceService;

	@Resource
	private InventoryEventService inventoryEventService;

	@Resource
	private BaseStoreService baseStoreService;

	private CurrencyModel currency;
	private Map<String, Long> productInfo;
	private BaseStoreModel baseStore;

	@Before
	public void setup() throws IOException, ImpExException
	{

		importCsv("/warehousing/test/impex/sourcingIntegration-test-data-default.impex", WarehousingTestConstants.ENCODING);
		importCsv("/warehousing/test/impex/consignmentIntegration-test-data-simple.impex", WarehousingTestConstants.ENCODING);

		try
		{
			currency = commonI18NService.getCurrency("USD");
		}
		catch (final UnknownIdentifierException e)
		{
			currency = new CurrencyModel();
			currency.setIsocode("USD");
			currency.setDigits(Integer.valueOf(2));
			modelService.save(currency);
		}

		productInfo = new HashMap<String, Long>();
		baseStore = baseStoreService.getAllBaseStores().iterator().next();
	}

	/**
	 * Given an pickup order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * It should source complete from 1 location, NAKANO<br>
	 * {Warehouse: Shinbashi, Availability : {[product1,6]}<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the number of consignment/allocation result<br>
	 * It verifies the warehouse where the consignment/allocation were sourced<br>
	 * It checks if the create Consignment/allocation is completed.
	 */
	@Test
	public void createPickupOrder()
	{
		productInfo.put("product1", new Long(3));
		final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(NAKANO);
		final DeliveryModeModel deliveryModeModel = new DeliveryModeModel();
		deliveryModeModel.setCode("pickup");
		modelService.save(deliveryModeModel);

		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo, pos,
				deliveryModeModel);

		final SourcingResults results = sourcingService.sourceOrder(order);

		//sourcing result is not null and sourcing is complete
		assertTrue(results.isComplete());

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, "con", results);

		assertTrue(consignmentResult.size() == 1);

		assertTrue(consignmentResult.stream().allMatch(result -> result.getStatus().getCode().equals("READY")));

		assertTrue(consignmentResult.stream().allMatch(result -> result.getWarehouse().getName().equals("Warehouse 1")
				&& result.getDeliveryPointOfService().getName().equals(NAKANO)));

		assertTrue(consignmentResult.stream()
				.allMatch(result -> result.getConsignmentEntries().stream().allMatch(e -> e.getQuantity() == 3)));

		final Collection<AllocationEventModel> allocationResult = consignmentResult.stream()
				.map(result -> inventoryEventService.createAllocationEvents(result)).flatMap(result -> result.stream())
				.collect(Collectors.toList());

		assertTrue(allocationResult.size() == 1);

		assertTrue(
				allocationResult.stream().allMatch(result -> result.getStockLevel().getWarehouse().getName().equals("Warehouse 1")));

		assertTrue(allocationResult.stream().allMatch(result -> result.getQuantity() == 3));
	}

	/**
	 * Given an pickup order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * entry 2 : {quantity: 2, product: product2}<br>
	 * <p>
	 * Result:<br>
	 * It should source complete from 1 location, NAKANO<br>
	 * {Warehouse: Shinbashi, Availability : {[product1,6], [product2,6]}<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the number of consignment/allocation result<br>
	 * It verifies the warehouse where the consignment/allocation were sourced<br>
	 * It checks if the create Consignment/allocation is completed.
	 */
	@Test
	public void createPickupOrderWith2Entries()
	{
		productInfo.put("product1", new Long(3));
		productInfo.put("product2", new Long(3));
		final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(NAKANO);
		final DeliveryModeModel deliveryModeModel = new PickUpDeliveryModeModel();
		deliveryModeModel.setCode("pickup");
		modelService.save(deliveryModeModel);
		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo, pos,
				deliveryModeModel);

		final SourcingResults results = sourcingService.sourceOrder(order);
		assertTrue(results.isComplete());
		assertNotNull(results.getResults());
		assertFalse(results.getResults().isEmpty());
		assertEquals(1, results.getResults().size());

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, "con", results);

		assertTrue(consignmentResult.size() == 1);
		assertTrue(consignmentResult.stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertTrue(consignmentResult.stream().allMatch(
				result -> result.getWarehouse().getCode().equals(NAKANO)
				&& result.getDeliveryPointOfService().getName().equals(NAKANO)));
		assertTrue(consignmentResult.stream().allMatch(
				result -> result.getConsignmentEntries().stream().allMatch(e -> e.getQuantity() == 3)));

		final Collection<AllocationEventModel> allocationResult = consignmentResult.stream()
				.map(result -> inventoryEventService.createAllocationEvents(result)).flatMap(result -> result.stream())
				.collect(Collectors.toList());
		assertTrue(allocationResult.size() == 2);
		assertTrue(consignmentResult.stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertTrue(allocationResult.stream().allMatch(result -> result.getStockLevel().getWarehouse().getCode().equals(NAKANO)));
		assertTrue(allocationResult.stream().allMatch(result -> result.getQuantity() == 3));
	}

	private AddressModel buildAddress()
	{
		final AddressModel address = new AddressModel();
		final Country country = jaloSession.getC2LManager().getCountryByIsoCode("US");
		address.setCountry((CountryModel) modelService.get(country));
		final UserModel user = userService.getCurrentUser();
		address.setOwner(user);
		return address;
	}

}
