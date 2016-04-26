/**
 * 
 */
package de.hybris.platform.sap.sapproductavailability.backend.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.core.bol.backend.BackendType;
import de.hybris.platform.sap.core.bol.backend.jco.BackendBusinessObjectBaseJCo;
import de.hybris.platform.sap.core.bol.logging.Log4JWrapper;
import de.hybris.platform.sap.core.bol.logging.LogCategories;
import de.hybris.platform.sap.core.bol.logging.LogSeverity;
import de.hybris.platform.sap.core.jco.connection.JCoConnection;
import de.hybris.platform.sap.core.jco.connection.JCoManagedConnectionFactory;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.core.module.ModuleConfigurationAccess;
import de.hybris.platform.sap.sapproductavailability.backend.SapProductAvailabilityBackend;
import de.hybris.platform.sap.sapproductavailability.businessobject.SapProductAvailability;
import de.hybris.platform.sap.sapproductavailability.businessobject.impl.SapProductAvailabilityImpl;
import de.hybris.platform.sap.sapproductavailability.constants.SapproductavailabilityConstants;
import de.hybris.platform.sap.sapproductavailability.converter.ConversionTools;
import de.hybris.platform.sap.sapproductavailability.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;


/**
 * @author Administrator
 * 
 */
@BackendType("ERP")
public class SapProductAvailabilityBackendERP extends BackendBusinessObjectBaseJCo implements SapProductAvailabilityBackend
{

	static final private Log4JWrapper sapLogger = Log4JWrapper.getInstance(SapProductAvailabilityBackendERP.class.getName());

	private static final int PRODUCT_CODE_LENGTH = 18;

	private static final int CUSTOMER_ID_LENGTH = 10;

	private static final String EMPTY_PLANT = "0000";

	/**
	 * Constant for the RFC enabled FM name used to perform product availability check in the ERP system.
	 */
	static protected String BAPI_MATERIAL_AVAILABILITY = "BAPI_MATERIAL_AVAILABILITY";

	static protected String BAPI_MATERIAL_PLANT = "BAPI_MVKE_ARRAY_READ";
	/**
	 * Constant for the RFC enabled FM name used to retrieve the plant from the customer material.
	 */
	static protected String BAPI_CUSTMATINFO_GETDETAILM = "BAPI_CUSTMATINFO_GETDETAILM";

	/**
	 * Constant for the RFC enabled FM name used to verify whether the material is available in the given plant
	 */
	static protected String BAPI_MATERIAL_GET_DETAIL = "BAPI_MATERIAL_GET_DETAIL";

	/**
	 * Constant for the RFC enabled FM name used to retrieve the plant from the customer.
	 */
	static protected String BAPI_CUSTOMER_GETDETAIL1 = "BAPI_CUSTOMER_GETDETAIL1";

	/**
	 * Constant for successful return of the RFC enabled FMs.
	 */
	static protected String BAPI_RETURN_SUCCESS = "S";


	@Resource(name = "sapCoreJCoManagedConnectionFactory")
	private JCoManagedConnectionFactory managedConnectionFactory;

	@Resource(name = "sapProductAvailabilityConfigurationAccess")
	private ModuleConfigurationAccess configAccess;

