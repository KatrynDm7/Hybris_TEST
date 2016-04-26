package de.hybris.platform.sap.sapproductavailability.backend.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.core.bol.cache.CacheAccess;
import de.hybris.platform.sap.core.bol.cache.exceptions.SAPHybrisCacheException;
import de.hybris.platform.sap.core.bol.logging.Log4JWrapper;
import de.hybris.platform.sap.core.bol.logging.LogCategories;
import de.hybris.platform.sap.core.bol.logging.LogSeverity;
import de.hybris.platform.sap.core.module.ModuleConfigurationAccess;
import de.hybris.platform.sap.sapproductavailability.businessobject.SapProductAvailability;
import de.hybris.platform.sap.sapproductavailability.constants.SapproductavailabilityConstants;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;


/**
 * Cache helper adds/retrieves objects from cache
 */
public class SapProductAvailabilityCache
{

	static final private Log4JWrapper sapLogger = Log4JWrapper.getInstance(SapProductAvailabilityCache.class.getName());

	@Resource(name = "sapProductAvailabilityConfigurationAccess")
	private ModuleConfigurationAccess configAccess;

	@Resource(name = SapproductavailabilityConstants.BEAN_ID_CACHE_PLANT)
	protected CacheAccess plantCacheAccess;

	@Resource(name = SapproductavailabilityConstants.BEAN_ID_CACHE_PLANT_MATERIAL)
	protected CacheAccess plantMaterialCacheAccess;

	@Resource(name = SapproductavailabilityConstants.BEAN_ID_CACHE_PLANT_CUSTOMER)
	protected CacheAccess plantCustomerCacheAccess;

	@Resource(name = SapproductavailabilityConstants.BEAN_ID_CACHE_AVAILABILITY)
	protected CacheAccess atpCacheAccess;

	/**
	 * read SapProductAvailability from cache
	 * 
	 * @param product
	 * @param customerId
	 * @param plant
	 * @param quantity
	 * @return SapProductAvailability
	 */
	public SapProductAvailability readCachedProductAvailability(final ProductModel product, final String customerId,
			final String plant, final Long quantity)
	{
		sapLogger.entering("readCachedProductAvailability(...)");
		return (SapProductAvailability) atpCacheAccess.get(createCacheKey(product, customerId, plant, quantity));
	}


	/**
	 * add SapProductAvailability to cache, in case of failure log error
	 * 
	 * @param availability
	 * @param product
	 * @param customerId
	 * @param plant
	 * @param quantity
	 */
	public void cacheProductAvailability(final SapProductAvailability availability, final ProductModel product,
			final String customerId, final String plant, final Long quantity)
	{
		sapLogger.entering("cacheProductAvailability(...)");

		try
		{
			atpCacheAccess.put(createCacheKey(product, customerId, plant, quantity), availability);
		}
		catch (final SAPHybrisCacheException e)
		{
			sapLogger
					.log(LogSeverity.ERROR,
							LogCategories.APPLICATIONS,
							String.format(
									"Error while adding SapProductAvailability to cache for Material: %s, Customer: %s, Plant:%s, Unit: %s, Quantity : %s.",
									product.getCode(), customerId, product.getUnit().getCode(), quantity.toString()));

		}
		finally
		{
			sapLogger.exiting();
		}

	}

	/**
	 * add plant to cache
	 * 
	 * @param plant
	 * @param material
	 * @param customerId
	 * @param salesOrg
	 * @param disChannel
	 */
	public void cachePlant(final String plant, final String material, final String customerId, final String salesOrg,
			final String disChannel)
	{
		sapLogger.entering("cacheProductAvailability(...)");

		try
		{
			atpCacheAccess.put(createCacheKey(material, customerId, salesOrg, disChannel), plant);
		}
		catch (final SAPHybrisCacheException e)
		{
			sapLogger.log(LogSeverity.ERROR, LogCategories.APPLICATIONS, String.format(
					"Error while adding Plant to cache for Material: %s, Customer: %s, salesOrg:%s, DisChannel: %s.", material,
					customerId, salesOrg, disChannel));

		}
		finally
		{
			sapLogger.exiting();
		}

	}


