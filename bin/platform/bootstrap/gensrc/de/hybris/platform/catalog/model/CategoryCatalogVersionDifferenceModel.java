/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 28.04.2016 16:51:49                         ---
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
import de.hybris.platform.catalog.enums.CategoryDifferenceMode;
import de.hybris.platform.catalog.model.CatalogVersionDifferenceModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CategoryCatalogVersionDifference first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CategoryCatalogVersionDifferenceModel extends CatalogVersionDifferenceModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CategoryCatalogVersionDifference";
	
	/** <i>Generated constant</i> - Attribute key of <code>CategoryCatalogVersionDifference.sourceCategory</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCECATEGORY = "sourceCategory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CategoryCatalogVersionDifference.targetCategory</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETCATEGORY = "targetCategory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CategoryCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. */
	public static final String MODE = "mode";
	
	
	/** <i>Generated variable</i> - Variable of <code>CategoryCatalogVersionDifference.sourceCategory</code> attribute defined at extension <code>catalog</code>. */
	private CategoryModel _sourceCategory;
	
	/** <i>Generated variable</i> - Variable of <code>CategoryCatalogVersionDifference.targetCategory</code> attribute defined at extension <code>catalog</code>. */
	private CategoryModel _targetCategory;
	
	/** <i>Generated variable</i> - Variable of <code>CategoryCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. */
	private CategoryDifferenceMode _mode;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CategoryCatalogVersionDifferenceModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CategoryCatalogVersionDifferenceModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _mode initial attribute declared by type <code>CategoryCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CategoryCatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final CategoryDifferenceMode _mode, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setMode(_mode);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _mode initial attribute declared by type <code>CategoryCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceCategory initial attribute declared by type <code>CategoryCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetCategory initial attribute declared by type <code>CategoryCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CategoryCatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final CategoryDifferenceMode _mode, final ItemModel _owner, final CategoryModel _sourceCategory, final CatalogVersionModel _sourceVersion, final CategoryModel _targetCategory, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setMode(_mode);
		setOwner(_owner);
		setSourceCategory(_sourceCategory);
		setSourceVersion(_sourceVersion);
		setTargetCategory(_targetCategory);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CategoryCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. 
	 * @return the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
	public CategoryDifferenceMode getMode()
	{
		if (this._mode!=null)
		{
			return _mode;
		}
		return _mode = getPersistenceContext().getValue(MODE, _mode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CategoryCatalogVersionDifference.sourceCategory</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceCategory
	 */
	@Accessor(qualifier = "sourceCategory", type = Accessor.Type.GETTER)
	public CategoryModel getSourceCategory()
	{
		if (this._sourceCategory!=null)
		{
			return _sourceCategory;
		}
		return _sourceCategory = getPersistenceContext().getValue(SOURCECATEGORY, _sourceCategory);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CategoryCatalogVersionDifference.targetCategory</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetCategory
	 */
	@Accessor(qualifier = "targetCategory", type = Accessor.Type.GETTER)
	public CategoryModel getTargetCategory()
	{
		if (this._targetCategory!=null)
		{
			return _targetCategory;
		}
		return _targetCategory = getPersistenceContext().getValue(TARGETCATEGORY, _targetCategory);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CategoryCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
	public void setMode(final CategoryDifferenceMode value)
	{
		_mode = getPersistenceContext().setValue(MODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CategoryCatalogVersionDifference.sourceCategory</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceCategory
	 */
	@Accessor(qualifier = "sourceCategory", type = Accessor.Type.SETTER)
	public void setSourceCategory(final CategoryModel value)
	{
		_sourceCategory = getPersistenceContext().setValue(SOURCECATEGORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CategoryCatalogVersionDifference.targetCategory</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetCategory
	 */
	@Accessor(qualifier = "targetCategory", type = Accessor.Type.SETTER)
	public void setTargetCategory(final CategoryModel value)
	{
		_targetCategory = getPersistenceContext().setValue(TARGETCATEGORY, value);
	}
	
}
