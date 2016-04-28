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
import java.util.Locale;

/**
 * Generated model class for type News first defined at extension cuppy.
 * <p>
 * A localized message posted at frontend by administrator which can be send as newsletter additional.
 */
@SuppressWarnings("all")
public class NewsModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "News";
	
	/**<i>Generated relation code constant for relation <code>CompetitionNewsRelation</code> defining source attribute <code>competition</code> in extension <code>cuppy</code>.</i>*/
	public final static String _COMPETITIONNEWSRELATION = "CompetitionNewsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>News.content</code> attribute defined at extension <code>cuppy</code>. */
	public static final String CONTENT = "content";
	
	/** <i>Generated constant</i> - Attribute key of <code>News.eMail</code> attribute defined at extension <code>cuppy</code>. */
	public static final String EMAIL = "eMail";
	
	/** <i>Generated constant</i> - Attribute key of <code>News.competition</code> attribute defined at extension <code>cuppy</code>. */
	public static final String COMPETITION = "competition";
	
	
	/** <i>Generated variable</i> - Variable of <code>News.eMail</code> attribute defined at extension <code>cuppy</code>. */
	private Boolean _eMail;
	
	/** <i>Generated variable</i> - Variable of <code>News.competition</code> attribute defined at extension <code>cuppy</code>. */
	private CompetitionModel _competition;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public NewsModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public NewsModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _eMail initial attribute declared by type <code>News</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public NewsModel(final boolean _eMail)
	{
		super();
		setEMail(_eMail);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _eMail initial attribute declared by type <code>News</code> at extension <code>cuppy</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public NewsModel(final boolean _eMail, final ItemModel _owner)
	{
		super();
		setEMail(_eMail);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.competition</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the competition - The competition this news belongs to. If null it belongs to every competition.
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
	 * <i>Generated method</i> - Getter of the <code>News.content</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the content - The message of the news.
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent()
	{
		return getContent(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>News.content</code> attribute defined at extension <code>cuppy</code>. 
	 * @param loc the value localization key 
	 * @return the content - The message of the news.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(CONTENT, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.eMail</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the eMail - If true, the news will be send as newsletter too.
	 */
	@Accessor(qualifier = "eMail", type = Accessor.Type.GETTER)
	public boolean isEMail()
	{
		return toPrimitive( _eMail = getPersistenceContext().getValue(EMAIL, _eMail));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>News.competition</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the competition - The competition this news belongs to. If null it belongs to every competition.
	 */
	@Accessor(qualifier = "competition", type = Accessor.Type.SETTER)
	public void setCompetition(final CompetitionModel value)
	{
		_competition = getPersistenceContext().setValue(COMPETITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>News.content</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the content - The message of the news.
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value)
	{
		setContent(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>News.content</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the content - The message of the news.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CONTENT, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>News.eMail</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the eMail - If true, the news will be send as newsletter too.
	 */
	@Accessor(qualifier = "eMail", type = Accessor.Type.SETTER)
	public void setEMail(final boolean value)
	{
		_eMail = getPersistenceContext().setValue(EMAIL, toObject(value));
	}
	
}
