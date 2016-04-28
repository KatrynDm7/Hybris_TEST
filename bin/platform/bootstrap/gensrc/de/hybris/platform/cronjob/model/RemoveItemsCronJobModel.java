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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RemoveItemsCronJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class RemoveItemsCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "RemoveItemsCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.itemPKs</code> attribute defined at extension <code>processing</code>. */
	public static final String ITEMPKS = "itemPKs";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.itemsFound</code> attribute defined at extension <code>processing</code>. */
	public static final String ITEMSFOUND = "itemsFound";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.itemsDeleted</code> attribute defined at extension <code>processing</code>. */
	public static final String ITEMSDELETED = "itemsDeleted";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.itemsRefused</code> attribute defined at extension <code>processing</code>. */
	public static final String ITEMSREFUSED = "itemsRefused";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.createSavedValues</code> attribute defined at extension <code>processing</code>. */
	public static final String CREATESAVEDVALUES = "createSavedValues";
	
	
	/** <i>Generated variable</i> - Variable of <code>RemoveItemsCronJob.itemPKs</code> attribute defined at extension <code>processing</code>. */
	private MediaModel _itemPKs;
	
	/** <i>Generated variable</i> - Variable of <code>RemoveItemsCronJob.itemsFound</code> attribute defined at extension <code>processing</code>. */
	private Integer _itemsFound;
	
	/** <i>Generated variable</i> - Variable of <code>RemoveItemsCronJob.itemsDeleted</code> attribute defined at extension <code>processing</code>. */
	private Integer _itemsDeleted;
	
	/** <i>Generated variable</i> - Variable of <code>RemoveItemsCronJob.itemsRefused</code> attribute defined at extension <code>processing</code>. */
	private Integer _itemsRefused;
	
	/** <i>Generated variable</i> - Variable of <code>RemoveItemsCronJob.createSavedValues</code> attribute defined at extension <code>processing</code>. */
	private Boolean _createSavedValues;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RemoveItemsCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RemoveItemsCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _itemPKs initial attribute declared by type <code>RemoveItemsCronJob</code> at extension <code>processing</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public RemoveItemsCronJobModel(final MediaModel _itemPKs, final JobModel _job)
	{
		super();
		setItemPKs(_itemPKs);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _itemPKs initial attribute declared by type <code>RemoveItemsCronJob</code> at extension <code>processing</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RemoveItemsCronJobModel(final MediaModel _itemPKs, final JobModel _job, final ItemModel _owner)
	{
		super();
		setItemPKs(_itemPKs);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.createSavedValues</code> attribute defined at extension <code>processing</code>. 
	 * @return the createSavedValues
	 */
	@Accessor(qualifier = "createSavedValues", type = Accessor.Type.GETTER)
	public Boolean getCreateSavedValues()
	{
		if (this._createSavedValues!=null)
		{
			return _createSavedValues;
		}
		return _createSavedValues = getPersistenceContext().getValue(CREATESAVEDVALUES, _createSavedValues);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.itemPKs</code> attribute defined at extension <code>processing</code>. 
	 * @return the itemPKs
	 */
	@Accessor(qualifier = "itemPKs", type = Accessor.Type.GETTER)
	public MediaModel getItemPKs()
	{
		if (this._itemPKs!=null)
		{
			return _itemPKs;
		}
		return _itemPKs = getPersistenceContext().getValue(ITEMPKS, _itemPKs);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.itemsDeleted</code> attribute defined at extension <code>processing</code>. 
	 * @return the itemsDeleted
	 */
	@Accessor(qualifier = "itemsDeleted", type = Accessor.Type.GETTER)
	public Integer getItemsDeleted()
	{
		if (this._itemsDeleted!=null)
		{
			return _itemsDeleted;
		}
		return _itemsDeleted = getPersistenceContext().getValue(ITEMSDELETED, _itemsDeleted);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.itemsFound</code> attribute defined at extension <code>processing</code>. 
	 * @return the itemsFound
	 */
	@Accessor(qualifier = "itemsFound", type = Accessor.Type.GETTER)
	public Integer getItemsFound()
	{
		if (this._itemsFound!=null)
		{
			return _itemsFound;
		}
		return _itemsFound = getPersistenceContext().getValue(ITEMSFOUND, _itemsFound);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.itemsRefused</code> attribute defined at extension <code>processing</code>. 
	 * @return the itemsRefused
	 */
	@Accessor(qualifier = "itemsRefused", type = Accessor.Type.GETTER)
	public Integer getItemsRefused()
	{
		if (this._itemsRefused!=null)
		{
			return _itemsRefused;
		}
		return _itemsRefused = getPersistenceContext().getValue(ITEMSREFUSED, _itemsRefused);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.createSavedValues</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the createSavedValues
	 */
	@Accessor(qualifier = "createSavedValues", type = Accessor.Type.SETTER)
	public void setCreateSavedValues(final Boolean value)
	{
		_createSavedValues = getPersistenceContext().setValue(CREATESAVEDVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.itemPKs</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the itemPKs
	 */
	@Accessor(qualifier = "itemPKs", type = Accessor.Type.SETTER)
	public void setItemPKs(final MediaModel value)
	{
		_itemPKs = getPersistenceContext().setValue(ITEMPKS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.itemsDeleted</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the itemsDeleted
	 */
	@Accessor(qualifier = "itemsDeleted", type = Accessor.Type.SETTER)
	public void setItemsDeleted(final Integer value)
	{
		_itemsDeleted = getPersistenceContext().setValue(ITEMSDELETED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.itemsFound</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the itemsFound
	 */
	@Accessor(qualifier = "itemsFound", type = Accessor.Type.SETTER)
	public void setItemsFound(final Integer value)
	{
		_itemsFound = getPersistenceContext().setValue(ITEMSFOUND, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.itemsRefused</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the itemsRefused
	 */
	@Accessor(qualifier = "itemsRefused", type = Accessor.Type.SETTER)
	public void setItemsRefused(final Integer value)
	{
		_itemsRefused = getPersistenceContext().setValue(ITEMSREFUSED, value);
	}
	
}
