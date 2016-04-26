package de.hybris.platform.warehousing.returns;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.constants.WarehousingTestConstants;
import de.hybris.platform.warehousing.data.shipping.ShippedEntry;
import de.hybris.platform.warehousing.shipping.ShippingService;
import de.hybris.platform.warehousing.shipping.impl.ShippingException;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.util.OrderBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ReturnBasicIntegrationTest extends ServicelayerTransactionalTest
{

	private final static String PRODUCT_1 = "product1";
	private final static String PRODUCT_2 = "product2";
	private final static String CONSIGNMENT_CODE = "con";
	private final static String SHINBASHI = "Shinbashi";
	private final static String PICKUP_CODE = "pickup";
	private CurrencyModel currency;

	public Map<String, Long> productInfo;
	public Map<AbstractOrderEntryModel, Long> returnEntryInfo;

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
	private ShippingService shippingService;
	@Resource
	private ReturnService returnService;
	@Resource
	private PointOfServiceService pointOfServiceService;
	@Resource
	private BaseStoreService baseStoreService;

	private String refundNotes;
	private ReturnAction action;
	private RefundReason reason;
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

		returnEntryInfo = new HashMap<>();
		productInfo = new HashMap<>();
		action = ReturnAction.IMMEDIATE;
		reason = RefundReason.DAMAGEDINTRANSIT;
		baseStore = baseStoreService.getAllBaseStores().iterator().next();
	}

	/**
	 * Given an order with 1 entry and 1 consignment<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * Create a return for all items<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Return should be created with 1 refund entry<br>
	 * <p>
	 * Assert:<br>
	 * Return is a valid BORIS return<br>
	 */
	@Test
	public void shouldSucceed_orderHasOneEntry()
	{
		//Given
		final OrderModel orderWithOneEntry = shipOrderWithOneEntry();
		final AbstractOrderEntryModel orderEntry = orderWithOneEntry.getEntries().get(0);
		final Long expectedQuantity = 3L;

		//When
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithOneEntry);
		final RefundEntryModel refund = returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action,
				reason);

		//Then
		assertEquals(orderWithOneEntry, request.getOrder());
		assertEquals(request, refund.getReturnRequest());
		assertEquals(orderEntry, refund.getOrderEntry());
		assertEquals(refundNotes, refund.getNotes());
		assertEquals(expectedQuantity, refund.getExpectedQuantity());
		assertEquals(action, refund.getAction());
		assertEquals(reason, refund.getReason());

		//For 5.7 only
		validateBorisReturn(request, refund);
	}

	/**
	 * Given an pickup order with 1 entry and 1 consignment<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * Create a return<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Return should be created with 1 refund entry<br>
	 * <p>
	 * Assert:<br>
	 * Return is a valid BORIS return<br>
	 */
	@Test
	public void shouldSucceed_pickupOrderHasOneEntry()
	{
		//Given
		final OrderModel orderWithOneEntry = confirmPickupOrderWithOneEntry();
		final AbstractOrderEntryModel orderEntry = orderWithOneEntry.getEntries().get(0);
		final Long expectedQuantity = 3L;

		//When
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithOneEntry);
		final RefundEntryModel refund = returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action,
				reason);

		//Then
		assertEquals(orderWithOneEntry, request.getOrder());
		assertEquals(request, refund.getReturnRequest());
		assertEquals(orderEntry, refund.getOrderEntry());
		assertEquals(refundNotes, refund.getNotes());
		assertEquals(expectedQuantity, refund.getExpectedQuantity());
		assertEquals(action, refund.getAction());
		assertEquals(reason, refund.getReason());

		//For 5.7 only
		validateBorisReturn(request, refund);
	}

	/**
	 * Given an order with 1 entry and 1 consignment<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * Create a return for a lower quantity<br>
	 * entry 1 : {quantity: 2, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Return should be created with 1 refund entry<br>
	 * <p>
	 * Assert:<br>
	 * Return is a valid BORIS return<br>
	 */
	@Test
	public void shouldSucceed_orderHasOneEntry_expectedQuantityLower()
	{
		//Given
		final OrderModel orderWithOneEntry = shipOrderWithOneEntry();
		final AbstractOrderEntryModel orderEntry = orderWithOneEntry.getEntries().get(0);
		final Long expectedQuantity = 2L;

		//When
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithOneEntry);
		final RefundEntryModel refund = returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action,
				reason);

		//Then
		assertEquals(orderWithOneEntry, request.getOrder());
		assertEquals(request, refund.getReturnRequest());
		assertEquals(orderEntry, refund.getOrderEntry());
		assertEquals(refundNotes, refund.getNotes());
		assertEquals(expectedQuantity, refund.getExpectedQuantity());
		assertEquals(action, refund.getAction());
		assertEquals(reason, refund.getReason());

		//For 5.7 only
		validateBorisReturn(request, refund);
	}

	/**
	 * Given an order with 1 entry and 1 consignment<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * Create a return for a higher quantity<br>
	 * entry 1 : {quantity: 4, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Creating return throws exception<br>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_expectedQuantityTooHigh()
	{
		//Given
		final OrderModel orderWithOneEntry = shipOrderWithOneEntry();
		final AbstractOrderEntryModel orderEntry = orderWithOneEntry.getEntries().get(0);
		final Long expectedQuantity = 4L;

		//When
		//We shouldn't allow returning more than ordered
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithOneEntry);
		returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action, reason);
	}

	/**
	 * Given an order with 1 entry that is not shipped yet<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * Create a return<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Creating return throws exception<br>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_orderIsNotShipped()
	{
		//Given
		final OrderModel orderWithOneEntry = createOrderWithOneEntry();
		final AbstractOrderEntryModel orderEntry = orderWithOneEntry.getEntries().get(0);
		final Long expectedQuantity = 1L;

		//When
		//Cannot return anything when there are no consignments shipped
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithOneEntry);
		final RefundEntryModel refund = returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action,
				reason);
	}

	/**
	 * Given an order with 1 entry<br>
	 * entry 1 : {quantity: 15, product: product1}<br>
	 * That is split across consignments<br>
	 * consignment 1 : {quantity: 6, product: product1}<br>
	 * consignment 2 : {quantity: 2, product: product1}<br>
	 * consignment 3 : {quantity: 7, product: product1}<br>
	 * Create a return for all items<br>
	 * entry 1 : {quantity: 15, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Return should be created with 1 refund entry<br>
	 * <p>
	 * Assert:<br>
	 * Return is a valid BORIS return<br>
	 */
	@Test
	public void shouldSucceed_orderHasManyConsignments()
	{
		//Given
		final OrderModel orderWithManyConsignments = shipOrderWithManyConsignments();
		final AbstractOrderEntryModel orderEntry = orderWithManyConsignments.getEntries().get(0);
		final Long expectedQuantity = 15L;

		//When
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithManyConsignments);
		final RefundEntryModel refund = returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action,
				reason);

		//Then
		assertEquals(orderWithManyConsignments, request.getOrder());
		assertEquals(request, refund.getReturnRequest());
		assertEquals(orderEntry, refund.getOrderEntry());
		assertEquals(refundNotes, refund.getNotes());
		assertEquals(expectedQuantity, refund.getExpectedQuantity());
		assertEquals(action, refund.getAction());
		assertEquals(reason, refund.getReason());

		//For 5.7 only
		validateBorisReturn(request, refund);
	}

	/**
	 * Given an order with 1 entry<br>
	 * entry 1 : {quantity: 15, product: product1}<br>
	 * That is split across consignments<br>
	 * consignment 1 : {quantity: 6, product: product1}<br>
	 * consignment 2 : {quantity: 2, product: product1}<br>
	 * consignment 3 : {quantity: 7, product: product1}<br>
	 * But only consignment 1 is shipped<br>
	 * Create a return for the shipped items only<br>
	 * entry 1 : {quantity: 7, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Return should be created with 1 refund entry<br>
	 * <p>
	 * Assert:<br>
	 * Return is a valid BORIS return<br>
	 *
	 * @throws ShippingException
	 */
	@Test
	public void shouldSucceed_orderPartiallyShipped() throws ShippingException
	{
		//Given
		final OrderModel orderWithManyConsignments = partiallyShipOrderWithManyConsignments();
		final AbstractOrderEntryModel orderEntry = orderWithManyConsignments.getEntries().get(0);

		//Only try to return the quantity of the shipped consignment 1
		final Long expectedQuantity = 7L;

		//When
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithManyConsignments);
		final RefundEntryModel refund = returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action,
				reason);

		//Then
		assertEquals(orderWithManyConsignments, request.getOrder());
		assertEquals(request, refund.getReturnRequest());
		assertEquals(orderEntry, refund.getOrderEntry());
		assertEquals(refundNotes, refund.getNotes());
		assertEquals(expectedQuantity, refund.getExpectedQuantity());
		assertEquals(action, refund.getAction());
		assertEquals(reason, refund.getReason());

		//For 5.7 only
		validateBorisReturn(request, refund);
	}

	/**
	 * Given an order with 1 entry<br>
	 * entry 1 : {quantity: 15, product: product1}<br>
	 * That is split across consignments<br>
	 * consignment 1 : {quantity: 6, product: product1}<br>
	 * consignment 2 : {quantity: 2, product: product1}<br>
	 * consignment 3 : {quantity: 7, product: product1}<br>
	 * But only consignment 1 is shipped<br>
	 * Create a return for all items<br>
	 * entry 1 : {quantity: 15, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Creating return throws exception<br>
	 *
	 * @throws ShippingException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_orderPartiallyShipped_expectedQuantityTooHigh() throws ShippingException
	{
		//Given
		final OrderModel orderWithOneEntry = partiallyShipOrderWithManyConsignments();
		final AbstractOrderEntryModel orderEntry = orderWithOneEntry.getEntries().get(0);
		final Long expectedQuantity = 15L;

		//When
		//Cannot return everything when there are some consignments not shipped
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithOneEntry);
		final RefundEntryModel refund = returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action,
				reason);
	}

	/**
	 * Given an order with 2 entries<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * entry 2 : {quantity: 5, product: product2}<br>
	 * Create a return for all items<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * entry 2 : {quantity: 5, product: product2}<br>
	 * <p>
	 * Result:<br>
	 * Return should be created with 2 refund entries<br>
	 * <p>
	 * Assert:<br>
	 * Return is a valid BORIS return<br>
	 */
	@Test
	public void shouldSucceed_orderHasManyEntries()
	{
		//Given
		final OrderModel orderWithManyEntries = shipOrderWithManyEntries();
		final List<AbstractOrderEntryModel> orderEntries = orderWithManyEntries.getEntries();
		final AbstractOrderEntryModel orderEntry1 = orderEntries.get(0);
		final AbstractOrderEntryModel orderEntry2 = orderEntries.get(1);
		final Long expectedQuantity1 = orderEntry1.getQuantityShipped();
		final Long expectedQuantity2 = orderEntry2.getQuantityShipped();

		//When
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithManyEntries);
		final RefundEntryModel refund1 = returnService.createRefund(request, orderEntry1, refundNotes, expectedQuantity1, action,
				reason);
		final RefundEntryModel refund2 = returnService.createRefund(request, orderEntry2, refundNotes, expectedQuantity2, action,
				reason);

		//Then
		assertEquals(orderWithManyEntries, request.getOrder());

		assertEquals(request, refund1.getReturnRequest());
		assertEquals(orderEntry1, refund1.getOrderEntry());
		assertEquals(refundNotes, refund1.getNotes());
		assertEquals(expectedQuantity1, refund1.getExpectedQuantity());
		assertEquals(action, refund1.getAction());
		assertEquals(reason, refund1.getReason());

		assertEquals(request, refund2.getReturnRequest());
		assertEquals(orderEntry2, refund2.getOrderEntry());
		assertEquals(refundNotes, refund2.getNotes());
		assertEquals(expectedQuantity2, refund2.getExpectedQuantity());
		assertEquals(action, refund2.getAction());
		assertEquals(reason, refund2.getReason());

		//For 5.7 only
		validateBorisReturn(request, refund1);
		validateBorisReturn(request, refund2);
	}

	/**
	 * Given an order with 2 entries<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * entry 2 : {quantity: 5, product: product2}<br>
	 * Create a return with one quantity too high<br>
	 * entry 1 : {quantity: 5, product: product1}<br>
	 * entry 2 : {quantity: 3, product: product2}<br>
	 * <p>
	 * Result:<br>
	 * Creating return throws exception<br>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_orderHasManyEntries_expectedQuantityOfOneTooHigh()
	{
		//Given
		final OrderModel orderWithManyEntries = shipOrderWithManyEntries();
		final List<AbstractOrderEntryModel> orderEntries = orderWithManyEntries.getEntries();
		final AbstractOrderEntryModel orderEntry1 = orderEntries.get(0);
		final AbstractOrderEntryModel orderEntry2 = orderEntries.get(1);

		//Try to invert the quantities to return, so we return more of product 1 than was ordered
		final Long expectedQuantity1 = orderEntry2.getQuantityShipped();
		final Long expectedQuantity2 = orderEntry1.getQuantityShipped();

		//When
		//Can't return more than what was ordered by putting other product's quantity
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithManyEntries);
		returnService.createRefund(request, orderEntry1, refundNotes, expectedQuantity1, action, reason);
		returnService.createRefund(request, orderEntry2, refundNotes, expectedQuantity2, action, reason);
	}

	/**
	 * Given an order with 1 entry:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * with an existing pending return<br>
	 * entry 1 : {quantity: 2, product: product1}<br>
	 * Create another return that would make the total quantity too high<br>
	 * entry 1 : {quantity: 2, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Creating the new return throws exception<br>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_pendingReturnExists_expectedQuantityTooHigh()
	{
		//Given
		final OrderModel orderWithOneEntry = shipOrderWithOneEntry();
		final AbstractOrderEntryModel orderEntry = orderWithOneEntry.getEntries().get(0);
		final Long expectedQuantity = 2L;

		final ReturnRequestModel result1 = returnService.createReturnRequest(orderWithOneEntry);
		returnService.createRefund(result1, orderEntry, refundNotes, expectedQuantity, action, reason);

		//When
		//Can't allow return more than (ordered - pending return)
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithOneEntry);
		returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action, reason);
	}

	/**
	 * Given an order with 1 entry:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * with an existing completed return<br>
	 * entry 1 : {quantity: 2, product: product1}<br>
	 * Create another return that would make the total quantity too high<br>
	 * entry 1 : {quantity: 2, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Creating the new return throws exception<br>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_completedReturnExists_expectedQuantityTooHigh()
	{
		//Given
		final OrderModel orderWithOneEntry = shipOrderWithOneEntry();
		final AbstractOrderEntryModel orderEntry = orderWithOneEntry.getEntries().get(0);
		final Long expectedQuantity = 2L;

		final ReturnRequestModel completedRequest = returnService.createReturnRequest(orderWithOneEntry);
		final RefundEntryModel completedRefund = returnService.createRefund(completedRequest, orderEntry, refundNotes,
				expectedQuantity,
				action, reason);

		completedRequest.setStatus(ReturnStatus.COMPLETED);
		completedRefund.setStatus(ReturnStatus.COMPLETED);
		modelService.save(completedRequest);
		modelService.save(completedRefund);

		//When
		//Can't allow return more than (ordered - completed return)
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithOneEntry);
		returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action, reason);
	}

	/**
	 * Given an order with 1 entry:<br>
	 * entry 1 : {quantity: 3, product: product1}<br>
	 * with an existing cancelled return<br>
	 * entry 1 : {quantity: 2, product: product1}<br>
	 * Create another return that would make the total quantity too high<br>
	 * entry 1 : {quantity: 2, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * Cancelled return is ignored: return is created with 1 refund entry<br>
	 * <p>
	 * Assert:<br>
	 * Return is a valid BORIS return<br>
	 */
	@Test()
	public void shouldSucceed_cancelledReturnExists_expectedQuantityTooHigh()
	{
		//Given
		final OrderModel orderWithOneEntry = shipOrderWithOneEntry();
		final AbstractOrderEntryModel orderEntry = orderWithOneEntry.getEntries().get(0);
		final Long expectedQuantity = 2L;

		final ReturnRequestModel cancelledRequest = returnService.createReturnRequest(orderWithOneEntry);
		final RefundEntryModel cancelledRefund = returnService.createRefund(cancelledRequest, orderEntry, refundNotes,
				expectedQuantity,
				action, reason);

		cancelledRequest.setStatus(ReturnStatus.CANCELED);
		cancelledRefund.setStatus(ReturnStatus.CANCELED);
		modelService.save(cancelledRequest);
		modelService.save(cancelledRefund);

		//when
		final ReturnRequestModel request = returnService.createReturnRequest(orderWithOneEntry);
		final RefundEntryModel refund = returnService.createRefund(request, orderEntry, refundNotes, expectedQuantity, action,
				reason);

		// Then
		assertEquals(orderWithOneEntry, request.getOrder());
		assertEquals(request, refund.getReturnRequest());
		assertEquals(orderEntry, refund.getOrderEntry());
		assertEquals(refundNotes, refund.getNotes());
		assertEquals(expectedQuantity, refund.getExpectedQuantity());
		assertEquals(action, refund.getAction());
		assertEquals(reason, refund.getReason());

		//For 5.7 only
		validateBorisReturn(request, refund);
	}

	/**
	 * For 5.7 only. Checks if the created return is for BORIS case
	 */
	private void validateBorisReturn(final ReturnRequestModel returnRequest, final RefundEntryModel refundEntry)
	{
		assertEquals(ReturnStatus.RECEIVED, returnRequest.getStatus());
		assertEquals(ReturnStatus.RECEIVED, refundEntry.getStatus());
		assertEquals(refundEntry.getExpectedQuantity(), refundEntry.getReceivedQuantity());
	}

	/**
	 * Create and ship an order with 1 order entry in 1 consignment
	 */
	private OrderModel shipOrderWithOneEntry()
	{
		productInfo.put(PRODUCT_1, 3L);

		final OrderModel order = OrderBuilder.aSourcingOrder().withBaseStore(baseStore)
				.build(buildAddress(), currency, productService, productInfo);
		final AbstractOrderEntryModel orderEntry = order.getEntries().get(0);

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE,
				sourcingService.sourceOrder(order));

		consignmentResult.stream().forEach(consignment -> {
			final Collection<ShippedEntry> shippedEntries = shippingService.createShippedEntries(consignment);
			try
			{
				shippingService.confirmShippedConsignmentEntries(shippedEntries);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		});

		//Refresh consignment entry to update quantityShipped
		final ConsignmentEntryModel consignmentEntry = orderEntry.getConsignmentEntries().iterator().next();
		modelService.refresh(consignmentEntry);

		return order;
	}

	/**
	 * Create an order with 1 order entry, but do not ship it.
	 */
	private OrderModel createOrderWithOneEntry()
	{
		productInfo.put(PRODUCT_1, 3L);

		final OrderModel order = OrderBuilder.aSourcingOrder().withBaseStore(baseStore)
				.build(buildAddress(), currency, productService, productInfo);
		final AbstractOrderEntryModel orderEntry = order.getEntries().get(0);

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE,
				sourcingService.sourceOrder(order));

		//Refresh consignment entry to update quantityShipped
		final ConsignmentEntryModel consignmentEntry = orderEntry.getConsignmentEntries().iterator().next();
		modelService.refresh(consignmentEntry);

		return order;
	}

	/**
	 * Create and ship an order with 1 order entry split across many consignments (should be 2 consignments: quantity 9
	 * and quantity 6)
	 */
	private OrderModel shipOrderWithManyConsignments()
	{
		productInfo.put(PRODUCT_1, 15L);

		final OrderModel order = OrderBuilder.aSourcingOrder().withBaseStore(baseStore)
				.build(buildAddress(), currency, productService, productInfo);
		final AbstractOrderEntryModel orderEntry = order.getEntries().get(0);

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE,
				sourcingService.sourceOrder(order));

		consignmentResult.stream().forEach(consignment -> {
			final Collection<ShippedEntry> shippedEntries = shippingService.createShippedEntries(consignment);
			try
			{
				shippingService.confirmShippedConsignmentEntries(shippedEntries);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		});

		//Refresh consignment entries to update quantityShipped
		orderEntry.getConsignmentEntries().stream().forEach(entry -> modelService.refresh(entry));

		return order;
	}

	/**
	 * Create an order with 1 order entry split across many consignments (should be 3 consignments: quantity 6, 2 and 7)
	 * but only ship the consignment with 7 items.
	 *
	 * @throws ShippingException
	 */
	private OrderModel partiallyShipOrderWithManyConsignments() throws ShippingException
	{
		productInfo.put(PRODUCT_1, 15L);

		final OrderModel order = OrderBuilder.aSourcingOrder().withBaseStore(baseStore)
				.build(buildAddress(), currency, productService, productInfo);
		final AbstractOrderEntryModel orderEntry = order.getEntries().get(0);

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE,
				sourcingService.sourceOrder(order));

		final ConsignmentModel biggerConsignment = consignmentResult.stream()
				.filter(consignment -> consignment.getConsignmentEntries().iterator().next().getQuantity() == 7L).findFirst().get();

		final Collection<ShippedEntry> shippedEntries = shippingService.createShippedEntries(biggerConsignment);
		shippingService.confirmShippedConsignmentEntries(shippedEntries);

		//Refresh consignment entries to update quantityShipped
		orderEntry.getConsignmentEntries().stream().forEach(entry -> modelService.refresh(entry));

		return order;
	}

	/**
	 * Create and ship an order with 2 order entries. Note that the order entries are not necessarily in the same order
	 * as when they are put in productInfo
	 */
	private OrderModel shipOrderWithManyEntries()
	{
		productInfo.put(PRODUCT_1, 3L);
		productInfo.put(PRODUCT_2, 5L);

		final OrderModel order = OrderBuilder.aSourcingOrder().withBaseStore(baseStore)
				.build(buildAddress(), currency, productService, productInfo);

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE,
				sourcingService.sourceOrder(order));

		consignmentResult.stream().forEach(consignment -> {
			final Collection<ShippedEntry> shippedEntries = shippingService.createShippedEntries(consignment);
			try
			{
				shippingService.confirmShippedConsignmentEntries(shippedEntries);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		});

		//Refresh consignment entries to update quantityShipped
		order.getEntries()
				.stream()
				.forEach(
						orderEntry -> orderEntry.getConsignmentEntries().stream()
								.forEach(consignmentEntry -> modelService.refresh(consignmentEntry)));

		return order;
	}

	/**
	 * Create and confirm a pickup order with one entry.
	 */
	private OrderModel confirmPickupOrderWithOneEntry()
	{
		productInfo.put(PRODUCT_1, 3L);

		final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(SHINBASHI);

		final DeliveryModeModel deliveryModeModel = new DeliveryModeModel();
		deliveryModeModel.setCode(PICKUP_CODE);
		modelService.save(deliveryModeModel);

		final OrderModel order = OrderBuilder.aSourcingOrder().withBaseStore(baseStore)
				.build(buildAddress(), currency, productService, productInfo, pos,
				deliveryModeModel);
		final AbstractOrderEntryModel orderEntry = order.getEntries().get(0);

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE,
				sourcingService.sourceOrder(order));

		consignmentResult.stream().forEach(consignment -> {
			final Collection<ShippedEntry> shippedEntries = shippingService.createShippedEntries(consignment);
			consignment.getDeliveryMode();
			try
			{
				shippingService.confirmShippedConsignmentEntries(shippedEntries);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		});

		//Refresh consignment entry to update quantityShipped
		final ConsignmentEntryModel consignmentEntry = orderEntry.getConsignmentEntries().iterator().next();
		modelService.refresh(consignmentEntry);

		return order;
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
