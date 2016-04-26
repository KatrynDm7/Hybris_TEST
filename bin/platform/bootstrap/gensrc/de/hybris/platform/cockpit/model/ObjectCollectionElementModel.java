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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ObjectCollectionElement first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class ObjectCollectionElementModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ObjectCollectionElement";
	
	/**<i>Generated relation code constant for relation <code>CockpitAbstractCollection2ElementRelation</code> defining source attribute <code>collection</code> in extension <code>cockpit</code>.</i>*/
	public final static String _COCKPITABSTRACTCOLLECTION2ELEMENTRELATION = "CockpitAbstractCollection2ElementRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ObjectCollectionElement.objectTypeCode</code> attribute defined at extension <code>cockpit</code>. */
	public static final String OBJECTTYPECODE = "objectTypeCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ObjectCollectionElement.position</code> attribute defined at extension <code>cockpit</code>. */
	public static final String POSITION = "position";
	
	/** <i>Generated constant</i> - Attribute key of <code>ObjectCollectionElement.collection</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COLLECTION = "collection";
	
	
	/** <i>Generated variable</i> - Variable of <code>ObjectCollectionElement.objectTypeCode</code> attribute defined at extension <code>cockpit</code>. */
	private String _objectTypeCode;
	
	/** <i>Generated variable</i> - Variable of <code>ObjectCollectionElement.position</code> attribute defined at extension <code>cockpit</code>. */
	private Integer _position;
	
	/** <i>Generated variable</i> - Variable of <code>ObjectCollectionElement.collection</code> attribute defined at extension <code>cockpit</code>. */
	private CockpitObjectAbstractCollectionModel _collection;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ObjectCollectionElementModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ObjectCollectionElementModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _collection initial attribute declared by type <code>ObjectCollectionElement</code> at extension <code>cockpit</code>
	 * @param _objectTypeCode initial attribute declared by type <code>ObjectCollectionElement</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public ObjectCollectionElementModel(final CockpitObjectAbstractCollectionModel _collection, final String _objectTypeCode)
	{
		super();
		setCollection(_collection);
		setObjectTypeCode(_objectTypeCode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _collection initial attribute declared by type <code>ObjectCollectionElement</code> at extension <code>cockpit</code>
	 * @param _objectTypeCode initial attribute declared by type <code>ObjectCollectionElement</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ObjectCollectionElementModel(final CockpitObjectAbstractCollectionModel _collection, final String _objectTypeCode, final ItemModel _owner)
	{
		super();
		setCollection(_collection);
		setObjectTypeCode(_objectTypeCode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ObjectCollectionElement.collection</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the collection
	 */
	@Accessor(qualifier = "collection", type = Accessor.Type.GETTER)
	public CockpitObjectAbstractCollectionModel getCollection()
	{
		if (this._collection!=null)
		{
			return _collection;
		}
		return _collection = getPersistenceContext().getValue(COLLECTION, _collection);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ObjectCollectionElement.objectTypeCode</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the objectTypeCode
	 */
	@Accessor(qualifier = "objectTypeCode", type = Accessor.Type.GETTER)
	public String getObjectTypeCode()
	{
		if (this._objectTypeCode!=null)
		{
			return _objectTypeCode;
		}
		return _objectTypeCode = getPersistenceContext().getValue(OBJECTTYPECODE, _objectTypeCode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ObjectCollectionElement.position</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the position
	 */
	@Accessor(qualifier = "position", type = Accessor.Type.GETTER)
	public Integer getPosition()
	{
		if (this._position!=null)
		{
			return _position;
		}
		return _position = getPersistenceContext().getValue(POSITION, _position);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ObjectCollectionElement.collection</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the collection
	 */
	@Accessor(qualifier = "collection", type = Accessor.Type.SETTER)
	public void setCollection(final CockpitObjectAbstractCollectionModel value)
	{
		_collection = getPersistenceContext().setValue(COLLECTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ObjectCollectionElement.objectTypeCode</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the objectTypeCode
	 */
	@Accessor(qualifier = "objectTypeCode", type = Accessor.Type.SETTER)
	public void setObjectTypeCode(final String value)
	{
		_objectTypeCode = getPersistenceContext().setValue(OBJECTTYPECODE, value);
	}
	
}
