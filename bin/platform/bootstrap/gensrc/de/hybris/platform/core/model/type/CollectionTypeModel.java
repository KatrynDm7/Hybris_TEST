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
import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CollectionType first defined at extension core.
 */
@SuppressWarnings("all")
public class CollectionTypeModel extends TypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CollectionType";
	
	/** <i>Generated constant</i> - Attribute key of <code>CollectionType.elementType</code> attribute defined at extension <code>core</code>. */
	public static final String ELEMENTTYPE = "elementType";
	
	/** <i>Generated constant</i> - Attribute key of <code>CollectionType.typeOfCollection</code> attribute defined at extension <code>core</code>. */
	public static final String TYPEOFCOLLECTION = "typeOfCollection";
	
	
	/** <i>Generated variable</i> - Variable of <code>CollectionType.elementType</code> attribute defined at extension <code>core</code>. */
	private TypeModel _elementType;
	
	/** <i>Generated variable</i> - Variable of <code>CollectionType.typeOfCollection</code> attribute defined at extension <code>core</code>. */
	private TypeOfCollectionEnum _typeOfCollection;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CollectionTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CollectionTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _elementType initial attribute declared by type <code>CollectionType</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 */
	@Deprecated
	public CollectionTypeModel(final String _code, final TypeModel _elementType, final Boolean _generate)
	{
		super();
		setCode(_code);
		setElementType(_elementType);
		setGenerate(_generate);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _elementType initial attribute declared by type <code>CollectionType</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _typeOfCollection initial attribute declared by type <code>CollectionType</code> at extension <code>core</code>
	 */
	@Deprecated
	public CollectionTypeModel(final String _code, final TypeModel _elementType, final Boolean _generate, final ItemModel _owner, final TypeOfCollectionEnum _typeOfCollection)
	{
		super();
		setCode(_code);
		setElementType(_elementType);
		setGenerate(_generate);
		setOwner(_owner);
		setTypeOfCollection(_typeOfCollection);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CollectionType.elementType</code> attribute defined at extension <code>core</code>. 
	 * @return the elementType
	 */
	@Accessor(qualifier = "elementType", type = Accessor.Type.GETTER)
	public TypeModel getElementType()
	{
		if (this._elementType!=null)
		{
			return _elementType;
		}
		return _elementType = getPersistenceContext().getValue(ELEMENTTYPE, _elementType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CollectionType.typeOfCollection</code> attribute defined at extension <code>core</code>. 
	 * @return the typeOfCollection
	 */
	@Accessor(qualifier = "typeOfCollection", type = Accessor.Type.GETTER)
	public TypeOfCollectionEnum getTypeOfCollection()
	{
		if (this._typeOfCollection!=null)
		{
			return _typeOfCollection;
		}
		return _typeOfCollection = getPersistenceContext().getValue(TYPEOFCOLLECTION, _typeOfCollection);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CollectionType.elementType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the elementType
	 */
	@Accessor(qualifier = "elementType", type = Accessor.Type.SETTER)
	public void setElementType(final TypeModel value)
	{
		_elementType = getPersistenceContext().setValue(ELEMENTTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CollectionType.typeOfCollection</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the typeOfCollection
	 */
	@Accessor(qualifier = "typeOfCollection", type = Accessor.Type.SETTER)
	public void setTypeOfCollection(final TypeOfCollectionEnum value)
	{
		_typeOfCollection = getPersistenceContext().setValue(TYPEOFCOLLECTION, value);
	}
	
}