	/**
	 * read cached plant
	 * 
	 * @param material
	 * @param customerId
	 * @param salesOrg
	 * @param disChannel
	 * @return String Plant
	 */
	public String readCachedPlant(final String material, final String customerId, final String salesOrg, final String disChannel)
	{
		sapLogger.entering("readCachedPlant(...)");

		return (String) plantCacheAccess.get(createCacheKey(material, customerId, salesOrg, disChannel));

	}

	protected String createCacheKey(final String material, final String customerId, final String salesOrg, final String disChannel)
	{
		final StringBuilder plantCacheKey = new StringBuilder();

		plantCacheKey.append(SapproductavailabilityConstants.CACHEKEY_SAP_ATP);
		plantCacheKey.append(material);
		plantCacheKey.append(StringUtils.isEmpty(customerId) ? "CUSTNULL" : customerId);
		plantCacheKey.append(salesOrg);
		plantCacheKey.append(disChannel);

		return plantCacheKey.toString();
	}

	/**
	 * Cache plant material
	 * 
	 * @param plant
	 * @param material
	 * @param salesOrg
	 * @param disChannel
	 */
	public void cachePlantMaterial(final String plant, final String material, final String salesOrg, final String disChannel)
	{
		sapLogger.entering("cachePlantMaterial(...)");

		try
		{
			plantMaterialCacheAccess.put(createCacheKey(material, null, salesOrg, disChannel), plant);
		}
		catch (final SAPHybrisCacheException e)
		{
			sapLogger.log(LogSeverity.ERROR, LogCategories.APPLICATIONS, String
					.format("Error while adding Plant to cache for Material: %s, salesOrg:%s, DisChannel: %s.", material, salesOrg,
							disChannel));

		}
		finally
		{
			sapLogger.exiting();
		}

	}

	/**
	 * read cached material plant
	 * 
	 * @param material
	 * @param salesOrg
	 * @param disChannel
	 * @return String plant
	 */
	public String readCachedPlantMaterial(final String material, final String salesOrg, final String disChannel)
	{
		sapLogger.entering("readCachedPlantMaterial(...)");
		return (String) plantMaterialCacheAccess.get(createCacheKey(material, null, salesOrg, disChannel));
	}

	/**
	 * cache plant customer
	 * 
	 * @param plant
	 * @param material
	 * @param customerId
	 * @param salesOrg
	 * @param disChannel
	 */
	public void cachePlantCustomer(final String plant, final String material, final String customerId, final String salesOrg,
			final String disChannel)
	{
		sapLogger.entering("cachePlantMaterial(...)");

		try
		{
			plantCustomerCacheAccess.put(createCacheKey(material, customerId, salesOrg, disChannel), plant);
		}
		catch (final SAPHybrisCacheException e)
		{
			sapLogger.log(LogSeverity.ERROR, LogCategories.APPLICATIONS, String.format(
					"Error while adding Plant to cache for Material: %s, Customer: %s, salesOrg:%s, DisChannel: %s.", material,
					customerId, salesOrg, disChannel));

		}
		finally
		{
			sapLogger.exiting();
		}

	}

	/**
	 * read cached plant for customer
	 * 
	 * @param material
	 * @param customerId
	 * @param salesOrg
	 * @param disChannel
	 * @return cached Plant
	 */
	public String readCachedPlantCustomer(final String material, final String customerId, final String salesOrg,
			final String disChannel)
	{
		sapLogger.entering("readCachedPlantMaterial(...)");

		return (String) plantCustomerCacheAccess.get(createCacheKey(material, customerId, salesOrg, disChannel));
	}

	protected String createCacheKey(final ProductModel product, final String customerId, final String plant, final Long quantity)
	{
		final StringBuilder plantCacheKey = new StringBuilder();

		plantCacheKey.append(SapproductavailabilityConstants.CACHEKEY_SAP_ATP);

		plantCacheKey.append(product.getCode());
		plantCacheKey.append(product.getUnit().getSapCode().isEmpty() ? "UNITNULL" : product.getUnit().getSapCode());

		plantCacheKey.append(StringUtils.isEmpty(customerId) ? "CUSTNULL" : customerId);
		plantCacheKey.append(plant);
		plantCacheKey.append(quantity.longValue());

		return plantCacheKey.toString();
	}












}
