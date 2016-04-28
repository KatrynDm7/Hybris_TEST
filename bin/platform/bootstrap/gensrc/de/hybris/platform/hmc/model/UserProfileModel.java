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
package de.hybris.platform.hmc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type UserProfile first defined at extension core.
 */
@SuppressWarnings("all")
public class UserProfileModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "UserProfile";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserProfile.readableLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String READABLELANGUAGES = "readableLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserProfile.writableLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String WRITABLELANGUAGES = "writableLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserProfile.allReadableLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String ALLREADABLELANGUAGES = "allReadableLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserProfile.allWritableLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String ALLWRITABLELANGUAGES = "allWritableLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserProfile.expandInitial</code> attribute defined at extension <code>core</code>. */
	public static final String EXPANDINITIAL = "expandInitial";
	
	
	/** <i>Generated variable</i> - Variable of <code>UserProfile.readableLanguages</code> attribute defined at extension <code>core</code>. */
	private List<LanguageModel> _readableLanguages;
	
	/** <i>Generated variable</i> - Variable of <code>UserProfile.writableLanguages</code> attribute defined at extension <code>core</code>. */
	private List<LanguageModel> _writableLanguages;
	
	/** <i>Generated variable</i> - Variable of <code>UserProfile.allReadableLanguages</code> attribute defined at extension <code>core</code>. */
	private Collection<LanguageModel> _allReadableLanguages;
	
	/** <i>Generated variable</i> - Variable of <code>UserProfile.allWritableLanguages</code> attribute defined at extension <code>core</code>. */
	private Collection<LanguageModel> _allWritableLanguages;
	
	/** <i>Generated variable</i> - Variable of <code>UserProfile.expandInitial</code> attribute defined at extension <code>core</code>. */
	private Boolean _expandInitial;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public UserProfileModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public UserProfileModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>UserProfile</code> at extension <code>core</code>
	 */
	@Deprecated
	public UserProfileModel(final PrincipalModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserProfile.allReadableLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allReadableLanguages
	 */
	@Accessor(qualifier = "allReadableLanguages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getAllReadableLanguages()
	{
		if (this._allReadableLanguages!=null)
		{
			return _allReadableLanguages;
		}
		return _allReadableLanguages = getPersistenceContext().getValue(ALLREADABLELANGUAGES, _allReadableLanguages);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserProfile.allWritableLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allWritableLanguages
	 */
	@Accessor(qualifier = "allWritableLanguages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getAllWritableLanguages()
	{
		if (this._allWritableLanguages!=null)
		{
			return _allWritableLanguages;
		}
		return _allWritableLanguages = getPersistenceContext().getValue(ALLWRITABLELANGUAGES, _allWritableLanguages);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserProfile.expandInitial</code> attribute defined at extension <code>core</code>. 
	 * @return the expandInitial
	 */
	@Accessor(qualifier = "expandInitial", type = Accessor.Type.GETTER)
	public Boolean getExpandInitial()
	{
		if (this._expandInitial!=null)
		{
			return _expandInitial;
		}
		return _expandInitial = getPersistenceContext().getValue(EXPANDINITIAL, _expandInitial);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.owner</code> attribute defined at extension <code>core</code> and redeclared at extension <code>core</code>. 
	 * @return the owner
	 */
	@Override
	@Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
	public PrincipalModel getOwner()
	{
		return (PrincipalModel) super.getOwner();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserProfile.readableLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readableLanguages
	 */
	@Accessor(qualifier = "readableLanguages", type = Accessor.Type.GETTER)
	public List<LanguageModel> getReadableLanguages()
	{
		if (this._readableLanguages!=null)
		{
			return _readableLanguages;
		}
		return _readableLanguages = getPersistenceContext().getValue(READABLELANGUAGES, _readableLanguages);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserProfile.writableLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writableLanguages
	 */
	@Accessor(qualifier = "writableLanguages", type = Accessor.Type.GETTER)
	public List<LanguageModel> getWritableLanguages()
	{
		if (this._writableLanguages!=null)
		{
			return _writableLanguages;
		}
		return _writableLanguages = getPersistenceContext().getValue(WRITABLELANGUAGES, _writableLanguages);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserProfile.expandInitial</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the expandInitial
	 */
	@Accessor(qualifier = "expandInitial", type = Accessor.Type.SETTER)
	public void setExpandInitial(final Boolean value)
	{
		_expandInitial = getPersistenceContext().setValue(EXPANDINITIAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Item.owner</code> attribute defined at extension <code>core</code> and redeclared at extension <code>core</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.core.model.security.PrincipalModel}.  
	 *  
	 * @param value the owner
	 */
	@Override
	@Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
	public void setOwner(final ItemModel value)
	{
		super.setOwner(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserProfile.readableLanguages</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the readableLanguages
	 */
	@Accessor(qualifier = "readableLanguages", type = Accessor.Type.SETTER)
	public void setReadableLanguages(final List<LanguageModel> value)
	{
		_readableLanguages = getPersistenceContext().setValue(READABLELANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserProfile.writableLanguages</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the writableLanguages
	 */
	@Accessor(qualifier = "writableLanguages", type = Accessor.Type.SETTER)
	public void setWritableLanguages(final List<LanguageModel> value)
	{
		_writableLanguages = getPersistenceContext().setValue(WRITABLELANGUAGES, value);
	}
	
}
