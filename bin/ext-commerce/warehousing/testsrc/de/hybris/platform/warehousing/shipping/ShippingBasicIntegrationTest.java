package de.hybris.platform.warehousing.shipping;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
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
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.cancellation.OrderCancellationService;
import de.hybris.platform.warehousing.constants.WarehousingTestConstants;
import de.hybris.platform.warehousing.data.shipping.ShippedEntry;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.util.OrderBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ShippingBasicIntegrationTest extends ServicelayerTransactionalTest
{

    private final static String PRODUCT_1 = "product1";
    private final static String PRODUCT_2 = "product2";
    private final static String CONSIGNMENT_CODE = "con";

    public Map<String, Long> productInfo;
    public Map<AbstractOrderEntryModel, Long> cancellationEntryInfo;

    @Resource
    private ProductService productService;
    @Resource
	private BaseStoreService baseStoreService;
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
    private OrderCancellationService orderCancellationService;
    @Resource
    private ShippingService shippingService;

    private CurrencyModel currency;
	private BaseStoreModel baseStore;


    @Before
    public void setup() throws IOException, ImpExException {

        importCsv(
                "/warehousing/test/impex/sourcingIntegration-test-data-default.impex",
                WarehousingTestConstants.ENCODING);
        importCsv(
                "/warehousing/test/impex/consignmentIntegration-test-data-simple.impex",
                WarehousingTestConstants.ENCODING);

        try
        {
            currency = commonI18NService.getCurrency("USD");
        } catch (final UnknownIdentifierException e)
        {
            currency = new CurrencyModel();
            currency.setIsocode("USD");
            currency.setDigits(Integer.valueOf(2));
            modelService.save(currency);
        }

        cancellationEntryInfo = new HashMap<>();
        productInfo = new HashMap<>();
		baseStore = baseStoreService.getAllBaseStores().iterator().next();
    }

    /**
     * Given an order with 1 entries, and create shipment<br>
     * entry 1 : {quantity: 3, product: product1}<br>
     * <p>
     * Result:<br>
     * shipment should be created<br>
     * <p>
     * Assert:<br>
     * It verifies the shipment result<br>
     */
    @Test
    public void createShippedEntrySuccess()
    {
        //Given
        productInfo.put(PRODUCT_1, new Long(3));

		final OrderModel order = OrderBuilder.aSourcingOrder().withBaseStore(baseStore)
				.build(buildAddress(), currency, productService, productInfo);
        final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE, sourcingService.sourceOrder(order));
        final Collection<ShippedEntry> shippedResult = new ArrayList<>();

        //when
        consignmentResult.stream().forEach(result -> shippedResult.addAll(shippingService.createShippedEntries(result)));

        // Then
        assertTrue(shippedResult.size() == 1);
        assertTrue(shippedResult.stream().allMatch(result -> result.getQuantity().equals(result.getConsignmentEntry().getQuantity())));
    }


    /**
	 * Given an order with 1 entries, and create shipment<br>
	 * entry 1 : {quantity: 16, product: product1}<br>
	 * <p>
	 * Result:<br>
	 * 3 shipment should be created since there it sourced from 3 warehouses<br>
	 * <p>
	 * Assert:<br>
	 * It verifies the shipment result<br>
	 */
    @Test
    public void createMultiShippedEntrySuccess() {

        //Given
        productInfo.put(PRODUCT_1, new Long(16));

		final OrderModel order = OrderBuilder.aSourcingOrder().withBaseStore(baseStore)
				.build(buildAddress(), currency, productService, productInfo);
        final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE, sourcingService.sourceOrder(order));
        final Collection<ShippedEntry> shippedResult = new ArrayList<>();

        //when
        consignmentResult.stream().forEach(result -> shippedResult.addAll(shippingService.createShippedEntries(result)));

        // Then
		assertTrue(shippedResult.size() == 3);
        assertTrue(shippedResult.stream().allMatch(result -> result.getQuantity().equals(result.getConsignmentEntry().getQuantity())));
    }

    /**
     * Given an order with 2 entries, then create shipment<br>
     * entry 1 : {quantity: 3, product: product1}<br>
     * entry 2 : {quantity: 2, product: product2}<br>
     * <p>
     * Result:<br>
     * 1 shipment should be created with both product<br>
     * <p>
     * Assert:<br>
     * It verifies the shipment result<br>
     */
    @Test
    public void createShippedEntryWith1Consignment2Products() {

        //Given
        productInfo.put(PRODUCT_1, new Long(3));
        productInfo.put(PRODUCT_2, new Long(2));

		final OrderModel order = OrderBuilder.aSourcingOrder().withBaseStore(baseStore)
				.build(buildAddress(), currency, productService, productInfo);
        final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, CONSIGNMENT_CODE, sourcingService.sourceOrder(order));
        getModelService().saveAll(consignmentResult);
        final Collection<ShippedEntry> shippedResult = new ArrayList<>();

        //when
        consignmentResult.stream().forEach(result -> shippingService.createShippedEntries(result));
        consignmentResult.stream().forEach(result -> shippedResult.addAll(shippingService.createShippedEntries(result)));

        // Then
        assertTrue(shippedResult.size() == 2);
        assertTrue(shippedResult.stream().allMatch(result -> result.getQuantity().equals(result.getConsignmentEntry().getQuantity())));
    }


    private AddressModel buildAddress() {

        final AddressModel address = new AddressModel();
        final Country country = jaloSession.getC2LManager().getCountryByIsoCode("US");
        address.setCountry((CountryModel) modelService.get(country));
        final UserModel user = userService.getCurrentUser();
        address.setOwner(user);
        return address;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }

}

