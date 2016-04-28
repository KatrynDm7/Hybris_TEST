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
package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type Domain first defined at extension comments.
 */
@SuppressWarnings("all")
public class DomainModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Domain";
	
	/** <i>Generated constant</i> - Attribute key of <code>Domain.code</code> attribute defined at extension <code>comments</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Domain.name</code> attribute defined at extension <code>comments</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Domain.components</code> attribute defined at extension <code>comments</code>. */
	public static final String COMPONENTS = "components";
	
	/** <i>Generated constant</i> - Attribute key of <code>Domain.commentTypes</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENTTYPES = "commentTypes";
	
	
	/** <i>Generated variable</i> - Variable of <code>Domain.code</code> attribute defined at extension <code>comments</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>Domain.name</code> attribute defined at extension <code>comments</code>. */
	private String _name;
	
	/** <i>Generated variable</i> - Variable of <code>Domain.components</code> attribute defined at extension <code>comments</code>. */
	private Collection<ComponentModel> _components;
	
	/** <i>Generated variable</i> - Variable of <code>Domain.commentTypes</code> attribute defined at extension <code>comments</code>. */
	private Collection<CommentTypeModel> _commentTypes;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DomainModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DomainModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Domain</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DomainModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Domain.code</code> attribute defined at extension <code>comments</code>. 
	 * @return the code - unique identifier of the domain; will be generated if not set
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
	 * <i>Generated method</i> - Getter of the <code>Domain.commentTypes</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the commentTypes
	 */
	@Accessor(qualifier = "commentTypes", type = Accessor.Type.GETTER)
	public Collection<CommentTypeModel> getCommentTypes()
	{
		if (this._commentTypes!=null)
		{
			return _commentTypes;
		}
		return _commentTypes = getPersistenceContext().getValue(COMMENTTYPES, _commentTypes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Domain.components</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the components
	 */
	@Accessor(qualifier = "components", type = Accessor.Type.GETTER)
	public Collection<ComponentModel> getComponents()
	{
		if (this._components!=null)
		{
			return _components;
		}
		return _components = getPersistenceContext().getValue(COMPONENTS, _components);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Domain.name</code> attribute defined at extension <code>comments</code>. 
	 * @return the name - Name of the domain
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		if (this._name!=null)
		{
			return _name;
		}
		return _name = getPersistenceContext().getValue(NAME, _name);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Domain.code</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - unique identifier of the domain; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Domain.commentTypes</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the commentTypes
	 */
	@Accessor(qualifier = "commentTypes", type = Accessor.Type.SETTER)
	public void setCommentTypes(final Collection<CommentTypeModel> value)
	{
		_commentTypes = getPersistenceContext().setValue(COMMENTTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Domain.components</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the components
	 */
	@Accessor(qualifier = "components", type = Accessor.Type.SETTER)
	public void setComponents(final Collection<ComponentModel> value)
	{
		_components = getPersistenceContext().setValue(COMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Domain.name</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the name - Name of the domain
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		_name = getPersistenceContext().setValue(NAME, value);
	}
	
}