	@Resource(name = "sapProductAvailabilityCache")
	private SapProductAvailabilityCache cachedBackend;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.sapproductavailability.backend.SapProductAvailabilityBackend#readProductAvailability(de
	 * .hybris.platform.core.model.product.ProductModel, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public SapProductAvailability readProductAvailability(final ProductModel product, final String customerId, final String plant,
			final Long requestedQuantity) throws BackendException
	{

		sapLogger.entering("readProductAvailability(...)");

		validateParameterNotNull(plant, "Product is empty for availability check!");
		validateParameterNotNull(product, "Product is empty for availability check!");
		validateParameterNotNull(product.getUnit(), "Product unit is empty for availability check!");
		validateParameterNotNull(product.getUnit().getSapCode(), "Product SAP unit is empty for availability check!");

		SapProductAvailability availability = cachedBackend.readCachedProductAvailability(product, customerId, plant,
				requestedQuantity);

		if (availability != null && availability.getFutureAvailability() != null && !availability.getFutureAvailability().isEmpty())
		{
			// Return the cached object
			return availability;
		}

		// Default check availability to today
		final Date requestedDate = Calendar.getInstance().getTime();

		try
		{
			if (sapLogger.isDebugEnabled())
			{
				sapLogger.debug(String.format("Reading productAvailability with attributes: %s", customerId));
			}

			// Fill import pars
			final JCoConnection jcoConnection = getDefaultJCoConnection();
			final JCoFunction bapiMatAvail = jcoConnection.getFunction(BAPI_MATERIAL_AVAILABILITY);
			final JCoParameterList importParameterList = bapiMatAvail.getImportParameterList();

			final String material = ConversionTools.addLeadingZerosToNumericID(product.getCode(), PRODUCT_CODE_LENGTH);
			importParameterList.setValue("MATERIAL", material);
			importParameterList.setValue("PLANT", plant);
			importParameterList.setValue("UNIT", product.getUnit().getSapCode());

			// Add the customer ID if not null
			if (!StringUtils.isEmpty(customerId))
			{
				importParameterList.setValue("CUSTOMER", ConversionTools.addLeadingZerosToNumericID(customerId, CUSTOMER_ID_LENGTH));
			}

			final JCoParameterList tableParameterList = bapiMatAvail.getTableParameterList();
			final JCoTable inputTable = tableParameterList.getTable("WMDVSX");
			inputTable.appendRow();
			inputTable.setValue("REQ_DATE", requestedDate);


			// Call the RFC
			jcoConnection.execute(bapiMatAvail);

			// This is for 4.5b (ERP version)
			final String stockLevel = bapiMatAvail.getExportParameterList().getString("AV_QTY_PLT");

			final Long stockLevelDecimal = ConversionTools.convertQuantityToNumber(stockLevel);

			final JCoTable resultTable = tableParameterList.getTable("WMDVEX");

			if (!resultTable.isEmpty())
			{

				final Map<String, Map<Date, Long>> resultMap = new HashMap<String, Map<Date, Long>>();

				if (resultTable.getNumRows() > 0)
				{

					resultTable.firstRow();

					final Map<Date, Long> availMap = new HashMap<Date, Long>();

					do
					{

						final String dateStr = resultTable.getString("COM_DATE");
						final String quantity = resultTable.getString("COM_QTY");

						final Long quantityDecimal = ConversionTools.convertQuantityToNumber(quantity);

						availMap.put(DateUtil.convertDate(dateStr), quantityDecimal);

					}
					while (resultTable.nextRow());

					resultMap.put(product.getCode(), availMap);

				}
				else
				{

					final JCoStructure errorStructure = bapiMatAvail.getExportParameterList().getStructure("RETURN");
					final String errorType = errorStructure.getString("TYPE");
					final String errorCode = errorStructure.getString("CODE");
					final String errorMessage = errorStructure.getString("MESSAGE");
					sapLogger.log(LogSeverity.ERROR, LogCategories.APPLICATIONS, String.format(
							"ATP Error for Material: %s, Plant:%s, Unit: %s. Error Type: %s, Error Code: %s, Error Message: %s",
							material, plant, product.getUnit().getSapCode(), errorType, errorCode, errorMessage));

				}

				availability = new SapProductAvailabilityImpl(stockLevelDecimal, resultMap);
			}

			cachedBackend.cacheProductAvailability(availability, product, customerId, plant, requestedQuantity);
			return availability;

		}
		finally
		{
			sapLogger.exiting();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.sapproductavailability.backend.SapProductAvailabilityBackend#readPlant(de.hybris.platform
	 * .core.model.product.ProductModel, java.lang.String)
	 */
	@Override
	public String readPlant(final ProductModel product, final String customerId) throws BackendException
	{

		sapLogger.entering("readPlant(...)");

		validateParameterNotNull(customerId, "Customer is empty while reading plant!");
		validateParameterNotNull(product, "Product is empty while reading customer plant!");

		String plant = cachedBackend.readCachedPlant(product.getCode(), customerId,
				getProperty(SapproductavailabilityConstants.SALES_ORG), getProperty(SapproductavailabilityConstants.DIS_CHANNEL));

		if (plant != null && !plant.contentEquals(EMPTY_PLANT))
		{
			return plant;

		}
		else if (plant != null && plant.contentEquals(EMPTY_PLANT))
		{
			return null;

		}
		else
		{
			// Set plant ID to 0000
			plant = EMPTY_PLANT;
		}

		final String material = ConversionTools.addLeadingZerosToNumericID(product.getCode(), PRODUCT_CODE_LENGTH);

		try
		{
			// First try to get the plant from the customer material record
			if (customerId != null)
			{
				plant = readPlantForCustomerMaterial(material,
						ConversionTools.addLeadingZerosToNumericID(customerId, CUSTOMER_ID_LENGTH));

				if (StringUtils.isEmpty(plant) || !isMaterialAvailableInPlant(material, plant))
				{
					// If the  plant is not found try to find it in the material record
					plant = readPlantForMaterial(material);
				}
			}

		}
		finally
		{
			sapLogger.exiting();
		}

		cachedBackend.cachePlant(plant, product.getCode(), customerId, getProperty(SapproductavailabilityConstants.SALES_ORG),
				getProperty(SapproductavailabilityConstants.DIS_CHANNEL));

		return plant.contentEquals(EMPTY_PLANT) ? null : plant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.sapproductavailability.backend.SapProductAvailabilityBackend#readPlantForMaterial(java.
	 * lang.String)
	 */
	@Override
	public String readPlantForMaterial(final String material) throws BackendException
	{

		sapLogger.entering("readPlantForMaterial(...)");

		validateParameterNotNull(material, "product should not be null.");

		String plant = cachedBackend.readCachedPlantMaterial(material, getProperty(SapproductavailabilityConstants.SALES_ORG),
				getProperty(SapproductavailabilityConstants.DIS_CHANNEL));

		if (plant != null)
		{
			return plant;
		}
		try
		{

			final JCoConnection jcoConnection = getDefaultJCoConnection();
			final JCoFunction bapiMatGetDetail = jcoConnection.getFunction(BAPI_MATERIAL_PLANT);
			final JCoParameterList tableParameterList = bapiMatGetDetail.getTableParameterList();
			final JCoParameterList importParameterList = bapiMatGetDetail.getImportParameterList();

			// Set import parameters
			if (importParameterList != null && importParameterList.getListMetaData().hasField("TVTA_SPART"))
			{

				importParameterList.setValue("TVTA_SPART", getProperty(SapproductavailabilityConstants.DIVISION));

				if (sapLogger.isDebugEnabled())
				{
					sapLogger
							.debug(String
									.format(
											"Retrieving plant from the material using material: %s, sales org: %s, distribution channel: %s, division: %s",
											material, "1000", "10", "00"));
				}
			}
			final JCoTable matTable = tableParameterList.getTable("IPRE10");
			matTable.appendRow();
			matTable.setValue("MATNR", ConversionTools.addLeadingZerosToNumericID(material, PRODUCT_CODE_LENGTH));
			matTable.setValue("VKORG", getProperty(SapproductavailabilityConstants.SALES_ORG));
			matTable.setValue("VTWEG", getProperty(SapproductavailabilityConstants.DIS_CHANNEL));

			// Call the RFC BAPI_MVKE_ARRAY_READ
			jcoConnection.execute(bapiMatGetDetail);

			final JCoTable results = tableParameterList.getTable("MVKE_TAB");

			if (results.getNumRows() > 0)
			{
				results.firstRow();

				plant = results.getString("DWERK");

			}

			// Check if the product exists in the plant
			if (StringUtils.isEmpty(plant)
					|| !isMaterialAvailableInPlant(ConversionTools.addLeadingZerosToNumericID(material, PRODUCT_CODE_LENGTH), plant))
			{
				return plant = null;
			}

			// Cache the result
			cachedBackend.cachePlantMaterial(plant, material, getProperty(SapproductavailabilityConstants.SALES_ORG),
					getProperty(SapproductavailabilityConstants.DIS_CHANNEL));

			return plant;

		}
		finally
		{
			sapLogger.exiting();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.sapproductavailability.backend.SapProductAvailabilityBackend#readPlantForCustomerMaterial
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public String readPlantForCustomerMaterial(final String material, final String customerId) throws BackendException
	{

		sapLogger.entering("readPlantForCustomerMaterial(...)");

		validateParameterNotNull(customerId, "Customer is empty while reading the customer's plant!");
		validateParameterNotNull(material, "Product is empty while reading the customer's plant!");

		String plant = cachedBackend.readCachedPlantCustomer(material, customerId,
				getProperty(SapproductavailabilityConstants.SALES_ORG), getProperty(SapproductavailabilityConstants.DIS_CHANNEL));

		if (plant != null)
		{
			return plant;
		}

		try
		{

			final JCoConnection jcoConnection = getDefaultJCoConnection();
			final JCoFunction bapiCustMatInfo = jcoConnection.getFunction(BAPI_CUSTMATINFO_GETDETAILM);
			final JCoParameterList tableParameterList = bapiCustMatInfo.getTableParameterList();

			final JCoTable matTable = tableParameterList.getTable("CUSTOMERMATERIALINFO");
			matTable.appendRow();
			matTable.setValue("MATERIAL", ConversionTools.addLeadingZerosToNumericID(material, PRODUCT_CODE_LENGTH));
			matTable.setValue("SALESORG", getProperty(SapproductavailabilityConstants.SALES_ORG));
			matTable.setValue("DISTR_CHAN", getProperty(SapproductavailabilityConstants.DIS_CHANNEL));

			matTable.setValue("CUSTOMER", ConversionTools.addLeadingZerosToNumericID(customerId, CUSTOMER_ID_LENGTH));

			if (sapLogger.isDebugEnabled())
			{
				sapLogger
						.debug(String
								.format(
										"Retrieving plant from the customer material using customer ID: %s, material: %s, sales org: %s, distribution channel: %s",
										customerId, getProperty("SALESORG"), getProperty("DISTR_CHAN"), material));
			}

			// Call the RFC BAPI_CUSTMATINFO_GETDETAILM
			jcoConnection.execute(bapiCustMatInfo);

			final JCoTable results = tableParameterList.getTable("CUSTOMERMATERIALINFODETAIL");

			if (results.getNumRows() > 0)
			{
				results.firstRow();
				plant = results.getString("PLANT");

			}

			// Check if the product exists in the plant
			if (StringUtils.isEmpty(plant)
					|| !isMaterialAvailableInPlant(ConversionTools.addLeadingZerosToNumericID(material, PRODUCT_CODE_LENGTH), plant))
			{
				plant = null;
			}

			if (StringUtils.isEmpty(plant))
			{
				// Read the custommer's plant
				plant = readPlantForCustomer(ConversionTools.addLeadingZerosToNumericID(material, PRODUCT_CODE_LENGTH),
						ConversionTools.addLeadingZerosToNumericID(customerId, CUSTOMER_ID_LENGTH));
			}

			// Cache the result
			cachedBackend.cachePlantCustomer(plant, material, customerId, getProperty(SapproductavailabilityConstants.SALES_ORG),
					getProperty(SapproductavailabilityConstants.DIS_CHANNEL));

			return plant;
		}
		finally
		{
			sapLogger.exiting();
		}

	}

	/**
	 * Checks if a material is available in the plant given. Uses BAPI_MATERIAL_GET_DETAIL.
	 * 
	 * @param material
	 *           the R/3 material no
	 * @param plant
	 *           the R/3 plant
	 * @return {@code true} if material exists in plant, {@code false} otherwise
	 * @throws BackendException
	 *            exception from backend
	 */
	protected boolean isMaterialAvailableInPlant(final String material, final String plant) throws BackendException
	{

		sapLogger.entering("isMaterialAvailable(...)");

		if (sapLogger.isDebugEnabled())
		{
			sapLogger.debug(String.format("checking availability of material: %s in plant: %s", material, plant));
		}

		boolean result = false;

		try
		{
			final JCoConnection jcoConnection = getDefaultJCoConnection();
			final JCoFunction function = jcoConnection.getFunction(BAPI_MATERIAL_GET_DETAIL);
			final JCoParameterList importParameterList = function.getImportParameterList();

			// Fill import parameters
			importParameterList.setValue("MATERIAL", ConversionTools.addLeadingZerosToNumericID(material, PRODUCT_CODE_LENGTH));
			importParameterList.setValue("PLANT", plant);

			// Call the RFC BAPI_MATERIAL_GET_DETAIL
			jcoConnection.execute(function);

			final JCoStructure results = function.getExportParameterList().getStructure("RETURN");
			result = results.getString("TYPE").equals(BAPI_RETURN_SUCCESS);

			if (sapLogger.isDebugEnabled())
			{
				if (result)
				{
					sapLogger.debug(String.format("Material: %s is available in plant: %s", material, plant));
				}
				else
				{

					sapLogger.debug(String.format("Material: %s is not available in plant: %s", material, plant));

				}

			}

			return result;
		}
		finally
		{
			sapLogger.exiting();
		}

	}


	/**
	 * Gets the customer's plant by calling BAPI_CUSTOMER_GETDETAIL1, only possible with releases from 4.5b onwards.
	 * Checks return segments PE_OPT_PERSONALDATA and PE_OPT_COMPANYDATA for the delivery plant.
	 * 
	 * @param material
	 * @param customerId
	 * @return cutomer's plant
	 * @throws BackendException
	 */
	protected String readPlantForCustomer(final String material, final String customerId) throws BackendException
	{

		sapLogger.entering("readPlantFromCustomer(...)");

		validateParameterNotNull(customerId, "Customer is empty while reading the customer's plant!");
		validateParameterNotNull(material, "Product is empty while reading the customer's plant!");

		String plant = null;

		try
		{
			final JCoConnection jcoConnection = getDefaultJCoConnection();
			final JCoFunction bapiCustGetDetail = jcoConnection.getFunction(BAPI_CUSTOMER_GETDETAIL1);
			final JCoParameterList importParameterList = bapiCustGetDetail.getImportParameterList();

			// Fill import parameters
			importParameterList.setValue("CUSTOMERNO", customerId);
			importParameterList.setValue("PI_SALESORG", getProperty(SapproductavailabilityConstants.SALES_ORG));
			importParameterList.setValue("PI_DISTR_CHAN", getProperty(SapproductavailabilityConstants.DIS_CHANNEL));
			importParameterList.setValue("PI_DIVISION", getProperty(SapproductavailabilityConstants.DIVISION));

			if (sapLogger.isDebugEnabled())
			{
				sapLogger
						.debug(String
								.format(
										"Retrieving plant from the customer using customer ID: %s, sales org: %s, distribution channel: %s and division: %s",
										customerId, getProperty(SapproductavailabilityConstants.SALES_ORG),
										getProperty(SapproductavailabilityConstants.DIS_CHANNEL),
										getProperty(SapproductavailabilityConstants.DIVISION)));
			}

			// Call the RFC BAPI_MATERIAL_GET_DETAIL
			jcoConnection.execute(bapiCustGetDetail);

			// Get return segment
			if (bapiCustGetDetail.getExportParameterList().getListMetaData().hasField("PE_OPT_COMPANYDATA"))
			{

				final JCoStructure returnStructure = bapiCustGetDetail.getExportParameterList().getStructure("PE_OPT_COMPANYDATA");

				plant = returnStructure.getString("DELYG_PLNT");

			}
			else
			{
				// This is for 4.5b (ERP version)
				plant = bapiCustGetDetail.getExportParameterList().getStructure("PE_OPT_PERSONALDATA").getString("DELYG_PLNT");
			}

			if (!StringUtils.isEmpty(plant) && isMaterialAvailableInPlant(material, plant))
			{
				return plant;
			}

			return null;
		}
		finally
		{
			sapLogger.exiting();
		}

	}

	/**
	 * @return the managedConnectionFactory
	 */
	public JCoManagedConnectionFactory getManagedConnectionFactory()
	{
		return managedConnectionFactory;
	}

	/**
	 * @param managedConnectionFactory
	 *           the managedConnectionFactory to set
	 */
	public void setManagedConnectionFactory(final JCoManagedConnectionFactory managedConnectionFactory)
	{
		this.managedConnectionFactory = managedConnectionFactory;
	}

	protected String getProperty(final String name)
	{
		final Object propertyValue = configAccess.getProperty(name);
		validateParameterNotNull(propertyValue, "no property value found for property name : " + name);
		return (String) propertyValue;
	}

}
