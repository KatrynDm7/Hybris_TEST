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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.MediaProcessCronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type FlexibleSearchCronJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class FlexibleSearchCronJobModel extends MediaProcessCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "FlexibleSearchCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchCronJob.query</code> attribute defined at extension <code>processing</code>. */
	public static final String QUERY = "query";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchCronJob.failOnUnknown</code> attribute defined at extension <code>processing</code>. */
	public static final String FAILONUNKNOWN = "failOnUnknown";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchCronJob.dontNeedTotal</code> attribute defined at extension <code>processing</code>. */
	public static final String DONTNEEDTOTAL = "dontNeedTotal";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchCronJob.rangeStart</code> attribute defined at extension <code>processing</code>. */
	public static final String RANGESTART = "rangeStart";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchCronJob.count</code> attribute defined at extension <code>processing</code>. */
	public static final String COUNT = "count";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchCronJob.searchResult</code> attribute defined at extension <code>processing</code>. */
	public static final String SEARCHRESULT = "searchResult";
	
	
	/** <i>Generated variable</i> - Variable of <code>FlexibleSearchCronJob.query</code> attribute defined at extension <code>processing</code>. */
	private String _query;
	
	/** <i>Generated variable</i> - Variable of <code>FlexibleSearchCronJob.failOnUnknown</code> attribute defined at extension <code>processing</code>. */
	private Boolean _failOnUnknown;
	
	/** <i>Generated variable</i> - Variable of <code>FlexibleSearchCronJob.dontNeedTotal</code> attribute defined at extension <code>processing</code>. */
	private Boolean _dontNeedTotal;
	
	/** <i>Generated variable</i> - Variable of <code>FlexibleSearchCronJob.rangeStart</code> attribute defined at extension <code>processing</code>. */
	private Integer _rangeStart;
	
	/** <i>Generated variable</i> - Variable of <code>FlexibleSearchCronJob.count</code> attribute defined at extension <code>processing</code>. */
	private Integer _count;
	
	/** <i>Generated variable</i> - Variable of <code>FlexibleSearchCronJob.searchResult</code> attribute defined at extension <code>processing</code>. */
	private Collection<String> _searchResult;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FlexibleSearchCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FlexibleSearchCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public FlexibleSearchCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public FlexibleSearchCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchCronJob.count</code> attribute defined at extension <code>processing</code>. 
	 * @return the count
	 */
	@Accessor(qualifier = "count", type = Accessor.Type.GETTER)
	public Integer getCount()
	{
		if (this._count!=null)
		{
			return _count;
		}
		return _count = getPersistenceContext().getValue(COUNT, _count);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchCronJob.dontNeedTotal</code> attribute defined at extension <code>processing</code>. 
	 * @return the dontNeedTotal
	 */
	@Accessor(qualifier = "dontNeedTotal", type = Accessor.Type.GETTER)
	public Boolean getDontNeedTotal()
	{
		if (this._dontNeedTotal!=null)
		{
			return _dontNeedTotal;
		}
		return _dontNeedTotal = getPersistenceContext().getValue(DONTNEEDTOTAL, _dontNeedTotal);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchCronJob.failOnUnknown</code> attribute defined at extension <code>processing</code>. 
	 * @return the failOnUnknown
	 */
	@Accessor(qualifier = "failOnUnknown", type = Accessor.Type.GETTER)
	public Boolean getFailOnUnknown()
	{
		if (this._failOnUnknown!=null)
		{
			return _failOnUnknown;
		}
		return _failOnUnknown = getPersistenceContext().getValue(FAILONUNKNOWN, _failOnUnknown);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchCronJob.query</code> attribute defined at extension <code>processing</code>. 
	 * @return the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.GETTER)
	public String getQuery()
	{
		if (this._query!=null)
		{
			return _query;
		}
		return _query = getPersistenceContext().getValue(QUERY, _query);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchCronJob.rangeStart</code> attribute defined at extension <code>processing</code>. 
	 * @return the rangeStart
	 */
	@Accessor(qualifier = "rangeStart", type = Accessor.Type.GETTER)
	public Integer getRangeStart()
	{
		if (this._rangeStart!=null)
		{
			return _rangeStart;
		}
		return _rangeStart = getPersistenceContext().getValue(RANGESTART, _rangeStart);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchCronJob.searchResult</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the searchResult
	 */
	@Accessor(qualifier = "searchResult", type = Accessor.Type.GETTER)
	public Collection<String> getSearchResult()
	{
		if (this._searchResult!=null)
		{
			return _searchResult;
		}
		return _searchResult = getPersistenceContext().getValue(SEARCHRESULT, _searchResult);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlexibleSearchCronJob.count</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the count
	 */
	@Accessor(qualifier = "count", type = Accessor.Type.SETTER)
	public void setCount(final Integer value)
	{
		_count = getPersistenceContext().setValue(COUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlexibleSearchCronJob.dontNeedTotal</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the dontNeedTotal
	 */
	@Accessor(qualifier = "dontNeedTotal", type = Accessor.Type.SETTER)
	public void setDontNeedTotal(final Boolean value)
	{
		_dontNeedTotal = getPersistenceContext().setValue(DONTNEEDTOTAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlexibleSearchCronJob.failOnUnknown</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the failOnUnknown
	 */
	@Accessor(qualifier = "failOnUnknown", type = Accessor.Type.SETTER)
	public void setFailOnUnknown(final Boolean value)
	{
		_failOnUnknown = getPersistenceContext().setValue(FAILONUNKNOWN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlexibleSearchCronJob.query</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.SETTER)
	public void setQuery(final String value)
	{
		_query = getPersistenceContext().setValue(QUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlexibleSearchCronJob.rangeStart</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the rangeStart
	 */
	@Accessor(qualifier = "rangeStart", type = Accessor.Type.SETTER)
	public void setRangeStart(final Integer value)
	{
		_rangeStart = getPersistenceContext().setValue(RANGESTART, value);
	}
	
}
