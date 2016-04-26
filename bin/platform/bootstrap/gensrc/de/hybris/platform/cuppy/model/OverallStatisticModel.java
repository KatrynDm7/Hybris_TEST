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
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OverallStatistic first defined at extension cuppy.
 * <p>
 * Collects statistics not realted to a certain point of time.
 */
@SuppressWarnings("all")
public class OverallStatisticModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "OverallStatistic";
	
	/** <i>Generated constant</i> - Attribute key of <code>OverallStatistic.playersOnlineMaxCount</code> attribute defined at extension <code>cuppy</code>. */
	public static final String PLAYERSONLINEMAXCOUNT = "playersOnlineMaxCount";
	
	
	/** <i>Generated variable</i> - Variable of <code>OverallStatistic.playersOnlineMaxCount</code> attribute defined at extension <code>cuppy</code>. */
	private Integer _playersOnlineMaxCount;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OverallStatisticModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OverallStatisticModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _playersOnlineMaxCount initial attribute declared by type <code>OverallStatistic</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public OverallStatisticModel(final int _playersOnlineMaxCount)
	{
		super();
		setPlayersOnlineMaxCount(_playersOnlineMaxCount);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _playersOnlineMaxCount initial attribute declared by type <code>OverallStatistic</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public OverallStatisticModel(final ItemModel _owner, final int _playersOnlineMaxCount)
	{
		super();
		setOwner(_owner);
		setPlayersOnlineMaxCount(_playersOnlineMaxCount);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	@Accessor(qualifier = "playersOnlineMaxCount", type = Accessor.Type.GETTER)
	public int getPlayersOnlineMaxCount()
	{
		return toPrimitive( _playersOnlineMaxCount = getPersistenceContext().getValue(PLAYERSONLINEMAXCOUNT, _playersOnlineMaxCount));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OverallStatistic.playersOnlineMaxCount</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	@Accessor(qualifier = "playersOnlineMaxCount", type = Accessor.Type.SETTER)
	public void setPlayersOnlineMaxCount(final int value)
	{
		_playersOnlineMaxCount = getPersistenceContext().setValue(PLAYERSONLINEMAXCOUNT, toObject(value));
	}
	
}
