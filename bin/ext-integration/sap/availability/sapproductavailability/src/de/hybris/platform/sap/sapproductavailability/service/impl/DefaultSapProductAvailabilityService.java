/**
 *
 */
package de.hybris.platform.sap.sapproductavailability.service.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.impl.DefaultCommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.sap.core.module.ModuleConfigurationAccess;
import de.hybris.platform.sap.sapproductavailability.businessobject.SapProductAvailability;
import de.hybris.platform.sap.sapproductavailability.constants.SapproductavailabilityConstants;
import de.hybris.platform.sap.sapproductavailability.service.SapProductAvailabilityBOFactory;
import de.hybris.platform.sap.sapproductavailability.service.SapProductAvailabilityService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;


/**
 * @author Administrator
 * 
 */
public class DefaultSapProductAvailabilityService extends DefaultCommerceStockService implements SapProductAvailabilityService
{
	private SapProductAvailabilityBOFactory sapProductAvailabilityBOFactory;

	private ModuleConfigurationAccess moduleConfigurationAccess;

	private static final Logger LOG = Logger.getLogger(DefaultSapProductAvailabilityService.class);

	private UserService userService;


	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the sapProductAvailabilityBolFactory
	 */
	public SapProductAvailabilityBOFactory getSapProductAvailabilityBOFactory()
	{
		return sapProductAvailabilityBOFactory;
	}

