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
package de.hybris.platform.hmc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SavedValueEntry first defined at extension core.
 */
@SuppressWarnings("all")
public class SavedValueEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SavedValueEntry";
	
	/**<i>Generated relation code constant for relation <code>SavedValueEntriesRelation</code> defining source attribute <code>parent</code> in extension <code>core</code>.</i>*/
	public final static String _SAVEDVALUEENTRIESRELATION = "SavedValueEntriesRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValueEntry.modifiedAttribute</code> attribute defined at extension <code>core</code>. */
	public static final String MODIFIEDATTRIBUTE = "modifiedAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValueEntry.OldValueAttributeDescriptor</code> attribute defined at extension <code>core</code>. */
	public static final String OLDVALUEATTRIBUTEDESCRIPTOR = "OldValueAttributeDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValueEntry.oldValue</code> attribute defined at extension <code>core</code>. */
	public static final String OLDVALUE = "oldValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValueEntry.newValue</code> attribute defined at extension <code>core</code>. */
	public static final String NEWVALUE = "newValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValueEntry.parent</code> attribute defined at extension <code>core</code>. */
	public static final String PARENT = "parent";
	
	
	/** <i>Generated variable</i> - Variable of <code>SavedValueEntry.modifiedAttribute</code> attribute defined at extension <code>core</code>. */
	private String _modifiedAttribute;
	
	/** <i>Generated variable</i> - Variable of <code>SavedValueEntry.OldValueAttributeDescriptor</code> attribute defined at extension <code>core</code>. */
	private AttributeDescriptorModel _OldValueAttributeDescriptor;
	
	/** <i>Generated variable</i> - Variable of <code>SavedValueEntry.oldValue</code> attribute defined at extension <code>core</code>. */
	private Object _oldValue;
	
	/** <i>Generated variable</i> - Variable of <code>SavedValueEntry.newValue</code> attribute defined at extension <code>core</code>. */
	private Object _newValue;
	
	/** <i>Generated variable</i> - Variable of <code>SavedValueEntry.parent</code> attribute defined at extension <code>core</code>. */
	private SavedValuesModel _parent;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SavedValueEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SavedValueEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _OldValueAttributeDescriptor initial attribute declared by type <code>SavedValueEntry</code> at extension <code>core</code>
	 * @param _modifiedAttribute initial attribute declared by type <code>SavedValueEntry</code> at extension <code>core</code>
	 * @param _parent initial attribute declared by type <code>SavedValueEntry</code> at extension <code>core</code>
	 */
	@Deprecated
	public SavedValueEntryModel(final AttributeDescriptorModel _OldValueAttributeDescriptor, final String _modifiedAttribute, final SavedValuesModel _parent)
	{
		super();
		setOldValueAttributeDescriptor(_OldValueAttributeDescriptor);
		setModifiedAttribute(_modifiedAttribute);
		setParent(_parent);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _OldValueAttributeDescriptor initial attribute declared by type <code>SavedValueEntry</code> at extension <code>core</code>
	 * @param _modifiedAttribute initial attribute declared by type <code>SavedValueEntry</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _parent initial attribute declared by type <code>SavedValueEntry</code> at extension <code>core</code>
	 */
	@Deprecated
	public SavedValueEntryModel(final AttributeDescriptorModel _OldValueAttributeDescriptor, final String _modifiedAttribute, final ItemModel _owner, final SavedValuesModel _parent)
	{
		super();
		setOldValueAttributeDescriptor(_OldValueAttributeDescriptor);
		setModifiedAttribute(_modifiedAttribute);
		setOwner(_owner);
		setParent(_parent);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValueEntry.modifiedAttribute</code> attribute defined at extension <code>core</code>. 
	 * @return the modifiedAttribute - the modified attribute
	 */
	@Accessor(qualifier = "modifiedAttribute", type = Accessor.Type.GETTER)
	public String getModifiedAttribute()
	{
		if (this._modifiedAttribute!=null)
		{
			return _modifiedAttribute;
		}
		return _modifiedAttribute = getPersistenceContext().getValue(MODIFIEDATTRIBUTE, _modifiedAttribute);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValueEntry.newValue</code> attribute defined at extension <code>core</code>. 
	 * @return the newValue - the new value
	 */
	@Accessor(qualifier = "newValue", type = Accessor.Type.GETTER)
	public Object getNewValue()
	{
		if (this._newValue!=null)
		{
			return _newValue;
		}
		return _newValue = getPersistenceContext().getValue(NEWVALUE, _newValue);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValueEntry.oldValue</code> attribute defined at extension <code>core</code>. 
	 * @return the oldValue - the old value
	 */
	@Accessor(qualifier = "oldValue", type = Accessor.Type.GETTER)
	public Object getOldValue()
	{
		if (this._oldValue!=null)
		{
			return _oldValue;
		}
		return _oldValue = getPersistenceContext().getValue(OLDVALUE, _oldValue);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValueEntry.OldValueAttributeDescriptor</code> attribute defined at extension <code>core</code>. 
	 * @return the OldValueAttributeDescriptor - the old attributedescriptor
	 */
	@Accessor(qualifier = "OldValueAttributeDescriptor", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getOldValueAttributeDescriptor()
	{
		if (this._OldValueAttributeDescriptor!=null)
		{
			return _OldValueAttributeDescriptor;
		}
		return _OldValueAttributeDescriptor = getPersistenceContext().getValue(OLDVALUEATTRIBUTEDESCRIPTOR, _OldValueAttributeDescriptor);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValueEntry.parent</code> attribute defined at extension <code>core</code>. 
	 * @return the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.GETTER)
	public SavedValuesModel getParent()
	{
		if (this._parent!=null)
		{
			return _parent;
		}
		return _parent = getPersistenceContext().getValue(PARENT, _parent);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedValueEntry.modifiedAttribute</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the modifiedAttribute - the modified attribute
	 */
	@Accessor(qualifier = "modifiedAttribute", type = Accessor.Type.SETTER)
	public void setModifiedAttribute(final String value)
	{
		_modifiedAttribute = getPersistenceContext().setValue(MODIFIEDATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedValueEntry.newValue</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the newValue - the new value
	 */
	@Accessor(qualifier = "newValue", type = Accessor.Type.SETTER)
	public void setNewValue(final Object value)
	{
		_newValue = getPersistenceContext().setValue(NEWVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedValueEntry.oldValue</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the oldValue - the old value
	 */
	@Accessor(qualifier = "oldValue", type = Accessor.Type.SETTER)
	public void setOldValue(final Object value)
	{
		_oldValue = getPersistenceContext().setValue(OLDVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedValueEntry.OldValueAttributeDescriptor</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the OldValueAttributeDescriptor - the old attributedescriptor
	 */
	@Accessor(qualifier = "OldValueAttributeDescriptor", type = Accessor.Type.SETTER)
	public void setOldValueAttributeDescriptor(final AttributeDescriptorModel value)
	{
		_OldValueAttributeDescriptor = getPersistenceContext().setValue(OLDVALUEATTRIBUTEDESCRIPTOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SavedValueEntry.parent</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.SETTER)
	public void setParent(final SavedValuesModel value)
	{
		_parent = getPersistenceContext().setValue(PARENT, value);
	}
	
}
