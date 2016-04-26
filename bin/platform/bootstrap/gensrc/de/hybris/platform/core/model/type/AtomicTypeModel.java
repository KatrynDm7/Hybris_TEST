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
package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type AtomicType first defined at extension core.
 */
@SuppressWarnings("all")
public class AtomicTypeModel extends TypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AtomicType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AtomicType.javaClass</code> attribute defined at extension <code>core</code>. */
	public static final String JAVACLASS = "javaClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>AtomicType.subtypes</code> attribute defined at extension <code>core</code>. */
	public static final String SUBTYPES = "subtypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>AtomicType.superType</code> attribute defined at extension <code>core</code>. */
	public static final String SUPERTYPE = "superType";
	
	
	/** <i>Generated variable</i> - Variable of <code>AtomicType.javaClass</code> attribute defined at extension <code>core</code>. */
	private Class _javaClass;
	
	/** <i>Generated variable</i> - Variable of <code>AtomicType.subtypes</code> attribute defined at extension <code>core</code>. */
	private Collection<AtomicTypeModel> _subtypes;
	
	/** <i>Generated variable</i> - Variable of <code>AtomicType.superType</code> attribute defined at extension <code>core</code>. */
	private AtomicTypeModel _superType;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AtomicTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AtomicTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _javaClass initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 */
	@Deprecated
	public AtomicTypeModel(final String _code, final Boolean _generate, final Class _javaClass)
	{
		super();
		setCode(_code);
		setGenerate(_generate);
		setJavaClass(_javaClass);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _javaClass initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 */
	@Deprecated
	public AtomicTypeModel(final String _code, final Boolean _generate, final Class _javaClass, final ItemModel _owner, final AtomicTypeModel _superType)
	{
		super();
		setCode(_code);
		setGenerate(_generate);
		setJavaClass(_javaClass);
		setOwner(_owner);
		setSuperType(_superType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtomicType.javaClass</code> attribute defined at extension <code>core</code>. 
	 * @return the javaClass
	 */
	@Accessor(qualifier = "javaClass", type = Accessor.Type.GETTER)
	public Class getJavaClass()
	{
		if (this._javaClass!=null)
		{
			return _javaClass;
		}
		return _javaClass = getPersistenceContext().getValue(JAVACLASS, _javaClass);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtomicType.subtypes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the subtypes
	 */
	@Accessor(qualifier = "subtypes", type = Accessor.Type.GETTER)
	public Collection<AtomicTypeModel> getSubtypes()
	{
		if (this._subtypes!=null)
		{
			return _subtypes;
		}
		return _subtypes = getPersistenceContext().getValue(SUBTYPES, _subtypes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtomicType.superType</code> attribute defined at extension <code>core</code>. 
	 * @return the superType
	 */
	@Accessor(qualifier = "superType", type = Accessor.Type.GETTER)
	public AtomicTypeModel getSuperType()
	{
		if (this._superType!=null)
		{
			return _superType;
		}
		return _superType = getPersistenceContext().getValue(SUPERTYPE, _superType);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AtomicType.javaClass</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the javaClass
	 */
	@Accessor(qualifier = "javaClass", type = Accessor.Type.SETTER)
	public void setJavaClass(final Class value)
	{
		_javaClass = getPersistenceContext().setValue(JAVACLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AtomicType.superType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the superType
	 */
	@Accessor(qualifier = "superType", type = Accessor.Type.SETTER)
	public void setSuperType(final AtomicTypeModel value)
	{
		_superType = getPersistenceContext().setValue(SUPERTYPE, value);
	}
	
}
