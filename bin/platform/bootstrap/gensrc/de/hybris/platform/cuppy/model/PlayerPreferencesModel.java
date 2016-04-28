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
package de.hybris.platform.cuppy.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PlayerPreferences first defined at extension cuppy.
 * <p>
 * Frontend preferences of a player.
 */
@SuppressWarnings("all")
public class PlayerPreferencesModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "PlayerPreferences";
	
	/** <i>Generated constant</i> - Attribute key of <code>PlayerPreferences.currentCompetition</code> attribute defined at extension <code>cuppy</code>. */
	public static final String CURRENTCOMPETITION = "currentCompetition";
	
	
	/** <i>Generated variable</i> - Variable of <code>PlayerPreferences.currentCompetition</code> attribute defined at extension <code>cuppy</code>. */
	private CompetitionModel _currentCompetition;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PlayerPreferencesModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PlayerPreferencesModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PlayerPreferencesModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PlayerPreferences.currentCompetition</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the currentCompetition - Competition the player was viewing last at frontend.
	 */
	@Accessor(qualifier = "currentCompetition", type = Accessor.Type.GETTER)
	public CompetitionModel getCurrentCompetition()
	{
		if (this._currentCompetition!=null)
		{
			return _currentCompetition;
		}
		return _currentCompetition = getPersistenceContext().getValue(CURRENTCOMPETITION, _currentCompetition);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PlayerPreferences.currentCompetition</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the currentCompetition - Competition the player was viewing last at frontend.
	 */
	@Accessor(qualifier = "currentCompetition", type = Accessor.Type.SETTER)
	public void setCurrentCompetition(final CompetitionModel value)
	{
		_currentCompetition = getPersistenceContext().setValue(CURRENTCOMPETITION, value);
	}
	
}
