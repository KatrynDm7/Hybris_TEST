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
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationMetaTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RelationDescriptor first defined at extension core.
 */
@SuppressWarnings("all")
public class RelationDescriptorModel extends AttributeDescriptorModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "RelationDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationDescriptor.isSource</code> attribute defined at extension <code>core</code>. */
	public static final String ISSOURCE = "isSource";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationDescriptor.ordered</code> attribute defined at extension <code>core</code>. */
	public static final String ORDERED = "ordered";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationDescriptor.relationName</code> attribute defined at extension <code>core</code>. */
	public static final String RELATIONNAME = "relationName";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationDescriptor.relationType</code> attribute defined at extension <code>core</code>. */
	public static final String RELATIONTYPE = "relationType";
	
	
	/** <i>Generated variable</i> - Variable of <code>RelationDescriptor.isSource</code> attribute defined at extension <code>core</code>. */
	private Boolean _isSource;
	
	/** <i>Generated variable</i> - Variable of <code>RelationDescriptor.ordered</code> attribute defined at extension <code>core</code>. */
	private Boolean _ordered;
	
	/** <i>Generated variable</i> - Variable of <code>RelationDescriptor.relationName</code> attribute defined at extension <code>core</code>. */
	private String _relationName;
	
	/** <i>Generated variable</i> - Variable of <code>RelationDescriptor.relationType</code> attribute defined at extension <code>core</code>. */
	private RelationMetaTypeModel _relationType;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RelationDescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RelationDescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _enclosingType initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public RelationDescriptorModel(final TypeModel _attributeType, final ComposedTypeModel _enclosingType, final Boolean _generate, final Boolean _partOf, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setEnclosingType(_enclosingType);
		setGenerate(_generate);
		setPartOf(_partOf);
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _enclosingType initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public RelationDescriptorModel(final TypeModel _attributeType, final ComposedTypeModel _enclosingType, final Boolean _generate, final ItemModel _owner, final Boolean _partOf, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setEnclosingType(_enclosingType);
		setGenerate(_generate);
		setOwner(_owner);
		setPartOf(_partOf);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationDescriptor.isSource</code> attribute defined at extension <code>core</code>. 
	 * @return the isSource
	 */
	@Accessor(qualifier = "isSource", type = Accessor.Type.GETTER)
	public Boolean getIsSource()
	{
		if (this._isSource!=null)
		{
			return _isSource;
		}
		return _isSource = getPersistenceContext().getValue(ISSOURCE, _isSource);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationDescriptor.ordered</code> attribute defined at extension <code>core</code>. 
	 * @return the ordered
	 */
	@Accessor(qualifier = "ordered", type = Accessor.Type.GETTER)
	public Boolean getOrdered()
	{
		if (this._ordered!=null)
		{
			return _ordered;
		}
		return _ordered = getPersistenceContext().getValue(ORDERED, _ordered);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationDescriptor.relationName</code> attribute defined at extension <code>core</code>. 
	 * @return the relationName
	 */
	@Accessor(qualifier = "relationName", type = Accessor.Type.GETTER)
	public String getRelationName()
	{
		if (this._relationName!=null)
		{
			return _relationName;
		}
		return _relationName = getPersistenceContext().getValue(RELATIONNAME, _relationName);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationDescriptor.relationType</code> attribute defined at extension <code>core</code>. 
	 * @return the relationType
	 */
	@Accessor(qualifier = "relationType", type = Accessor.Type.GETTER)
	public RelationMetaTypeModel getRelationType()
	{
		if (this._relationType!=null)
		{
			return _relationType;
		}
		return _relationType = getPersistenceContext().getValue(RELATIONTYPE, _relationType);
	}
	
}
