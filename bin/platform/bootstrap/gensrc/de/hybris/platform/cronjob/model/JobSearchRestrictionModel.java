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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type JobSearchRestriction first defined at extension processing.
 */
@SuppressWarnings("all")
public class JobSearchRestrictionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "JobSearchRestriction";
	
	/**<i>Generated relation code constant for relation <code>JobSearchRestrictionRelation</code> defining source attribute <code>job</code> in extension <code>processing</code>.</i>*/
	public final static String _JOBSEARCHRESTRICTIONRELATION = "JobSearchRestrictionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobSearchRestriction.code</code> attribute defined at extension <code>processing</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobSearchRestriction.type</code> attribute defined at extension <code>processing</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobSearchRestriction.query</code> attribute defined at extension <code>processing</code>. */
	public static final String QUERY = "query";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobSearchRestriction.job</code> attribute defined at extension <code>processing</code>. */
	public static final String JOB = "job";
	
	
	/** <i>Generated variable</i> - Variable of <code>JobSearchRestriction.code</code> attribute defined at extension <code>processing</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>JobSearchRestriction.type</code> attribute defined at extension <code>processing</code>. */
	private ComposedTypeModel _type;
	
	/** <i>Generated variable</i> - Variable of <code>JobSearchRestriction.query</code> attribute defined at extension <code>processing</code>. */
	private String _query;
	
	/** <i>Generated variable</i> - Variable of <code>JobSearchRestriction.job</code> attribute defined at extension <code>processing</code>. */
	private JobModel _job;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JobSearchRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JobSearchRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public JobSearchRestrictionModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobSearchRestriction.code</code> attribute defined at extension <code>processing</code>. 
	 * @return the code - ID
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
	 * <i>Generated method</i> - Getter of the <code>JobSearchRestriction.job</code> attribute defined at extension <code>processing</code>. 
	 * @return the job - assigned job
	 */
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public JobModel getJob()
	{
		if (this._job!=null)
		{
			return _job;
		}
		return _job = getPersistenceContext().getValue(JOB, _job);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobSearchRestriction.query</code> attribute defined at extension <code>processing</code>. 
	 * @return the query - Search Restriction query
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
	 * <i>Generated method</i> - Getter of the <code>JobSearchRestriction.type</code> attribute defined at extension <code>processing</code>. 
	 * @return the type - Code of the Type for which the restriction is defined
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public ComposedTypeModel getType()
	{
		if (this._type!=null)
		{
			return _type;
		}
		return _type = getPersistenceContext().getValue(TYPE, _type);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JobSearchRestriction.code</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the code - ID
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JobSearchRestriction.job</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the job - assigned job
	 */
	@Accessor(qualifier = "job", type = Accessor.Type.SETTER)
	public void setJob(final JobModel value)
	{
		_job = getPersistenceContext().setValue(JOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JobSearchRestriction.query</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the query - Search Restriction query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.SETTER)
	public void setQuery(final String value)
	{
		_query = getPersistenceContext().setValue(QUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JobSearchRestriction.type</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the type - Code of the Type for which the restriction is defined
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final ComposedTypeModel value)
	{
		_type = getPersistenceContext().setValue(TYPE, value);
	}
	
}
