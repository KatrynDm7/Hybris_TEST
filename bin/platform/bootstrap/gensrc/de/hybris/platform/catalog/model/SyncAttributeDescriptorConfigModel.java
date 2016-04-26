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
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SyncAttributeDescriptorConfig first defined at extension catalog.
 */
@SuppressWarnings("all")
public class SyncAttributeDescriptorConfigModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SyncAttributeDescriptorConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncAttributeDescriptorConfig.syncJob</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCJOB = "syncJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncAttributeDescriptorConfig.attributeDescriptor</code> attribute defined at extension <code>catalog</code>. */
	public static final String ATTRIBUTEDESCRIPTOR = "attributeDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncAttributeDescriptorConfig.includedInSync</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLUDEDINSYNC = "includedInSync";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncAttributeDescriptorConfig.copyByValue</code> attribute defined at extension <code>catalog</code>. */
	public static final String COPYBYVALUE = "copyByValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncAttributeDescriptorConfig.untranslatable</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNTRANSLATABLE = "untranslatable";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncAttributeDescriptorConfig.translateValue</code> attribute defined at extension <code>catalog</code>. */
	public static final String TRANSLATEVALUE = "translateValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncAttributeDescriptorConfig.presetValue</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRESETVALUE = "presetValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncAttributeDescriptorConfig.partiallyTranslatable</code> attribute defined at extension <code>catalog</code>. */
	public static final String PARTIALLYTRANSLATABLE = "partiallyTranslatable";
	
	
	/** <i>Generated variable</i> - Variable of <code>SyncAttributeDescriptorConfig.syncJob</code> attribute defined at extension <code>catalog</code>. */
	private SyncItemJobModel _syncJob;
	
	/** <i>Generated variable</i> - Variable of <code>SyncAttributeDescriptorConfig.attributeDescriptor</code> attribute defined at extension <code>catalog</code>. */
	private AttributeDescriptorModel _attributeDescriptor;
	
	/** <i>Generated variable</i> - Variable of <code>SyncAttributeDescriptorConfig.includedInSync</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _includedInSync;
	
	/** <i>Generated variable</i> - Variable of <code>SyncAttributeDescriptorConfig.copyByValue</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _copyByValue;
	
	/** <i>Generated variable</i> - Variable of <code>SyncAttributeDescriptorConfig.untranslatable</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _untranslatable;
	
	/** <i>Generated variable</i> - Variable of <code>SyncAttributeDescriptorConfig.translateValue</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _translateValue;
	
	/** <i>Generated variable</i> - Variable of <code>SyncAttributeDescriptorConfig.presetValue</code> attribute defined at extension <code>catalog</code>. */
	private Object _presetValue;
	
	/** <i>Generated variable</i> - Variable of <code>SyncAttributeDescriptorConfig.partiallyTranslatable</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _partiallyTranslatable;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SyncAttributeDescriptorConfigModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SyncAttributeDescriptorConfigModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeDescriptor initial attribute declared by type <code>SyncAttributeDescriptorConfig</code> at extension <code>catalog</code>
	 * @param _copyByValue initial attribute declared by type <code>SyncAttributeDescriptorConfig</code> at extension <code>catalog</code>
	 * @param _syncJob initial attribute declared by type <code>SyncAttributeDescriptorConfig</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public SyncAttributeDescriptorConfigModel(final AttributeDescriptorModel _attributeDescriptor, final Boolean _copyByValue, final SyncItemJobModel _syncJob)
	{
		super();
		setAttributeDescriptor(_attributeDescriptor);
		setCopyByValue(_copyByValue);
		setSyncJob(_syncJob);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeDescriptor initial attribute declared by type <code>SyncAttributeDescriptorConfig</code> at extension <code>catalog</code>
	 * @param _copyByValue initial attribute declared by type <code>SyncAttributeDescriptorConfig</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _syncJob initial attribute declared by type <code>SyncAttributeDescriptorConfig</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public SyncAttributeDescriptorConfigModel(final AttributeDescriptorModel _attributeDescriptor, final Boolean _copyByValue, final ItemModel _owner, final SyncItemJobModel _syncJob)
	{
		super();
		setAttributeDescriptor(_attributeDescriptor);
		setCopyByValue(_copyByValue);
		setOwner(_owner);
		setSyncJob(_syncJob);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncAttributeDescriptorConfig.attributeDescriptor</code> attribute defined at extension <code>catalog</code>. 
	 * @return the attributeDescriptor
	 */
	@Accessor(qualifier = "attributeDescriptor", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getAttributeDescriptor()
	{
		if (this._attributeDescriptor!=null)
		{
			return _attributeDescriptor;
		}
		return _attributeDescriptor = getPersistenceContext().getValue(ATTRIBUTEDESCRIPTOR, _attributeDescriptor);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncAttributeDescriptorConfig.copyByValue</code> attribute defined at extension <code>catalog</code>. 
	 * @return the copyByValue
	 */
	@Accessor(qualifier = "copyByValue", type = Accessor.Type.GETTER)
	public Boolean getCopyByValue()
	{
		if (this._copyByValue!=null)
		{
			return _copyByValue;
		}
		return _copyByValue = getPersistenceContext().getValue(COPYBYVALUE, _copyByValue);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncAttributeDescriptorConfig.includedInSync</code> attribute defined at extension <code>catalog</code>. 
	 * @return the includedInSync
	 */
	@Accessor(qualifier = "includedInSync", type = Accessor.Type.GETTER)
	public Boolean getIncludedInSync()
	{
		if (this._includedInSync!=null)
		{
			return _includedInSync;
		}
		return _includedInSync = getPersistenceContext().getValue(INCLUDEDINSYNC, _includedInSync);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncAttributeDescriptorConfig.partiallyTranslatable</code> attribute defined at extension <code>catalog</code>. 
	 * @return the partiallyTranslatable
	 */
	@Accessor(qualifier = "partiallyTranslatable", type = Accessor.Type.GETTER)
	public Boolean getPartiallyTranslatable()
	{
		if (this._partiallyTranslatable!=null)
		{
			return _partiallyTranslatable;
		}
		return _partiallyTranslatable = getPersistenceContext().getValue(PARTIALLYTRANSLATABLE, _partiallyTranslatable);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncAttributeDescriptorConfig.presetValue</code> attribute defined at extension <code>catalog</code>. 
	 * @return the presetValue
	 */
	@Accessor(qualifier = "presetValue", type = Accessor.Type.GETTER)
	public Object getPresetValue()
	{
		if (this._presetValue!=null)
		{
			return _presetValue;
		}
		return _presetValue = getPersistenceContext().getValue(PRESETVALUE, _presetValue);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncAttributeDescriptorConfig.syncJob</code> attribute defined at extension <code>catalog</code>. 
	 * @return the syncJob
	 */
	@Accessor(qualifier = "syncJob", type = Accessor.Type.GETTER)
	public SyncItemJobModel getSyncJob()
	{
		if (this._syncJob!=null)
		{
			return _syncJob;
		}
		return _syncJob = getPersistenceContext().getValue(SYNCJOB, _syncJob);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncAttributeDescriptorConfig.translateValue</code> attribute defined at extension <code>catalog</code>. 
	 * @return the translateValue - deprecated: please use attribute untranslatable
	 */
	@Accessor(qualifier = "translateValue", type = Accessor.Type.GETTER)
	public Boolean getTranslateValue()
	{
		if (this._translateValue!=null)
		{
			return _translateValue;
		}
		return _translateValue = getPersistenceContext().getValue(TRANSLATEVALUE, _translateValue);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncAttributeDescriptorConfig.untranslatable</code> attribute defined at extension <code>catalog</code>. 
	 * @return the untranslatable
	 */
	@Accessor(qualifier = "untranslatable", type = Accessor.Type.GETTER)
	public Boolean getUntranslatable()
	{
		if (this._untranslatable!=null)
		{
			return _untranslatable;
		}
		return _untranslatable = getPersistenceContext().getValue(UNTRANSLATABLE, _untranslatable);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SyncAttributeDescriptorConfig.attributeDescriptor</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the attributeDescriptor
	 */
	@Accessor(qualifier = "attributeDescriptor", type = Accessor.Type.SETTER)
	public void setAttributeDescriptor(final AttributeDescriptorModel value)
	{
		_attributeDescriptor = getPersistenceContext().setValue(ATTRIBUTEDESCRIPTOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncAttributeDescriptorConfig.copyByValue</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the copyByValue
	 */
	@Accessor(qualifier = "copyByValue", type = Accessor.Type.SETTER)
	public void setCopyByValue(final Boolean value)
	{
		_copyByValue = getPersistenceContext().setValue(COPYBYVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncAttributeDescriptorConfig.includedInSync</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the includedInSync
	 */
	@Accessor(qualifier = "includedInSync", type = Accessor.Type.SETTER)
	public void setIncludedInSync(final Boolean value)
	{
		_includedInSync = getPersistenceContext().setValue(INCLUDEDINSYNC, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncAttributeDescriptorConfig.partiallyTranslatable</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the partiallyTranslatable
	 */
	@Accessor(qualifier = "partiallyTranslatable", type = Accessor.Type.SETTER)
	public void setPartiallyTranslatable(final Boolean value)
	{
		_partiallyTranslatable = getPersistenceContext().setValue(PARTIALLYTRANSLATABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncAttributeDescriptorConfig.presetValue</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the presetValue
	 */
	@Accessor(qualifier = "presetValue", type = Accessor.Type.SETTER)
	public void setPresetValue(final Object value)
	{
		_presetValue = getPersistenceContext().setValue(PRESETVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SyncAttributeDescriptorConfig.syncJob</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the syncJob
	 */
	@Accessor(qualifier = "syncJob", type = Accessor.Type.SETTER)
	public void setSyncJob(final SyncItemJobModel value)
	{
		_syncJob = getPersistenceContext().setValue(SYNCJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncAttributeDescriptorConfig.translateValue</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the translateValue - deprecated: please use attribute untranslatable
	 */
	@Accessor(qualifier = "translateValue", type = Accessor.Type.SETTER)
	public void setTranslateValue(final Boolean value)
	{
		_translateValue = getPersistenceContext().setValue(TRANSLATEVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncAttributeDescriptorConfig.untranslatable</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the untranslatable
	 */
	@Accessor(qualifier = "untranslatable", type = Accessor.Type.SETTER)
	public void setUntranslatable(final Boolean value)
	{
		_untranslatable = getPersistenceContext().setValue(UNTRANSLATABLE, value);
	}
	
}
