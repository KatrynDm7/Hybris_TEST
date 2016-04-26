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
package de.hybris.platform.cuppy.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.LastStartTimeAwareCronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type UpdateCompetitionCronJob first defined at extension cuppy.
 * <p>
 * Updates matches of a competition.
 */
@SuppressWarnings("all")
public class UpdateCompetitionCronJobModel extends LastStartTimeAwareCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "UpdateCompetitionCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>UpdateCompetitionCronJob.competition</code> attribute defined at extension <code>cuppy</code>. */
	public static final String COMPETITION = "competition";
	
	
	/** <i>Generated variable</i> - Variable of <code>UpdateCompetitionCronJob.competition</code> attribute defined at extension <code>cuppy</code>. */
	private CompetitionModel _competition;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public UpdateCompetitionCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public UpdateCompetitionCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _competition initial attribute declared by type <code>UpdateCompetitionCronJob</code> at extension <code>cuppy</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public UpdateCompetitionCronJobModel(final CompetitionModel _competition, final JobModel _job)
	{
		super();
		setCompetition(_competition);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _competition initial attribute declared by type <code>UpdateCompetitionCronJob</code> at extension <code>cuppy</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public UpdateCompetitionCronJobModel(final CompetitionModel _competition, final JobModel _job, final ItemModel _owner)
	{
		super();
		setCompetition(_competition);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UpdateCompetitionCronJob.competition</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the competition - Competition this CronJob will update.
	 */
	@Accessor(qualifier = "competition", type = Accessor.Type.GETTER)
	public CompetitionModel getCompetition()
	{
		if (this._competition!=null)
		{
			return _competition;
		}
		return _competition = getPersistenceContext().getValue(COMPETITION, _competition);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UpdateCompetitionCronJob.competition</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the competition - Competition this CronJob will update.
	 */
	@Accessor(qualifier = "competition", type = Accessor.Type.SETTER)
	public void setCompetition(final CompetitionModel value)
	{
		_competition = getPersistenceContext().setValue(COMPETITION, value);
	}
	
}