	/**
	 * @param sapProductAvailabilityBOFactory
	 *           the sapProductAvailabilityBOFactory to set
	 */
	@Required
	public void setSapProductAvailabilityBOFactory(final SapProductAvailabilityBOFactory sapProductAvailabilityBOFactory)
	{
		this.sapProductAvailabilityBOFactory = sapProductAvailabilityBOFactory;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.sapproductavailability.service.SapProductAvailabilityService#isSynchronousATPCheckActive()
	 */
	@Override
	public boolean isSynchronousATPCheckActive()
	{
		if (getModuleConfigurationAccess().isSAPConfigurationActive()) {
			if (getModuleConfigurationAccess().getProperty(
					SapproductavailabilityConstants.ATPACTIVE) != null) {
				return Boolean.valueOf(
						getModuleConfigurationAccess().getProperty(
								SapproductavailabilityConstants.ATPACTIVE)
								.toString()).booleanValue();
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.stock.impl.DefaultCommerceStockService#getStockLevelForProductAndBaseStore
	 * (de.hybris.platform.core.model.product.ProductModel, de.hybris.platform.store.BaseStoreModel)
	 */
	@Override
	public Long getStockLevelForProductAndBaseStore(final ProductModel product, final BaseStoreModel baseStore)
	{
		ServicesUtil.validateParameterNotNull(product, "product cannot be null");
		ServicesUtil.validateParameterNotNull(baseStore, "baseStore cannot be null");

		if (isSynchronousATPCheckActive())
		{
			// not available stock return 0
			final SapProductAvailability productAvailability = readSapProductAvailability(product);

			if (productAvailability == null)
			{
				return Long.valueOf(0); // no stock available
			}

			return productAvailability.getCurrentStockLevel();


		}
		else
		{
			return convertStockUnit(product, baseStore);
		}
	}

	private String getCustomerId()
	{
		if (getUserService().isAnonymousUser(getUserService().getCurrentUser()))
		{
			return null;
		}
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();

		final String customerId = currentCustomer != null ? currentCustomer.getCustomerID() : null;

		return customerId;

	}

	protected SapProductAvailability readSapProductAvailability(final ProductModel product)

	{
		final String plant = determinePlant(product);

		if (StringUtils.isEmpty(plant))
		{
			return null;
		}

		return getSapProductAvailabilityBOFactory().getSapProductAvailabilityBO().readProductAvailability(product, getCustomerId(),
				plant, Long.valueOf(0));

	}

	private String determinePlantForCustomer(final ProductModel product, final String customerId)
	{
		String plant = null;

		if (!StringUtils.isEmpty(customerId) && !StringUtils.isEmpty(product.getCode()))
		{
			plant = getSapProductAvailabilityBOFactory().getSapProductAvailabilityBO().readPlantForCustomerMaterial(
					product.getCode(), customerId);
		}

		if (StringUtils.isEmpty(plant))
		{
			plant = determinePlantForMaterial(product);

		}
		return plant;

	}

	private String determinePlantForMaterial(final ProductModel product)
	{

		String plant = null;

		if (product.getSapPlant() != null && product.getSapPlant().getCode() != null)
		{
			plant = product.getSapPlant().getCode();

		}

		if (StringUtils.isEmpty(plant))
		{
			plant = getSapProductAvailabilityBOFactory().getSapProductAvailabilityBO().readPlantForMaterial(product.getCode());
		}

		return plant;

	}

	private String determinePlant(final ProductModel product)
	{

		String plant = null;

		if (!StringUtils.isEmpty(getCustomerId()))
		{
			plant = determinePlantForCustomer(product, getCustomerId());

		}
		else
		{
			plant = determinePlantForMaterial(product);
		}

		return plant;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.stock.impl.DefaultCommerceStockService#getStockLevelStatusForProductAndBaseStore
	 * (de.hybris.platform.core.model.product.ProductModel, de.hybris.platform.store.BaseStoreModel)
	 */
	@Override
	public StockLevelStatus getStockLevelStatusForProductAndBaseStore(final ProductModel product, final BaseStoreModel baseStore)
	{
		if (isSynchronousATPCheckActive())
		{

			final Long stockLevel = getStockLevelForProductAndBaseStore(product, baseStore);

			if (stockLevel.compareTo(Long.valueOf(0)) == 1)
			{
				return StockLevelStatus.INSTOCK;
			}
			else
			{
				return StockLevelStatus.OUTOFSTOCK;
			}

		}
		else
		{
			return super.getStockLevelStatusForProductAndBaseStore(product, baseStore);
		}
	}

	/**
	 * @param product
	 * @param baseStore
	 * @return Long the stock level in sales unit
	 */
	protected Long convertStockUnit(final ProductModel product, final BaseStoreModel baseStore)
	{
		// Get the stock in base unit
		final Long stockInBaseUnit = super.getStockLevelForProductAndBaseStore(product, baseStore);
		
	 	
		// Get the unit conversion factor to convert from the base unit to the sales unit
		final Double factor = product.getSapBaseUnitConversion();

		//stockInBaseUnit is null if the product is flagged FORCEINSTOCK
		if (factor == null || factor.equals(Double.valueOf(0)) || stockInBaseUnit == null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Stock unint conversion factor is null, return the stock unit as is!");
			}

			return stockInBaseUnit;
		}
		else
		{
			 
			// Convert the base unit to the sales unit
			return Long.valueOf((long) Math.floor((stockInBaseUnit.longValue() / factor.doubleValue())));
			
			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.sapproductavailability.service.SapProductAvailabilityService#getFutureAvailability(de.hybris
	 * .platform.core.model.product.ProductModel)
	 */
	@Override
	public Map<String, Map<Date, Integer>> readProductFutureAvailability(final ProductModel productModel)
	{
		final SapProductAvailability availability = readSapProductAvailability(productModel);

		if (availability != null)
		{
			return convertQuantityOfFutureAvailability((availability).getFutureAvailability());
		}
		return Collections.emptyMap();
	}

	/**
	 * The future available quantity in ERP is of type Long and it has to be convert to Integer to match hybris expected
	 * type.The maximum quantity that is displayed in hybris is Integer.MAX_VALUE which is 2147483647 .
	 * 
	 * @param futureAvailabilityMap
	 * @return Map<String, Map<Date, Integer>> futureAvailability
	 */
	protected Map<String, Map<Date, Integer>> convertQuantityOfFutureAvailability(
			final Map<String, Map<Date, Long>> futureAvailabilityMap)
	{

		final Map<String, Map<Date, Integer>> productDateQuantityMap = new HashMap<>();
		final HashMap<Date, Integer> dateQuantityMap = new HashMap<>();

		final Iterator it = futureAvailabilityMap.entrySet().iterator();

		while (it.hasNext())
		{
			final Entry productDateQuantity = (Entry) it.next();

			final String product = productDateQuantity.getKey().toString();
			final Map<Date, Long> dateQuantity = (Map<Date, Long>) productDateQuantity.getValue();

			for (final Entry<Date, Long> dateQuantityEntry : dateQuantity.entrySet())
			{
				// Convert the quantity type from Long to Integer to match the FutureStockService.getFutureAvailability(...) API
				final Integer quantity = dateQuantityEntry.getValue().compareTo(Long.valueOf(Integer.MAX_VALUE)) == -1 ? Integer
						.valueOf(dateQuantityEntry.getValue().intValue()) : Integer.valueOf(Integer.MAX_VALUE);

				dateQuantityMap.put(dateQuantityEntry.getKey(), quantity);
			}

			productDateQuantityMap.put(product, dateQuantityMap);
			it.remove();
		}

		return productDateQuantityMap;

	}

	public ModuleConfigurationAccess getModuleConfigurationAccess()
	{
		return moduleConfigurationAccess;
	}

	@Required
	public void setModuleConfigurationAccess(final ModuleConfigurationAccess moduleConfigurationAccess)
	{
		this.moduleConfigurationAccess = moduleConfigurationAccess;
	}

}
