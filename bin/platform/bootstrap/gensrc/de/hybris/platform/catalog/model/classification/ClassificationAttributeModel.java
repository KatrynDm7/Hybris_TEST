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
package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type ClassificationAttribute first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ClassificationAttributeModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ClassificationAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.systemVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYSTEMVERSION = "systemVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.code</code> attribute defined at extension <code>catalog</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.externalID</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXTERNALID = "externalID";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.classes</code> attribute defined at extension <code>catalog</code>. */
	public static final String CLASSES = "classes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.defaultAttributeValues</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEFAULTATTRIBUTEVALUES = "defaultAttributeValues";
	
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttribute.systemVersion</code> attribute defined at extension <code>catalog</code>. */
	private ClassificationSystemVersionModel _systemVersion;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttribute.code</code> attribute defined at extension <code>catalog</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttribute.externalID</code> attribute defined at extension <code>catalog</code>. */
	private String _externalID;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttribute.classes</code> attribute defined at extension <code>catalog</code>. */
	private List<ClassificationClassModel> _classes;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttribute.defaultAttributeValues</code> attribute defined at extension <code>catalog</code>. */
	private List<ClassificationAttributeValueModel> _defaultAttributeValues;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassificationAttributeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassificationAttributeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ClassificationAttribute</code> at extension <code>catalog</code>
	 * @param _systemVersion initial attribute declared by type <code>ClassificationAttribute</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationAttributeModel(final String _code, final ClassificationSystemVersionModel _systemVersion)
	{
		super();
		setCode(_code);
		setSystemVersion(_systemVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ClassificationAttribute</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _systemVersion initial attribute declared by type <code>ClassificationAttribute</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationAttributeModel(final String _code, final ItemModel _owner, final ClassificationSystemVersionModel _systemVersion)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setSystemVersion(_systemVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.classes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the classes - list of classes this attribute was assigned to
	 */
	@Accessor(qualifier = "classes", type = Accessor.Type.GETTER)
	public List<ClassificationClassModel> getClasses()
	{
		if (this._classes!=null)
		{
			return _classes;
		}
		return _classes = getPersistenceContext().getValue(CLASSES, _classes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.code</code> attribute defined at extension <code>catalog</code>. 
	 * @return the code - Code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		if (this._code!=null)
		{
			return _code;
		}
		return _code = getPersistenceContext().getValue(CODE, _code);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.defaultAttributeValues</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the defaultAttributeValues
	 */
	@Accessor(qualifier = "defaultAttributeValues", type = Accessor.Type.GETTER)
	public List<ClassificationAttributeValueModel> getDefaultAttributeValues()
	{
		if (this._defaultAttributeValues!=null)
		{
			return _defaultAttributeValues;
		}
		return _defaultAttributeValues = getPersistenceContext().getValue(DEFAULTATTRIBUTEVALUES, _defaultAttributeValues);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.externalID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
	public String getExternalID()
	{
		if (this._externalID!=null)
		{
			return _externalID;
		}
		return _externalID = getPersistenceContext().getValue(EXTERNALID, _externalID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. 
	 * @return the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.systemVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
	public ClassificationSystemVersionModel getSystemVersion()
	{
		if (this._systemVersion!=null)
		{
			return _systemVersion;
		}
		return _systemVersion = getPersistenceContext().getValue(SYSTEMVERSION, _systemVersion);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ClassificationAttribute.code</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - Code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.defaultAttributeValues</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the defaultAttributeValues
	 */
	@Accessor(qualifier = "defaultAttributeValues", type = Accessor.Type.SETTER)
	public void setDefaultAttributeValues(final List<ClassificationAttributeValueModel> value)
	{
		_defaultAttributeValues = getPersistenceContext().setValue(DEFAULTATTRIBUTEVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.externalID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
	public void setExternalID(final String value)
	{
		_externalID = getPersistenceContext().setValue(EXTERNALID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.systemVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
	public void setSystemVersion(final ClassificationSystemVersionModel value)
	{
		_systemVersion = getPersistenceContext().setValue(SYSTEMVERSION, value);
	}
	
}
