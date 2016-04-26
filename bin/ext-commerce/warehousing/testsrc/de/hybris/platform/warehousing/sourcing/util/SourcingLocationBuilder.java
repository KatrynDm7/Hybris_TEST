package de.hybris.platform.warehousing.sourcing.util;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.util.Builder;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * This is a Sourcing location builder implementation of the Builder interface
 *
 */
public class SourcingLocationBuilder  implements Builder <SourcingLocation>
{
	public static final Double DEFAULT_DISTANCE = 100.0;
	public static final Integer DEFAULT_PRIORITY = 1;
	public static final Integer DEFAULT_INITIAL_ID = 0;
	public static final String DEFAULT_SKU = "SKU1";
	public static final Long DEFAULT_SKU_QUANTITY = 10l;

	static private Integer uniqueId = DEFAULT_INITIAL_ID;
	private Double distance = DEFAULT_DISTANCE;
	private Integer priority = DEFAULT_PRIORITY;
	private SourcingContext context;
	private Map<ProductModel, Long> availability;
	private WarehouseModel warehouse;
	private ProductModel productModel;

	public SourcingLocationBuilder()
	{
		super();
		this.availability = new HashMap<>();
	}

	public static SourcingLocationBuilder aSourcingLocation()
	{
		return new SourcingLocationBuilder();
	}

	@Override
	public SourcingLocation build()
	{
		if (this.availability.isEmpty())
		{
			withCustomAvailabilitySkuQuantity(DEFAULT_SKU, DEFAULT_SKU_QUANTITY);
		}
		if (this.warehouse.getCode().equals(""))
		{
			withDefaultWarehouse();
		}
		final SourcingLocation sourcingLoc = new SourcingLocation();
		sourcingLoc.setContext(this.context);
		sourcingLoc.setDistance(this.distance);
		sourcingLoc.setWarehouse(this.warehouse);
		sourcingLoc.setAvailability(this.availability);
		sourcingLoc.setPriority(this.priority);
		return sourcingLoc;
	}

	public SourcingLocationBuilder withWarehouse(final WarehouseModel warehouse)
	{
		this.warehouse = warehouse;
		return this;
	}

	public SourcingLocationBuilder withDistance(final Double distance)
	{
		this.distance = distance;
		return this;
	}

	public SourcingLocationBuilder withContext(final SourcingContext context)
	{
		this.context = context;
		return this;
	}

	/**
	 * Creates a new {@link WarehouseModel} from the given id
	 *
	 * @param id
	 *           - the String id of the warehouse to be created
	 * @return {@link SourcingLocationBuilder}
	 */
	public SourcingLocationBuilder withWarehouseCode(final String id)
	{
		this.warehouse = new WarehouseModel();
		this.warehouse.setCode(id);
		return this;
	}

	public SourcingLocationBuilder withAvailability(final Map<ProductModel, Long> availability)
	{
		this.availability = availability;
		return this;
	}

	public SourcingLocationBuilder withPriority(final Integer priority)
	{
		this.priority = priority;
		return this;
	}

	/**
	 * Adds a new Product of the default SKU and the specified quantity to the location to be built
	 *
	 * @param quantity
	 *           - the quantity of the the product
	 * @return SourcingLocationBuilder
	 */
	public SourcingLocationBuilder withDefaultAvailabilityQuantity(final Long quantity)
	{
		withCustomAvailabilitySkuQuantity(DEFAULT_SKU, quantity);
		return this;
	}

	/**
	 * Adds a new {@link ProductModel} of the specified SKU and quantity to the location to be built
	 *
	 * @param SKUId
	 *           - SKU ID to be used as the product code
	 * @param quantity
	 *           - the quantity of the the product
	 * @return SourcingLocationBuilder
	 */
	public SourcingLocationBuilder withCustomAvailabilitySkuQuantity(final String SKUId, final Long quantity)
	{

		productModel = new ProductModel();
		productModel.setCode(SKUId);
		availability.put(productModel, quantity);
		return this;
	}

	/**
	 * Adds an existing {@link ProductModel} of the specified quantity to the location to be built
	 *
	 * @param productModel
	 * @param quantity
	 * @return
	 */
	public SourcingLocationBuilder withCustomAvailabilityProductQuantity(final ProductModel productModel, final Long quantity)
	{
		availability.put(productModel, quantity);
		return this;
	}

	private SourcingLocationBuilder withDefaultWarehouse()
	{
		this.warehouse = new WarehouseModel();
		this.warehouse.setCode(generateId());
		return this;

	}

	private String generateId() {
		uniqueId++;
		return "loc"+uniqueId;
	}

}