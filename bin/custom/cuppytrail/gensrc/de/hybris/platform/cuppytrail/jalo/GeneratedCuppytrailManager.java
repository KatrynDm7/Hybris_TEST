/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11.04.2016 18:36:08                         ---
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
package de.hybris.platform.cuppytrail.jalo;

import de.hybris.platform.cuppy.jalo.Match;
import de.hybris.platform.cuppytrail.constants.CuppytrailConstants;
import de.hybris.platform.cuppytrail.jalo.Stadium;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>CuppytrailManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedCuppytrailManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("stadium", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.cuppy.jalo.Match", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public Stadium createStadium(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CuppytrailConstants.TC.STADIUM );
			return (Stadium)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Stadium : "+e.getMessage(), 0 );
		}
	}
	
	public Stadium createStadium(final Map attributeValues)
	{
		return createStadium( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return CuppytrailConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.stadium</code> attribute.
	 * @return the stadium
	 */
	public Stadium getStadium(final SessionContext ctx, final Match item)
	{
		return (Stadium)item.getProperty( ctx, CuppytrailConstants.Attributes.Match.STADIUM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.stadium</code> attribute.
	 * @return the stadium
	 */
	public Stadium getStadium(final Match item)
	{
		return getStadium( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.stadium</code> attribute. 
	 * @param value the stadium
	 */
	public void setStadium(final SessionContext ctx, final Match item, final Stadium value)
	{
		item.setProperty(ctx, CuppytrailConstants.Attributes.Match.STADIUM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.stadium</code> attribute. 
	 * @param value the stadium
	 */
	public void setStadium(final Match item, final Stadium value)
	{
		setStadium( getSession().getSessionContext(), item, value );
	}
	
}
