package de.hybris.platform.yacceleratorordermanagement.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.cancellation.OrderCancellationService;
import de.hybris.platform.warehousing.constants.WarehousingTestConstants;
import de.hybris.platform.warehousing.data.cancellation.CancellationEntry;
import de.hybris.platform.warehousing.data.shipping.ShippedEntry;
import de.hybris.platform.warehousing.shipping.ShippingService;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.util.CancellationEntryBuilder;
import de.hybris.platform.warehousing.util.OrderBuilder;
import de.hybris.platform.yacceleratorordermanagement.actions.order.SourceOrderAction;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@IntegrationTest
public class CancellationIntegrationTest extends ServicelayerTransactionalTest
{

	private final static String SHINBASHI = "Shinbashi";
	private final static String NAKANO = "Nakano";
	private final static String BASESTORE_NAME = "testStore";
	private final static String PRODUCT_1 = "product1";
	private final static String PRODUCT_2 = "product2";
	private final static String CONSIGNMENT_CODE = "con";

	@Resource
	private ProductService productService;

	@Resource
	private SourcingService sourcingService;

	@Resource
	private ModelService modelService;

	@Resource
	private AllocationService allocationService;

	@Resource
	CommonI18NService commonI18NService;

	@Resource
	private UserService userService;

	@Resource
	private PointOfServiceService pointOfServiceService;

	@Resource
	private BusinessProcessService businessProcessService;

	@Resource
	private BaseStoreService baseStoreService;

	@Resource
	private OrderCancellationService orderCancellationService;

	@Resource
	private ShippingService shippingService;

	@Resource
	private SourceOrderAction sourceOrderAction;

	@Resource
	private CommerceStockService commerceStockService;

	private CurrencyModel currency;

	public Map<String, Long> productInfo;
	public Map<AbstractOrderEntryModel, Long> cancellationEntryInfo;

	@Before
	public void setup() throws IOException, ImpExException
	{

		importCsv("/yacceleratorordermanagement/test/impex/integration-test-data-default.impex", WarehousingTestConstants.ENCODING);
		importCsv("/yacceleratorordermanagement/test/impex/integration-test-data-simple.impex", WarehousingTestConstants.ENCODING);

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
		cancellationEntryInfo = new HashMap<AbstractOrderEntryModel, Long>();
	}

	/**
	 * Given an order with 1 entries, and cancel:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * consignment should be cancelled<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the cancellation result<br>
	 */
	@Test
	public void cancelConsignmentEntrySuccess()
	{

		//Given
		productInfo.put("product1", new Long(3));


		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo);

		final OrderProcessModel process = getBusinessProcessService().<OrderProcessModel> createProcess(
				order.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.ORDER_PROCESS_NAME);
		process.setOrder(order);

		getModelService().save(process);

		businessProcessService.startProcess(process);

		cancellationEntryInfo.put(order.getEntries().get(0), 3L);

		final Collection<CancellationEntry> cancellationEntry = CancellationEntryBuilder.aCancellation()
				.build(cancellationEntryInfo);

		//when
		final Collection<CancellationEntry> cancellationResult = orderCancellationService.cancelOrder(order, cancellationEntry);

