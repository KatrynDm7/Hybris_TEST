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
package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ConfigAttributeDescriptor first defined at extension core.
 */
@SuppressWarnings("all")
public class ConfigAttributeDescriptorModel extends AttributeDescriptorModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ConfigAttributeDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConfigAttributeDescriptor.externalQualifier</code> attribute defined at extension <code>core</code>. */
	public static final String EXTERNALQUALIFIER = "externalQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConfigAttributeDescriptor.storeInDatabase</code> attribute defined at extension <code>core</code>. */
	public static final String STOREINDATABASE = "storeInDatabase";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConfigAttributeDescriptor.needRestart</code> attribute defined at extension <code>core</code>. */
	public static final String NEEDRESTART = "needRestart";
	
	
	/** <i>Generated variable</i> - Variable of <code>ConfigAttributeDescriptor.externalQualifier</code> attribute defined at extension <code>core</code>. */
	private String _externalQualifier;
	
	/** <i>Generated variable</i> - Variable of <code>ConfigAttributeDescriptor.storeInDatabase</code> attribute defined at extension <code>core</code>. */
	private Boolean _storeInDatabase;
	
	/** <i>Generated variable</i> - Variable of <code>ConfigAttributeDescriptor.needRestart</code> attribute defined at extension <code>core</code>. */
	private Boolean _needRestart;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConfigAttributeDescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConfigAttributeDescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _enclosingType initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _externalQualifier initial attribute declared by type <code>ConfigAttributeDescriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public ConfigAttributeDescriptorModel(final TypeModel _attributeType, final ComposedTypeModel _enclosingType, final String _externalQualifier, final Boolean _generate, final Boolean _partOf, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setEnclosingType(_enclosingType);
		setExternalQualifier(_externalQualifier);
		setGenerate(_generate);
		setPartOf(_partOf);
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _enclosingType initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _externalQualifier initial attribute declared by type <code>ConfigAttributeDescriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public ConfigAttributeDescriptorModel(final TypeModel _attributeType, final ComposedTypeModel _enclosingType, final String _externalQualifier, final Boolean _generate, final ItemModel _owner, final Boolean _partOf, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setEnclosingType(_enclosingType);
		setExternalQualifier(_externalQualifier);
		setGenerate(_generate);
		setOwner(_owner);
		setPartOf(_partOf);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConfigAttributeDescriptor.externalQualifier</code> attribute defined at extension <code>core</code>. 
	 * @return the externalQualifier
	 */
	@Accessor(qualifier = "externalQualifier", type = Accessor.Type.GETTER)
	public String getExternalQualifier()
	{
		if (this._externalQualifier!=null)
		{
			return _externalQualifier;
		}
		return _externalQualifier = getPersistenceContext().getValue(EXTERNALQUALIFIER, _externalQualifier);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConfigAttributeDescriptor.needRestart</code> attribute defined at extension <code>core</code>. 
	 * @return the needRestart
	 */
	@Accessor(qualifier = "needRestart", type = Accessor.Type.GETTER)
	public Boolean getNeedRestart()
	{
		if (this._needRestart!=null)
		{
			return _needRestart;
		}
		return _needRestart = getPersistenceContext().getValue(NEEDRESTART, _needRestart);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConfigAttributeDescriptor.storeInDatabase</code> attribute defined at extension <code>core</code>. 
	 * @return the storeInDatabase
	 */
	@Accessor(qualifier = "storeInDatabase", type = Accessor.Type.GETTER)
	public Boolean getStoreInDatabase()
	{
		if (this._storeInDatabase!=null)
		{
			return _storeInDatabase;
		}
		return _storeInDatabase = getPersistenceContext().getValue(STOREINDATABASE, _storeInDatabase);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConfigAttributeDescriptor.externalQualifier</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the externalQualifier
	 */
	@Accessor(qualifier = "externalQualifier", type = Accessor.Type.SETTER)
	public void setExternalQualifier(final String value)
	{
		_externalQualifier = getPersistenceContext().setValue(EXTERNALQUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConfigAttributeDescriptor.needRestart</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the needRestart
	 */
	@Accessor(qualifier = "needRestart", type = Accessor.Type.SETTER)
	public void setNeedRestart(final Boolean value)
	{
		_needRestart = getPersistenceContext().setValue(NEEDRESTART, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConfigAttributeDescriptor.storeInDatabase</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the storeInDatabase
	 */
	@Accessor(qualifier = "storeInDatabase", type = Accessor.Type.SETTER)
	public void setStoreInDatabase(final Boolean value)
	{
		_storeInDatabase = getPersistenceContext().setValue(STOREINDATABASE, value);
	}
	
}
