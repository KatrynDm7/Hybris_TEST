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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CatalogVersionDifference first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionDifferenceModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CatalogVersionDifference";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEVERSION = "sourceVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETVERSION = "targetVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.cronJob</code> attribute defined at extension <code>catalog</code>. */
	public static final String CRONJOB = "cronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.differenceText</code> attribute defined at extension <code>catalog</code>. */
	public static final String DIFFERENCETEXT = "differenceText";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.differenceValue</code> attribute defined at extension <code>catalog</code>. */
	public static final String DIFFERENCEVALUE = "differenceValue";
	
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionDifference.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _sourceVersion;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionDifference.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _targetVersion;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionDifference.cronJob</code> attribute defined at extension <code>catalog</code>. */
	private CompareCatalogVersionsCronJobModel _cronJob;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionDifference.differenceText</code> attribute defined at extension <code>catalog</code>. */
	private String _differenceText;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionDifference.differenceValue</code> attribute defined at extension <code>catalog</code>. */
	private Double _differenceValue;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionDifferenceModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionDifferenceModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final ItemModel _owner, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setOwner(_owner);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.cronJob</code> attribute defined at extension <code>catalog</code>. 
	 * @return the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
	public CompareCatalogVersionsCronJobModel getCronJob()
	{
		if (this._cronJob!=null)
		{
			return _cronJob;
		}
		return _cronJob = getPersistenceContext().getValue(CRONJOB, _cronJob);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.differenceText</code> attribute defined at extension <code>catalog</code>. 
	 * @return the differenceText
	 */
	@Accessor(qualifier = "differenceText", type = Accessor.Type.GETTER)
	public String getDifferenceText()
	{
		if (this._differenceText!=null)
		{
			return _differenceText;
		}
		return _differenceText = getPersistenceContext().getValue(DIFFERENCETEXT, _differenceText);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.differenceValue</code> attribute defined at extension <code>catalog</code>. 
	 * @return the differenceValue
	 */
	@Accessor(qualifier = "differenceValue", type = Accessor.Type.GETTER)
	public Double getDifferenceValue()
	{
		if (this._differenceValue!=null)
		{
			return _differenceValue;
		}
		return _differenceValue = getPersistenceContext().getValue(DIFFERENCEVALUE, _differenceValue);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getSourceVersion()
	{
		if (this._sourceVersion!=null)
		{
			return _sourceVersion;
		}
		return _sourceVersion = getPersistenceContext().getValue(SOURCEVERSION, _sourceVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getTargetVersion()
	{
		if (this._targetVersion!=null)
		{
			return _targetVersion;
		}
		return _targetVersion = getPersistenceContext().getValue(TARGETVERSION, _targetVersion);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersionDifference.cronJob</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
	public void setCronJob(final CompareCatalogVersionsCronJobModel value)
	{
		_cronJob = getPersistenceContext().setValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionDifference.differenceText</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the differenceText
	 */
	@Accessor(qualifier = "differenceText", type = Accessor.Type.SETTER)
	public void setDifferenceText(final String value)
	{
		_differenceText = getPersistenceContext().setValue(DIFFERENCETEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionDifference.differenceValue</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the differenceValue
	 */
	@Accessor(qualifier = "differenceValue", type = Accessor.Type.SETTER)
	public void setDifferenceValue(final Double value)
	{
		_differenceValue = getPersistenceContext().setValue(DIFFERENCEVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersionDifference.sourceVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
	public void setSourceVersion(final CatalogVersionModel value)
	{
		_sourceVersion = getPersistenceContext().setValue(SOURCEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersionDifference.targetVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
	public void setTargetVersion(final CatalogVersionModel value)
	{
		_targetVersion = getPersistenceContext().setValue(TARGETVERSION, value);
	}
	
}
