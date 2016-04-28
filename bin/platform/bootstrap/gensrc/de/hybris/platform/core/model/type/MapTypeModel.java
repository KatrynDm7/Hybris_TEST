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
package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MapType first defined at extension core.
 */
@SuppressWarnings("all")
public class MapTypeModel extends TypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MapType";
	
	/** <i>Generated constant</i> - Attribute key of <code>MapType.argumentType</code> attribute defined at extension <code>core</code>. */
	public static final String ARGUMENTTYPE = "argumentType";
	
	/** <i>Generated constant</i> - Attribute key of <code>MapType.returntype</code> attribute defined at extension <code>core</code>. */
	public static final String RETURNTYPE = "returntype";
	
	
	/** <i>Generated variable</i> - Variable of <code>MapType.argumentType</code> attribute defined at extension <code>core</code>. */
	private TypeModel _argumentType;
	
	/** <i>Generated variable</i> - Variable of <code>MapType.returntype</code> attribute defined at extension <code>core</code>. */
	private TypeModel _returntype;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MapTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MapTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _argumentType initial attribute declared by type <code>MapType</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _returntype initial attribute declared by type <code>MapType</code> at extension <code>core</code>
	 */
	@Deprecated
	public MapTypeModel(final TypeModel _argumentType, final String _code, final Boolean _generate, final TypeModel _returntype)
	{
		super();
		setArgumentType(_argumentType);
		setCode(_code);
		setGenerate(_generate);
		setReturntype(_returntype);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _argumentType initial attribute declared by type <code>MapType</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _returntype initial attribute declared by type <code>MapType</code> at extension <code>core</code>
	 */
	@Deprecated
	public MapTypeModel(final TypeModel _argumentType, final String _code, final Boolean _generate, final ItemModel _owner, final TypeModel _returntype)
	{
		super();
		setArgumentType(_argumentType);
		setCode(_code);
		setGenerate(_generate);
		setOwner(_owner);
		setReturntype(_returntype);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MapType.argumentType</code> attribute defined at extension <code>core</code>. 
	 * @return the argumentType
	 */
	@Accessor(qualifier = "argumentType", type = Accessor.Type.GETTER)
	public TypeModel getArgumentType()
	{
		if (this._argumentType!=null)
		{
			return _argumentType;
		}
		return _argumentType = getPersistenceContext().getValue(ARGUMENTTYPE, _argumentType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MapType.returntype</code> attribute defined at extension <code>core</code>. 
	 * @return the returntype
	 */
	@Accessor(qualifier = "returntype", type = Accessor.Type.GETTER)
	public TypeModel getReturntype()
	{
		if (this._returntype!=null)
		{
			return _returntype;
		}
		return _returntype = getPersistenceContext().getValue(RETURNTYPE, _returntype);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MapType.argumentType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the argumentType
	 */
	@Accessor(qualifier = "argumentType", type = Accessor.Type.SETTER)
	public void setArgumentType(final TypeModel value)
	{
		_argumentType = getPersistenceContext().setValue(ARGUMENTTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MapType.returntype</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the returntype
	 */
	@Accessor(qualifier = "returntype", type = Accessor.Type.SETTER)
	public void setReturntype(final TypeModel value)
	{
		_returntype = getPersistenceContext().setValue(RETURNTYPE, value);
	}
	
}
