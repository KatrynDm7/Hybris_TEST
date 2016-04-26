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
package de.hybris.platform.mcc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mcc.enums.CockpitLinkArea;
import de.hybris.platform.mcc.model.AbstractLinkEntryModel;
import de.hybris.platform.mcc.model.StaticLinkModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type StaticLink first defined at extension mcc.
 */
@SuppressWarnings("all")
public class StaticLinkModel extends AbstractLinkEntryModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "StaticLink";
	
	/**<i>Generated relation code constant for relation <code>StaticLink2StaticLinkRelation</code> defining source attribute <code>parentLink</code> in extension <code>mcc</code>.</i>*/
	public final static String _STATICLINK2STATICLINKRELATION = "StaticLink2StaticLinkRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>StaticLink.title</code> attribute defined at extension <code>mcc</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>StaticLink.descriptionIcon</code> attribute defined at extension <code>mcc</code>. */
	public static final String DESCRIPTIONICON = "descriptionIcon";
	
	/** <i>Generated constant</i> - Attribute key of <code>StaticLink.description</code> attribute defined at extension <code>mcc</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>StaticLink.url</code> attribute defined at extension <code>mcc</code>. */
	public static final String URL = "url";
	
	/** <i>Generated constant</i> - Attribute key of <code>StaticLink.extensionName</code> attribute defined at extension <code>mcc</code>. */
	public static final String EXTENSIONNAME = "extensionName";
	
	/** <i>Generated constant</i> - Attribute key of <code>StaticLink.parentLink</code> attribute defined at extension <code>mcc</code>. */
	public static final String PARENTLINK = "parentLink";
	
	/** <i>Generated constant</i> - Attribute key of <code>StaticLink.sublinks</code> attribute defined at extension <code>mcc</code>. */
	public static final String SUBLINKS = "sublinks";
	
	
	/** <i>Generated variable</i> - Variable of <code>StaticLink.descriptionIcon</code> attribute defined at extension <code>mcc</code>. */
	private MediaModel _descriptionIcon;
	
	/** <i>Generated variable</i> - Variable of <code>StaticLink.url</code> attribute defined at extension <code>mcc</code>. */
	private String _url;
	
	/** <i>Generated variable</i> - Variable of <code>StaticLink.extensionName</code> attribute defined at extension <code>mcc</code>. */
	private String _extensionName;
	
	/** <i>Generated variable</i> - Variable of <code>StaticLink.parentLink</code> attribute defined at extension <code>mcc</code>. */
	private StaticLinkModel _parentLink;
	
	/** <i>Generated variable</i> - Variable of <code>StaticLink.sublinks</code> attribute defined at extension <code>mcc</code>. */
	private List<StaticLinkModel> _sublinks;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StaticLinkModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StaticLinkModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _area initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _code initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 */
	@Deprecated
	public StaticLinkModel(final CockpitLinkArea _area, final String _code)
	{
		super();
		setArea(_area);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _area initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _code initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public StaticLinkModel(final CockpitLinkArea _area, final String _code, final ItemModel _owner)
	{
		super();
		setArea(_area);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StaticLink.description</code> attribute defined at extension <code>mcc</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>StaticLink.description</code> attribute defined at extension <code>mcc</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StaticLink.descriptionIcon</code> attribute defined at extension <code>mcc</code>. 
	 * @return the descriptionIcon
	 */
	@Accessor(qualifier = "descriptionIcon", type = Accessor.Type.GETTER)
	public MediaModel getDescriptionIcon()
	{
		if (this._descriptionIcon!=null)
		{
			return _descriptionIcon;
		}
		return _descriptionIcon = getPersistenceContext().getValue(DESCRIPTIONICON, _descriptionIcon);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StaticLink.extensionName</code> attribute defined at extension <code>mcc</code>. 
	 * @return the extensionName
	 */
	@Accessor(qualifier = "extensionName", type = Accessor.Type.GETTER)
	public String getExtensionName()
	{
		if (this._extensionName!=null)
		{
			return _extensionName;
		}
		return _extensionName = getPersistenceContext().getValue(EXTENSIONNAME, _extensionName);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StaticLink.parentLink</code> attribute defined at extension <code>mcc</code>. 
	 * @return the parentLink
	 */
	@Accessor(qualifier = "parentLink", type = Accessor.Type.GETTER)
	public StaticLinkModel getParentLink()
	{
		if (this._parentLink!=null)
		{
			return _parentLink;
		}
		return _parentLink = getPersistenceContext().getValue(PARENTLINK, _parentLink);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StaticLink.sublinks</code> attribute defined at extension <code>mcc</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the sublinks
	 */
	@Accessor(qualifier = "sublinks", type = Accessor.Type.GETTER)
	public List<StaticLinkModel> getSublinks()
	{
		if (this._sublinks!=null)
		{
			return _sublinks;
		}
		return _sublinks = getPersistenceContext().getValue(SUBLINKS, _sublinks);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StaticLink.title</code> attribute defined at extension <code>mcc</code>. 
	 * @return the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>StaticLink.title</code> attribute defined at extension <code>mcc</code>. 
	 * @param loc the value localization key 
	 * @return the title
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StaticLink.url</code> attribute defined at extension <code>mcc</code>. 
	 * @return the url - If no url is provided, render link as text
	 */
	@Accessor(qualifier = "url", type = Accessor.Type.GETTER)
	public String getUrl()
	{
		if (this._url!=null)
		{
			return _url;
		}
		return _url = getPersistenceContext().getValue(URL, _url);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StaticLink.description</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>StaticLink.description</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StaticLink.descriptionIcon</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the descriptionIcon
	 */
	@Accessor(qualifier = "descriptionIcon", type = Accessor.Type.SETTER)
	public void setDescriptionIcon(final MediaModel value)
	{
		_descriptionIcon = getPersistenceContext().setValue(DESCRIPTIONICON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StaticLink.extensionName</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the extensionName
	 */
	@Accessor(qualifier = "extensionName", type = Accessor.Type.SETTER)
	public void setExtensionName(final String value)
	{
		_extensionName = getPersistenceContext().setValue(EXTENSIONNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StaticLink.parentLink</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the parentLink
	 */
	@Accessor(qualifier = "parentLink", type = Accessor.Type.SETTER)
	public void setParentLink(final StaticLinkModel value)
	{
		_parentLink = getPersistenceContext().setValue(PARENTLINK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StaticLink.sublinks</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the sublinks
	 */
	@Accessor(qualifier = "sublinks", type = Accessor.Type.SETTER)
	public void setSublinks(final List<StaticLinkModel> value)
	{
		_sublinks = getPersistenceContext().setValue(SUBLINKS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StaticLink.title</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>StaticLink.title</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the title
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StaticLink.url</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the url - If no url is provided, render link as text
	 */
	@Accessor(qualifier = "url", type = Accessor.Type.SETTER)
	public void setUrl(final String value)
	{
		_url = getPersistenceContext().setValue(URL, value);
	}
	
}
