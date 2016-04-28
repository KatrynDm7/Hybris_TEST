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
package de.hybris.platform.advancedsavedquery.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.advancedsavedquery.model.WherePartModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.flexiblesearch.SavedQueryModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type AdvancedSavedQuery first defined at extension advancedsavedquery.
 */
@SuppressWarnings("all")
public class AdvancedSavedQueryModel extends SavedQueryModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AdvancedSavedQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>AdvancedSavedQuery.generatedFlexibleSearch</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String GENERATEDFLEXIBLESEARCH = "generatedFlexibleSearch";
	
	/** <i>Generated constant</i> - Attribute key of <code>AdvancedSavedQuery.whereparts</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String WHEREPARTS = "whereparts";
	
	
	/** <i>Generated variable</i> - Variable of <code>AdvancedSavedQuery.generatedFlexibleSearch</code> attribute defined at extension <code>advancedsavedquery</code>. */
	private String _generatedFlexibleSearch;
	
	/** <i>Generated variable</i> - Variable of <code>AdvancedSavedQuery.whereparts</code> attribute defined at extension <code>advancedsavedquery</code>. */
	private Collection<WherePartModel> _whereparts;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AdvancedSavedQueryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AdvancedSavedQueryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 * @param _resultType initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 */
	@Deprecated
	public AdvancedSavedQueryModel(final String _code, final String _query, final ComposedTypeModel _resultType)
	{
		super();
		setCode(_code);
		setQuery(_query);
		setResultType(_resultType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 * @param _resultType initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 */
	@Deprecated
	public AdvancedSavedQueryModel(final String _code, final ItemModel _owner, final String _query, final ComposedTypeModel _resultType)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setQuery(_query);
		setResultType(_resultType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AdvancedSavedQuery.generatedFlexibleSearch</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the generatedFlexibleSearch
	 */
	@Accessor(qualifier = "generatedFlexibleSearch", type = Accessor.Type.GETTER)
	public String getGeneratedFlexibleSearch()
	{
		if (this._generatedFlexibleSearch!=null)
		{
			return _generatedFlexibleSearch;
		}
		return _generatedFlexibleSearch = getPersistenceContext().getValue(GENERATEDFLEXIBLESEARCH, _generatedFlexibleSearch);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AdvancedSavedQuery.whereparts</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the whereparts
	 */
	@Accessor(qualifier = "whereparts", type = Accessor.Type.GETTER)
	public Collection<WherePartModel> getWhereparts()
	{
		if (this._whereparts!=null)
		{
			return _whereparts;
		}
		return _whereparts = getPersistenceContext().getValue(WHEREPARTS, _whereparts);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AdvancedSavedQuery.whereparts</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the whereparts
	 */
	@Accessor(qualifier = "whereparts", type = Accessor.Type.SETTER)
	public void setWhereparts(final Collection<WherePartModel> value)
	{
		_whereparts = getPersistenceContext().setValue(WHEREPARTS, value);
	}
	
}
