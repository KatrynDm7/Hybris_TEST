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
package de.hybris.platform.advancedsavedquery.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.advancedsavedquery.model.AbstractAdvancedSavedQuerySearchParameterModel;
import de.hybris.platform.advancedsavedquery.model.AdvancedSavedQueryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type WherePart first defined at extension advancedsavedquery.
 */
@SuppressWarnings("all")
public class WherePartModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WherePart";
	
	/**<i>Generated relation code constant for relation <code>Query2WherePartRelation</code> defining source attribute <code>savedQuery</code> in extension <code>advancedsavedquery</code>.</i>*/
	public final static String _QUERY2WHEREPARTRELATION = "Query2WherePartRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WherePart.and</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String AND = "and";
	
	/** <i>Generated constant</i> - Attribute key of <code>WherePart.replacePattern</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String REPLACEPATTERN = "replacePattern";
	
	/** <i>Generated constant</i> - Attribute key of <code>WherePart.savedQuery</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String SAVEDQUERY = "savedQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>WherePart.dynamicParams</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String DYNAMICPARAMS = "dynamicParams";
	
	
	/** <i>Generated variable</i> - Variable of <code>WherePart.and</code> attribute defined at extension <code>advancedsavedquery</code>. */
	private Boolean _and;
	
	/** <i>Generated variable</i> - Variable of <code>WherePart.replacePattern</code> attribute defined at extension <code>advancedsavedquery</code>. */
	private String _replacePattern;
	
	/** <i>Generated variable</i> - Variable of <code>WherePart.savedQuery</code> attribute defined at extension <code>advancedsavedquery</code>. */
	private AdvancedSavedQueryModel _savedQuery;
	
	/** <i>Generated variable</i> - Variable of <code>WherePart.dynamicParams</code> attribute defined at extension <code>advancedsavedquery</code>. */
	private Collection<AbstractAdvancedSavedQuerySearchParameterModel> _dynamicParams;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WherePartModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WherePartModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _replacePattern initial attribute declared by type <code>WherePart</code> at extension <code>advancedsavedquery</code>
	 * @param _savedQuery initial attribute declared by type <code>WherePart</code> at extension <code>advancedsavedquery</code>
	 */
	@Deprecated
	public WherePartModel(final String _replacePattern, final AdvancedSavedQueryModel _savedQuery)
	{
		super();
		setReplacePattern(_replacePattern);
		setSavedQuery(_savedQuery);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _replacePattern initial attribute declared by type <code>WherePart</code> at extension <code>advancedsavedquery</code>
	 * @param _savedQuery initial attribute declared by type <code>WherePart</code> at extension <code>advancedsavedquery</code>
	 */
	@Deprecated
	public WherePartModel(final ItemModel _owner, final String _replacePattern, final AdvancedSavedQueryModel _savedQuery)
	{
		super();
		setOwner(_owner);
		setReplacePattern(_replacePattern);
		setSavedQuery(_savedQuery);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WherePart.and</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the and
	 */
	@Accessor(qualifier = "and", type = Accessor.Type.GETTER)
	public Boolean getAnd()
	{
		if (this._and!=null)
		{
			return _and;
		}
		return _and = getPersistenceContext().getValue(AND, _and);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WherePart.dynamicParams</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the dynamicParams
	 */
	@Accessor(qualifier = "dynamicParams", type = Accessor.Type.GETTER)
	public Collection<AbstractAdvancedSavedQuerySearchParameterModel> getDynamicParams()
	{
		if (this._dynamicParams!=null)
		{
			return _dynamicParams;
		}
		return _dynamicParams = getPersistenceContext().getValue(DYNAMICPARAMS, _dynamicParams);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WherePart.replacePattern</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the replacePattern
	 */
	@Accessor(qualifier = "replacePattern", type = Accessor.Type.GETTER)
	public String getReplacePattern()
	{
		if (this._replacePattern!=null)
		{
			return _replacePattern;
		}
		return _replacePattern = getPersistenceContext().getValue(REPLACEPATTERN, _replacePattern);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WherePart.savedQuery</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the savedQuery
	 */
	@Accessor(qualifier = "savedQuery", type = Accessor.Type.GETTER)
	public AdvancedSavedQueryModel getSavedQuery()
	{
		if (this._savedQuery!=null)
		{
			return _savedQuery;
		}
		return _savedQuery = getPersistenceContext().getValue(SAVEDQUERY, _savedQuery);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WherePart.and</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the and
	 */
	@Accessor(qualifier = "and", type = Accessor.Type.SETTER)
	public void setAnd(final Boolean value)
	{
		_and = getPersistenceContext().setValue(AND, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WherePart.dynamicParams</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the dynamicParams
	 */
	@Accessor(qualifier = "dynamicParams", type = Accessor.Type.SETTER)
	public void setDynamicParams(final Collection<AbstractAdvancedSavedQuerySearchParameterModel> value)
	{
		_dynamicParams = getPersistenceContext().setValue(DYNAMICPARAMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WherePart.replacePattern</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the replacePattern
	 */
	@Accessor(qualifier = "replacePattern", type = Accessor.Type.SETTER)
	public void setReplacePattern(final String value)
	{
		_replacePattern = getPersistenceContext().setValue(REPLACEPATTERN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WherePart.savedQuery</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the savedQuery
	 */
	@Accessor(qualifier = "savedQuery", type = Accessor.Type.SETTER)
	public void setSavedQuery(final AdvancedSavedQueryModel value)
	{
		_savedQuery = getPersistenceContext().setValue(SAVEDQUERY, value);
	}
	
}