		// Then
		assertTrue(cancellationResult.size() == 0);

	}

	/**
	 * Given an order with 1 entries, and cancel more:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * cancellation:{quantity:30, product: product1)}
	 * <p>
	 * Result:<br>
	 * consignment should not be cancelled<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the cancellation result<br>
	 */
	@Test
	public void cancelConsignmentEntryFail()
	{

		//Given
		productInfo.put("product1", new Long(3));

		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo);

		final OrderProcessModel process = getBusinessProcessService().<OrderProcessModel> createProcess(
				order.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.ORDER_PROCESS_NAME);
		process.setOrder(order);

		getModelService().save(process);

		businessProcessService.startProcess(process);

		cancellationEntryInfo.put(order.getEntries().get(0), 30L);

		final Collection<CancellationEntry> cancellationEntry = CancellationEntryBuilder.aCancellation()
				.build(cancellationEntryInfo);

		//when
		final Collection<CancellationEntry> cancellationResult = orderCancellationService.cancelOrder(order, cancellationEntry);

		// Then
		assertTrue(cancellationResult.size() == 1);

		assertTrue(cancellationResult.stream().allMatch(result -> result.getQuantity() == 27));
	}

	/**
	 * Given an order with 1 entries, and cancel more:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * cancellation:{quantity:30, product: product1)}
	 * <p>
	 * Result:<br>
	 * consignment should not be cancelled<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the cancellation result<br>
	 */
	@Test
	@Ignore
	public void cancelConsignmentEntryWithoutConsignment()
	{

		//Given
		productInfo.put("product1", new Long(100));


		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo);

		final OrderProcessModel process = getBusinessProcessService().<OrderProcessModel> createProcess(
				order.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.ORDER_PROCESS_NAME);
		process.setOrder(order);

		getModelService().save(process);

		businessProcessService.startProcess(process);

		final Collection<CancellationEntry> cancellationEntry = CancellationEntryBuilder.aCancellation()
				.build(cancellationEntryInfo);

		//when
		final Collection<CancellationEntry> cancellationResult = orderCancellationService.cancelOrder(order, cancellationEntry);

		// Then
		assertTrue(cancellationResult.size() == 0);

	}

	/**
	 * Given an order with 2 entries, and cancel all:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * entry 2 : {quantity: 3, product: product2}<br>
	 * cancellation:{all}
	 * <p>
	 * Result:<br>
	 * consignment should be cancelled<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the cancellation result<br>
	 */
	@Test
	public void cancelMultiConsignmentEntry()
	{

		//Given
		productInfo.put("product1", new Long(3));
		productInfo.put("product2", new Long(3));


		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo);

		final OrderProcessModel process = getBusinessProcessService().<OrderProcessModel> createProcess(
				order.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.ORDER_PROCESS_NAME);
		process.setOrder(order);

		getModelService().save(process);

		businessProcessService.startProcess(process);

		order.getEntries().forEach(e -> cancellationEntryInfo.put(e, 3L));

		final Collection<CancellationEntry> cancellationEntry = CancellationEntryBuilder.aCancellation()
				.build(cancellationEntryInfo);

		//when
		final Collection<CancellationEntry> cancellationResult = orderCancellationService.cancelOrder(order, cancellationEntry);

		// Then
		assertTrue(cancellationEntry.size() == 2);
		assertTrue(cancellationResult.size() == 0);
	}

	/**
	 * Given an order with 1 entries sourced from 2 POS, and cancel all:<br>
	 * entry 1 : {quantity: 17, product: product1}<br>
	 * cancellation:{all}
	 * <p>
	 * Result:<br>
	 * consignment should be cancelled<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the cancellation result<br>
	 */
	@Test
	public void cancel1ConsignmentEntrySourcedFromMultiPOS()
	{

		//Given
		productInfo.put("product1", new Long(17));

		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo);

		final OrderProcessModel process = getBusinessProcessService().<OrderProcessModel> createProcess(
				order.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.ORDER_PROCESS_NAME);
		process.setOrder(order);

		getModelService().save(process);

		businessProcessService.startProcess(process);

		cancellationEntryInfo.put(order.getEntries().get(0), 17L);

		final Collection<CancellationEntry> cancellationEntry = CancellationEntryBuilder.aCancellation()
				.build(cancellationEntryInfo);

		//when
		final Collection<CancellationEntry> cancellationResult = orderCancellationService.cancelOrder(order, cancellationEntry);

		// Then
		assertTrue(cancellationResult.size() == 0);
	}


	/**
	 * Given an order with 1 entries sourced from 2 POS, and cancel only 1:<br>
	 * entry 1 : {quantity: 17, product: product1}<br>
	 * cancellation:{quantity: 1, product: product1}
	 * <p>
	 * Result:<br>
	 * consignment should be cancelled<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the cancellation result<br>
	 */
	@Test
	public void partiallyCancel1ConsignmentEntrySourcedFromMultiPOS()
	{

		//Given
		productInfo.put("product1", new Long(17));

		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo);

		final OrderProcessModel process = getBusinessProcessService().<OrderProcessModel> createProcess(
				order.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.ORDER_PROCESS_NAME);
		process.setOrder(order);

		getModelService().save(process);

		businessProcessService.startProcess(process);

		cancellationEntryInfo.put(order.getEntries().get(0), 1L);


		final Collection<CancellationEntry> cancellationEntry = CancellationEntryBuilder.aCancellation()
				.build(cancellationEntryInfo);

		//when
		final Collection<CancellationEntry> cancellationResult = orderCancellationService.cancelOrder(order, cancellationEntry);

		// Then
		assertTrue(cancellationResult.size() == 0);
	}

	/**
	 * Given an order with 1 entries, and cancel:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * ATS should be add back after cancellation<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the number of global/local atp<br>
	 */
	@Test
	public void cancelOrder_ATP()
	{
		//Given
		productInfo.put("product1", new Long(3));

		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo);
		final OrderProcessModel process = getBusinessProcessService().<OrderProcessModel> createProcess(
				order.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.ORDER_PROCESS_NAME);
		process.setOrder(order);

		getModelService().save(process);

		businessProcessService.startProcess(process);

		cancellationEntryInfo.put(order.getEntries().get(0), 2L);

		final Collection<CancellationEntry> cancellationEntry = CancellationEntryBuilder.aCancellation()
				.build(cancellationEntryInfo);

		orderCancellationService.cancelOrder(order, cancellationEntry);

		//when
		final Long globalATP_after = commerceStockService.getStockLevelForProductAndBaseStore(
				productService.getProductForCode(PRODUCT_1), baseStoreService.getBaseStoreForUid(BASESTORE_NAME));
		final Long localATP_SHINBASHI = commerceStockService.getStockLevelForProductAndPointOfService(
				productService.getProductForCode(PRODUCT_1), pointOfServiceService.getPointOfServiceForName(SHINBASHI));
		final Long localATP_NAKANO = commerceStockService.getStockLevelForProductAndPointOfService(
				productService.getProductForCode(PRODUCT_1), pointOfServiceService.getPointOfServiceForName(NAKANO));

		// Then
		assertThat(localATP_SHINBASHI, is(6L));
		assertThat(localATP_NAKANO, is(13L));
		assertThat(globalATP_after, is(19L));
	}

	/**
	 * Given an order with 1 entries, and cancel partially then create shipment<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * cancellation : {quantity: 1, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * 1 shipment should be created with quantity 2<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the shipment result<br>
	 */
	@Test
	@Ignore
	public void createShippedEntryAfterCancellation()
	{

		//Given
		productInfo.put(PRODUCT_1, new Long(3));


		final OrderModel order = OrderBuilder.aSourcingOrder().build(buildAddress(), currency, productService, productInfo);

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE,
				sourcingService.sourceOrder(order));

		cancellationEntryInfo.put(order.getEntries().get(0), 1L);
		final Collection<CancellationEntry> cancellationEntry = CancellationEntryBuilder.aCancellation()
				.build(cancellationEntryInfo);
		orderCancellationService.cancelOrder(order, cancellationEntry);

		BusinessProcessEvent.builder("waitForShipConsignment").withChoice("confirmShipConsignment").build();

		final Collection<ShippedEntry> shippedResult = new ArrayList<>();

		//when
		consignmentResult.stream().forEach(result -> shippingService.createShippedEntries(result));

		consignmentResult.stream().forEach(result -> shippedResult.addAll(shippingService.createShippedEntries(result)));

		// Then
		assertTrue(shippedResult.size() == 1);
		assertTrue(shippedResult.stream().allMatch(result -> result.getQuantity() == 2));
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

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}
}

