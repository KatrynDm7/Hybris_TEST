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
package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.cuppy.model.CountryFlagModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

/**
 * Generated model class for type Country first defined at extension core.
 */
@SuppressWarnings("all")
public class CountryModel extends C2LItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Country";
	
	/**<i>Generated relation code constant for relation <code>ZoneCountryRelation</code> defining source attribute <code>zones</code> in extension <code>deliveryzone</code>.</i>*/
	public final static String _ZONECOUNTRYRELATION = "ZoneCountryRelation";
	
	/**<i>Generated relation code constant for relation <code>PlayerCountryRelation</code> defining source attribute <code>players</code> in extension <code>cuppy</code>.</i>*/
	public final static String _PLAYERCOUNTRYRELATION = "PlayerCountryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Country.regions</code> attribute defined at extension <code>core</code>. */
	public static final String REGIONS = "regions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Country.zones</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String ZONES = "zones";
	
	/** <i>Generated constant</i> - Attribute key of <code>Country.flag</code> attribute defined at extension <code>cuppy</code>. */
	public static final String FLAG = "flag";
	
	/** <i>Generated constant</i> - Attribute key of <code>Country.players</code> attribute defined at extension <code>cuppy</code>. */
	public static final String PLAYERS = "players";
	
	
	/** <i>Generated variable</i> - Variable of <code>Country.regions</code> attribute defined at extension <code>core</code>. */
	private Collection<RegionModel> _regions;
	
	/** <i>Generated variable</i> - Variable of <code>Country.zones</code> attribute defined at extension <code>deliveryzone</code>. */
	private Set<ZoneModel> _zones;
	
	/** <i>Generated variable</i> - Variable of <code>Country.flag</code> attribute defined at extension <code>cuppy</code>. */
	private CountryFlagModel _flag;
	
	/** <i>Generated variable</i> - Variable of <code>Country.players</code> attribute defined at extension <code>cuppy</code>. */
	private Collection<PlayerModel> _players;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CountryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CountryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Country</code> at extension <code>core</code>
	 */
	@Deprecated
	public CountryModel(final String _isocode)
	{
		super();
		setIsocode(_isocode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Country</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CountryModel(final String _isocode, final ItemModel _owner)
	{
		super();
		setIsocode(_isocode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.flag</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the flag - Media managing flag picture of this country.
	 */
	@Accessor(qualifier = "flag", type = Accessor.Type.GETTER)
	public CountryFlagModel getFlag()
	{
		if (this._flag!=null)
		{
			return _flag;
		}
		return _flag = getPersistenceContext().getValue(FLAG, _flag);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.players</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the players - All players of this country (nation).
	 */
	@Accessor(qualifier = "players", type = Accessor.Type.GETTER)
	public Collection<PlayerModel> getPlayers()
	{
		if (this._players!=null)
		{
			return _players;
		}
		return _players = getPersistenceContext().getValue(PLAYERS, _players);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.regions</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the regions
	 */
	@Accessor(qualifier = "regions", type = Accessor.Type.GETTER)
	public Collection<RegionModel> getRegions()
	{
		if (this._regions!=null)
		{
			return _regions;
		}
		return _regions = getPersistenceContext().getValue(REGIONS, _regions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.zones</code> attribute defined at extension <code>deliveryzone</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the zones
	 */
	@Accessor(qualifier = "zones", type = Accessor.Type.GETTER)
	public Set<ZoneModel> getZones()
	{
		if (this._zones!=null)
		{
			return _zones;
		}
		return _zones = getPersistenceContext().getValue(ZONES, _zones);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Country.flag</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the flag - Media managing flag picture of this country.
	 */
	@Accessor(qualifier = "flag", type = Accessor.Type.SETTER)
	public void setFlag(final CountryFlagModel value)
	{
		_flag = getPersistenceContext().setValue(FLAG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Country.players</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the players - All players of this country (nation).
	 */
	@Accessor(qualifier = "players", type = Accessor.Type.SETTER)
	public void setPlayers(final Collection<PlayerModel> value)
	{
		_players = getPersistenceContext().setValue(PLAYERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Country.regions</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the regions
	 */
	@Accessor(qualifier = "regions", type = Accessor.Type.SETTER)
	public void setRegions(final Collection<RegionModel> value)
	{
		_regions = getPersistenceContext().setValue(REGIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Country.zones</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the zones
	 */
	@Accessor(qualifier = "zones", type = Accessor.Type.SETTER)
	public void setZones(final Set<ZoneModel> value)
	{
		_zones = getPersistenceContext().setValue(ZONES, value);
	}
	
}
