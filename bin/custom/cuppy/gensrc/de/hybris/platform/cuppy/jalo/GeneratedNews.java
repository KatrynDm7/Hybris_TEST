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
package de.hybris.platform.cuppy.jalo;

import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.jalo.Competition;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.News News}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedNews extends GenericItem
{
	/** Qualifier of the <code>News.content</code> attribute **/
	public static final String CONTENT = "content";
	/** Qualifier of the <code>News.eMail</code> attribute **/
	public static final String EMAIL = "eMail";
	/** Qualifier of the <code>News.competition</code> attribute **/
	public static final String COMPETITION = "competition";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n COMPETITION's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedNews> COMPETITIONHANDLER = new BidirectionalOneToManyHandler<GeneratedNews>(
	CuppyConstants.TC.NEWS,
	false,
	"competition",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CONTENT, AttributeMode.INITIAL);
		tmp.put(EMAIL, AttributeMode.INITIAL);
		tmp.put(COMPETITION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.competition</code> attribute.
	 * @return the competition - The competition this news belongs to. If null it belongs to every competition.
	 */
	public Competition getCompetition(final SessionContext ctx)
	{
		return (Competition)getProperty( ctx, COMPETITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.competition</code> attribute.
	 * @return the competition - The competition this news belongs to. If null it belongs to every competition.
	 */
	public Competition getCompetition()
	{
		return getCompetition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.competition</code> attribute. 
	 * @param value the competition - The competition this news belongs to. If null it belongs to every competition.
	 */
	public void setCompetition(final SessionContext ctx, final Competition value)
	{
		COMPETITIONHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.competition</code> attribute. 
	 * @param value the competition - The competition this news belongs to. If null it belongs to every competition.
	 */
	public void setCompetition(final Competition value)
	{
		setCompetition( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.content</code> attribute.
	 * @return the content - The message of the news.
	 */
	public String getContent(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedNews.getContent requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.content</code> attribute.
	 * @return the content - The message of the news.
	 */
	public String getContent()
	{
		return getContent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.content</code> attribute. 
	 * @return the localized content - The message of the news.
	 */
	public Map<Language,String> getAllContent(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,CONTENT,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.content</code> attribute. 
	 * @return the localized content - The message of the news.
	 */
	public Map<Language,String> getAllContent()
	{
		return getAllContent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.content</code> attribute. 
	 * @param value the content - The message of the news.
	 */
	public void setContent(final SessionContext ctx, final String value)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedNews.setContent requires a session language", 0 );
		}
		setLocalizedProperty(ctx, CONTENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.content</code> attribute. 
	 * @param value the content - The message of the news.
	 */
	public void setContent(final String value)
	{
		setContent( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.content</code> attribute. 
	 * @param value the content - The message of the news.
	 */
	public void setAllContent(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,CONTENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.content</code> attribute. 
	 * @param value the content - The message of the news.
	 */
	public void setAllContent(final Map<Language,String> value)
	{
		setAllContent( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		COMPETITIONHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.eMail</code> attribute.
	 * @return the eMail - If true, the news will be send as newsletter too.
	 */
	public Boolean isEMail(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, EMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.eMail</code> attribute.
	 * @return the eMail - If true, the news will be send as newsletter too.
	 */
	public Boolean isEMail()
	{
		return isEMail( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.eMail</code> attribute. 
	 * @return the eMail - If true, the news will be send as newsletter too.
	 */
	public boolean isEMailAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isEMail( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>News.eMail</code> attribute. 
	 * @return the eMail - If true, the news will be send as newsletter too.
	 */
	public boolean isEMailAsPrimitive()
	{
		return isEMailAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.eMail</code> attribute. 
	 * @param value the eMail - If true, the news will be send as newsletter too.
	 */
	public void setEMail(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, EMAIL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.eMail</code> attribute. 
	 * @param value the eMail - If true, the news will be send as newsletter too.
	 */
	public void setEMail(final Boolean value)
	{
		setEMail( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.eMail</code> attribute. 
	 * @param value the eMail - If true, the news will be send as newsletter too.
	 */
	public void setEMail(final SessionContext ctx, final boolean value)
	{
		setEMail( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>News.eMail</code> attribute. 
	 * @param value the eMail - If true, the news will be send as newsletter too.
	 */
	public void setEMail(final boolean value)
	{
		setEMail( getSession().getSessionContext(), value );
	}
	
}
