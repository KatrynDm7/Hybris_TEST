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
 * Generated model class for type TimePointStatistic first defined at extension cuppy.
 * <p>
 * Collects statistics for a certain point of time.
 */
@SuppressWarnings("all")
public class TimePointStatisticModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "TimePointStatistic";
	
	/** <i>Generated constant</i> - Attribute key of <code>TimePointStatistic.playersOnlineCount</code> attribute defined at extension <code>cuppy</code>. */
	public static final String PLAYERSONLINECOUNT = "playersOnlineCount";
	
	
	/** <i>Generated variable</i> - Variable of <code>TimePointStatistic.playersOnlineCount</code> attribute defined at extension <code>cuppy</code>. */
	private Integer _playersOnlineCount;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TimePointStatisticModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TimePointStatisticModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _playersOnlineCount initial attribute declared by type <code>TimePointStatistic</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public TimePointStatisticModel(final int _playersOnlineCount)
	{
		super();
		setPlayersOnlineCount(_playersOnlineCount);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _playersOnlineCount initial attribute declared by type <code>TimePointStatistic</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public TimePointStatisticModel(final ItemModel _owner, final int _playersOnlineCount)
	{
		super();
		setOwner(_owner);
		setPlayersOnlineCount(_playersOnlineCount);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TimePointStatistic.playersOnlineCount</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	@Accessor(qualifier = "playersOnlineCount", type = Accessor.Type.GETTER)
	public int getPlayersOnlineCount()
	{
		return toPrimitive( _playersOnlineCount = getPersistenceContext().getValue(PLAYERSONLINECOUNT, _playersOnlineCount));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TimePointStatistic.playersOnlineCount</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	@Accessor(qualifier = "playersOnlineCount", type = Accessor.Type.SETTER)
	public void setPlayersOnlineCount(final int value)
	{
		_playersOnlineCount = getPersistenceContext().setValue(PLAYERSONLINECOUNT, toObject(value));
	}
	
}
