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
package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type Language first defined at extension core.
 */
@SuppressWarnings("all")
public class LanguageModel extends C2LItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Language";
	
	/** <i>Generated constant</i> - Attribute key of <code>Language.fallbackLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String FALLBACKLANGUAGES = "fallbackLanguages";
	
	
	/** <i>Generated variable</i> - Variable of <code>Language.fallbackLanguages</code> attribute defined at extension <code>core</code>. */
	private List<LanguageModel> _fallbackLanguages;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public LanguageModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public LanguageModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Language</code> at extension <code>core</code>
	 */
	@Deprecated
	public LanguageModel(final String _isocode)
	{
		super();
		setIsocode(_isocode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Language</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public LanguageModel(final String _isocode, final ItemModel _owner)
	{
		super();
		setIsocode(_isocode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Language.fallbackLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the fallbackLanguages
	 */
	@Accessor(qualifier = "fallbackLanguages", type = Accessor.Type.GETTER)
	public List<LanguageModel> getFallbackLanguages()
	{
		if (this._fallbackLanguages!=null)
		{
			return _fallbackLanguages;
		}
		return _fallbackLanguages = getPersistenceContext().getValue(FALLBACKLANGUAGES, _fallbackLanguages);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Language.fallbackLanguages</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the fallbackLanguages
	 */
	@Accessor(qualifier = "fallbackLanguages", type = Accessor.Type.SETTER)
	public void setFallbackLanguages(final List<LanguageModel> value)
	{
		_fallbackLanguages = getPersistenceContext().setValue(FALLBACKLANGUAGES, value);
	}
	
}
