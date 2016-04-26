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
package de.hybris.platform.cockpit.model.template;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type CockpitItemTemplate first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitItemTemplateModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CockpitItemTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.code</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.relatedType</code> attribute defined at extension <code>cockpit</code>. */
	public static final String RELATEDTYPE = "relatedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.classificationClasses</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CLASSIFICATIONCLASSES = "classificationClasses";
	
	
	/** <i>Generated variable</i> - Variable of <code>CockpitItemTemplate.code</code> attribute defined at extension <code>cockpit</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitItemTemplate.relatedType</code> attribute defined at extension <code>cockpit</code>. */
	private ComposedTypeModel _relatedType;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitItemTemplate.classificationClasses</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<ClassificationClassModel> _classificationClasses;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitItemTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitItemTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitItemTemplate</code> at extension <code>cockpit</code>
	 * @param _relatedType initial attribute declared by type <code>CockpitItemTemplate</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitItemTemplateModel(final String _code, final ComposedTypeModel _relatedType)
	{
		super();
		setCode(_code);
		setRelatedType(_relatedType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitItemTemplate</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _relatedType initial attribute declared by type <code>CockpitItemTemplate</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitItemTemplateModel(final String _code, final ItemModel _owner, final ComposedTypeModel _relatedType)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setRelatedType(_relatedType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.classificationClasses</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the classificationClasses
	 */
	@Accessor(qualifier = "classificationClasses", type = Accessor.Type.GETTER)
	public Collection<ClassificationClassModel> getClassificationClasses()
	{
		if (this._classificationClasses!=null)
		{
			return _classificationClasses;
		}
		return _classificationClasses = getPersistenceContext().getValue(CLASSIFICATIONCLASSES, _classificationClasses);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.code</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the code
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
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.relatedType</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the relatedType
	 */
	@Accessor(qualifier = "relatedType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getRelatedType()
	{
		if (this._relatedType!=null)
		{
			return _relatedType;
		}
		return _relatedType = getPersistenceContext().getValue(RELATEDTYPE, _relatedType);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.classificationClasses</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the classificationClasses
	 */
	@Accessor(qualifier = "classificationClasses", type = Accessor.Type.SETTER)
	public void setClassificationClasses(final Collection<ClassificationClassModel> value)
	{
		_classificationClasses = getPersistenceContext().setValue(CLASSIFICATIONCLASSES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.code</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.relatedType</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the relatedType
	 */
	@Accessor(qualifier = "relatedType", type = Accessor.Type.SETTER)
	public void setRelatedType(final ComposedTypeModel value)
	{
		_relatedType = getPersistenceContext().setValue(RELATEDTYPE, value);
	}
	
}
