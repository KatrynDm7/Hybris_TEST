/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 18.04.2016 18:26:54                         ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *  
 */
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CompareCatalogVersionsCronJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CompareCatalogVersionsCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CompareCatalogVersionsCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.processedItemsCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String PROCESSEDITEMSCOUNT = "processedItemsCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEVERSION = "sourceVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETVERSION = "targetVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.missingProducts</code> attribute defined at extension <code>catalog</code>. */
	public static final String MISSINGPRODUCTS = "missingProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.newProducts</code> attribute defined at extension <code>catalog</code>. */
	public static final String NEWPRODUCTS = "newProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.maxPriceTolerance</code> attribute defined at extension <code>catalog</code>. */
	public static final String MAXPRICETOLERANCE = "maxPriceTolerance";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchMissingProducts</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHMISSINGPRODUCTS = "searchMissingProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchMissingCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHMISSINGCATEGORIES = "searchMissingCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchNewProducts</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHNEWPRODUCTS = "searchNewProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchNewCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHNEWCATEGORIES = "searchNewCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchPriceDifferences</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHPRICEDIFFERENCES = "searchPriceDifferences";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.overwriteProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. */
	public static final String OVERWRITEPRODUCTAPPROVALSTATUS = "overwriteProductApprovalStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.priceCompareCustomer</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRICECOMPARECUSTOMER = "priceCompareCustomer";
	
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.processedItemsCount</code> attribute defined at extension <code>catalog</code>. */
	private Integer _processedItemsCount;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _sourceVersion;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _targetVersion;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.missingProducts</code> attribute defined at extension <code>catalog</code>. */
	private Integer _missingProducts;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.newProducts</code> attribute defined at extension <code>catalog</code>. */
	private Integer _newProducts;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.maxPriceTolerance</code> attribute defined at extension <code>catalog</code>. */
	private Double _maxPriceTolerance;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.searchMissingProducts</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _searchMissingProducts;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.searchMissingCategories</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _searchMissingCategories;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.searchNewProducts</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _searchNewProducts;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.searchNewCategories</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _searchNewCategories;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.searchPriceDifferences</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _searchPriceDifferences;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.overwriteProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _overwriteProductApprovalStatus;
	
	/** <i>Generated variable</i> - Variable of <code>CompareCatalogVersionsCronJob.priceCompareCustomer</code> attribute defined at extension <code>catalog</code>. */
	private UserModel _priceCompareCustomer;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CompareCatalogVersionsCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CompareCatalogVersionsCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _missingProducts initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _newProducts initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _processedItemsCount initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CompareCatalogVersionsCronJobModel(final JobModel _job, final int _missingProducts, final int _newProducts, final int _processedItemsCount, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setJob(_job);
		setMissingProducts(_missingProducts);
		setNewProducts(_newProducts);
		setProcessedItemsCount(_processedItemsCount);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _missingProducts initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _newProducts initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processedItemsCount initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CompareCatalogVersionsCronJobModel(final JobModel _job, final int _missingProducts, final int _newProducts, final ItemModel _owner, final int _processedItemsCount, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setJob(_job);
		setMissingProducts(_missingProducts);
		setNewProducts(_newProducts);
		setOwner(_owner);
		setProcessedItemsCount(_processedItemsCount);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.maxPriceTolerance</code> attribute defined at extension <code>catalog</code>. 
	 * @return the maxPriceTolerance
	 */
	@Accessor(qualifier = "maxPriceTolerance", type = Accessor.Type.GETTER)
	public Double getMaxPriceTolerance()
	{
		if (this._maxPriceTolerance!=null)
		{
			return _maxPriceTolerance;
		}
		return _maxPriceTolerance = getPersistenceContext().getValue(MAXPRICETOLERANCE, _maxPriceTolerance);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.missingProducts</code> attribute defined at extension <code>catalog</code>. 
	 * @return the missingProducts
	 */
	@Accessor(qualifier = "missingProducts", type = Accessor.Type.GETTER)
	public int getMissingProducts()
	{
		return toPrimitive( _missingProducts = getPersistenceContext().getValue(MISSINGPRODUCTS, _missingProducts));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.newProducts</code> attribute defined at extension <code>catalog</code>. 
	 * @return the newProducts
	 */
	@Accessor(qualifier = "newProducts", type = Accessor.Type.GETTER)
	public int getNewProducts()
	{
		return toPrimitive( _newProducts = getPersistenceContext().getValue(NEWPRODUCTS, _newProducts));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.overwriteProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. 
	 * @return the overwriteProductApprovalStatus
	 */
	@Accessor(qualifier = "overwriteProductApprovalStatus", type = Accessor.Type.GETTER)
	public Boolean getOverwriteProductApprovalStatus()
	{
		if (this._overwriteProductApprovalStatus!=null)
		{
			return _overwriteProductApprovalStatus;
		}
		return _overwriteProductApprovalStatus = getPersistenceContext().getValue(OVERWRITEPRODUCTAPPROVALSTATUS, _overwriteProductApprovalStatus);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.priceCompareCustomer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the priceCompareCustomer
	 */
	@Accessor(qualifier = "priceCompareCustomer", type = Accessor.Type.GETTER)
	public UserModel getPriceCompareCustomer()
	{
		if (this._priceCompareCustomer!=null)
		{
			return _priceCompareCustomer;
		}
		return _priceCompareCustomer = getPersistenceContext().getValue(PRICECOMPARECUSTOMER, _priceCompareCustomer);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.processedItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the processedItemsCount
	 */
	@Accessor(qualifier = "processedItemsCount", type = Accessor.Type.GETTER)
	public int getProcessedItemsCount()
	{
		return toPrimitive( _processedItemsCount = getPersistenceContext().getValue(PROCESSEDITEMSCOUNT, _processedItemsCount));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchMissingCategories</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchMissingCategories
	 */
	@Accessor(qualifier = "searchMissingCategories", type = Accessor.Type.GETTER)
	public Boolean getSearchMissingCategories()
	{
		if (this._searchMissingCategories!=null)
		{
			return _searchMissingCategories;
		}
		return _searchMissingCategories = getPersistenceContext().getValue(SEARCHMISSINGCATEGORIES, _searchMissingCategories);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchMissingProducts</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchMissingProducts
	 */
	@Accessor(qualifier = "searchMissingProducts", type = Accessor.Type.GETTER)
	public Boolean getSearchMissingProducts()
	{
		if (this._searchMissingProducts!=null)
		{
			return _searchMissingProducts;
		}
		return _searchMissingProducts = getPersistenceContext().getValue(SEARCHMISSINGPRODUCTS, _searchMissingProducts);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchNewCategories</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchNewCategories
	 */
	@Accessor(qualifier = "searchNewCategories", type = Accessor.Type.GETTER)
	public Boolean getSearchNewCategories()
	{
		if (this._searchNewCategories!=null)
		{
			return _searchNewCategories;
		}
		return _searchNewCategories = getPersistenceContext().getValue(SEARCHNEWCATEGORIES, _searchNewCategories);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchNewProducts</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchNewProducts
	 */
	@Accessor(qualifier = "searchNewProducts", type = Accessor.Type.GETTER)
	public Boolean getSearchNewProducts()
	{
		if (this._searchNewProducts!=null)
		{
			return _searchNewProducts;
		}
		return _searchNewProducts = getPersistenceContext().getValue(SEARCHNEWPRODUCTS, _searchNewProducts);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchPriceDifferences</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchPriceDifferences
	 */
	@Accessor(qualifier = "searchPriceDifferences", type = Accessor.Type.GETTER)
	public Boolean getSearchPriceDifferences()
	{
		if (this._searchPriceDifferences!=null)
		{
			return _searchPriceDifferences;
		}
		return _searchPriceDifferences = getPersistenceContext().getValue(SEARCHPRICEDIFFERENCES, _searchPriceDifferences);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getSourceVersion()
	{
		if (this._sourceVersion!=null)
		{
			return _sourceVersion;
		}
		return _sourceVersion = getPersistenceContext().getValue(SOURCEVERSION, _sourceVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getTargetVersion()
	{
		if (this._targetVersion!=null)
		{
			return _targetVersion;
		}
		return _targetVersion = getPersistenceContext().getValue(TARGETVERSION, _targetVersion);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.maxPriceTolerance</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the maxPriceTolerance
	 */
	@Accessor(qualifier = "maxPriceTolerance", type = Accessor.Type.SETTER)
	public void setMaxPriceTolerance(final Double value)
	{
		_maxPriceTolerance = getPersistenceContext().setValue(MAXPRICETOLERANCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.missingProducts</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the missingProducts
	 */
	@Accessor(qualifier = "missingProducts", type = Accessor.Type.SETTER)
	public void setMissingProducts(final int value)
	{
		_missingProducts = getPersistenceContext().setValue(MISSINGPRODUCTS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.newProducts</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the newProducts
	 */
	@Accessor(qualifier = "newProducts", type = Accessor.Type.SETTER)
	public void setNewProducts(final int value)
	{
		_newProducts = getPersistenceContext().setValue(NEWPRODUCTS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.overwriteProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the overwriteProductApprovalStatus
	 */
	@Accessor(qualifier = "overwriteProductApprovalStatus", type = Accessor.Type.SETTER)
	public void setOverwriteProductApprovalStatus(final Boolean value)
	{
		_overwriteProductApprovalStatus = getPersistenceContext().setValue(OVERWRITEPRODUCTAPPROVALSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.priceCompareCustomer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the priceCompareCustomer
	 */
	@Accessor(qualifier = "priceCompareCustomer", type = Accessor.Type.SETTER)
	public void setPriceCompareCustomer(final UserModel value)
	{
		_priceCompareCustomer = getPersistenceContext().setValue(PRICECOMPARECUSTOMER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.processedItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the processedItemsCount
	 */
	@Accessor(qualifier = "processedItemsCount", type = Accessor.Type.SETTER)
	public void setProcessedItemsCount(final int value)
	{
		_processedItemsCount = getPersistenceContext().setValue(PROCESSEDITEMSCOUNT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchMissingCategories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchMissingCategories
	 */
	@Accessor(qualifier = "searchMissingCategories", type = Accessor.Type.SETTER)
	public void setSearchMissingCategories(final Boolean value)
	{
		_searchMissingCategories = getPersistenceContext().setValue(SEARCHMISSINGCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchMissingProducts</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchMissingProducts
	 */
	@Accessor(qualifier = "searchMissingProducts", type = Accessor.Type.SETTER)
	public void setSearchMissingProducts(final Boolean value)
	{
		_searchMissingProducts = getPersistenceContext().setValue(SEARCHMISSINGPRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchNewCategories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchNewCategories
	 */
	@Accessor(qualifier = "searchNewCategories", type = Accessor.Type.SETTER)
	public void setSearchNewCategories(final Boolean value)
	{
		_searchNewCategories = getPersistenceContext().setValue(SEARCHNEWCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchNewProducts</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchNewProducts
	 */
	@Accessor(qualifier = "searchNewProducts", type = Accessor.Type.SETTER)
	public void setSearchNewProducts(final Boolean value)
	{
		_searchNewProducts = getPersistenceContext().setValue(SEARCHNEWPRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchPriceDifferences</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchPriceDifferences
	 */
	@Accessor(qualifier = "searchPriceDifferences", type = Accessor.Type.SETTER)
	public void setSearchPriceDifferences(final Boolean value)
	{
		_searchPriceDifferences = getPersistenceContext().setValue(SEARCHPRICEDIFFERENCES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
	public void setSourceVersion(final CatalogVersionModel value)
	{
		_sourceVersion = getPersistenceContext().setValue(SOURCEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
	public void setTargetVersion(final CatalogVersionModel value)
	{
		_targetVersion = getPersistenceContext().setValue(TARGETVERSION, value);
	}
	
}
