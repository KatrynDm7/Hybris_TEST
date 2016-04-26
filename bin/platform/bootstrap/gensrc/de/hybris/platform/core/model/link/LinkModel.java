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
package de.hybris.platform.core.model.link;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type Link first defined at extension core.
 */
@SuppressWarnings("all")
public class LinkModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Link";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.language</code> attribute defined at extension <code>core</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.qualifier</code> attribute defined at extension <code>core</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.source</code> attribute defined at extension <code>core</code>. */
	public static final String SOURCE = "source";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.target</code> attribute defined at extension <code>core</code>. */
	public static final String TARGET = "target";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.sequenceNumber</code> attribute defined at extension <code>core</code>. */
	public static final String SEQUENCENUMBER = "sequenceNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.reverseSequenceNumber</code> attribute defined at extension <code>core</code>. */
	public static final String REVERSESEQUENCENUMBER = "reverseSequenceNumber";
	
	
	/** <i>Generated variable</i> - Variable of <code>Link.language</code> attribute defined at extension <code>core</code>. */
	private LanguageModel _language;
	
	/** <i>Generated variable</i> - Variable of <code>Link.qualifier</code> attribute defined at extension <code>core</code>. */
	private String _qualifier;
	
	/** <i>Generated variable</i> - Variable of <code>Link.source</code> attribute defined at extension <code>core</code>. */
	private ItemModel _source;
	
	/** <i>Generated variable</i> - Variable of <code>Link.target</code> attribute defined at extension <code>core</code>. */
	private ItemModel _target;
	
	/** <i>Generated variable</i> - Variable of <code>Link.sequenceNumber</code> attribute defined at extension <code>core</code>. */
	private Integer _sequenceNumber;
	
	/** <i>Generated variable</i> - Variable of <code>Link.reverseSequenceNumber</code> attribute defined at extension <code>core</code>. */
	private Integer _reverseSequenceNumber;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public LinkModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public LinkModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _source initial attribute declared by type <code>Link</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>Link</code> at extension <code>core</code>
	 */
	@Deprecated
	public LinkModel(final ItemModel _source, final ItemModel _target)
	{
		super();
		setSource(_source);
		setTarget(_target);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _source initial attribute declared by type <code>Link</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>Link</code> at extension <code>core</code>
	 */
	@Deprecated
	public LinkModel(final ItemModel _owner, final ItemModel _source, final ItemModel _target)
	{
		super();
		setOwner(_owner);
		setSource(_source);
		setTarget(_target);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.language</code> attribute defined at extension <code>core</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		if (this._language!=null)
		{
			return _language;
		}
		return _language = getPersistenceContext().getValue(LANGUAGE, _language);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.qualifier</code> attribute defined at extension <code>core</code>. 
	 * @return the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		if (this._qualifier!=null)
		{
			return _qualifier;
		}
		return _qualifier = getPersistenceContext().getValue(QUALIFIER, _qualifier);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.reverseSequenceNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the reverseSequenceNumber
	 */
	@Accessor(qualifier = "reverseSequenceNumber", type = Accessor.Type.GETTER)
	public Integer getReverseSequenceNumber()
	{
		if (this._reverseSequenceNumber!=null)
		{
			return _reverseSequenceNumber;
		}
		return _reverseSequenceNumber = getPersistenceContext().getValue(REVERSESEQUENCENUMBER, _reverseSequenceNumber);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.sequenceNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
	public Integer getSequenceNumber()
	{
		if (this._sequenceNumber!=null)
		{
			return _sequenceNumber;
		}
		return _sequenceNumber = getPersistenceContext().getValue(SEQUENCENUMBER, _sequenceNumber);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.source</code> attribute defined at extension <code>core</code>. 
	 * @return the source
	 */
	@Accessor(qualifier = "source", type = Accessor.Type.GETTER)
	public ItemModel getSource()
	{
		if (this._source!=null)
		{
			return _source;
		}
		return _source = getPersistenceContext().getValue(SOURCE, _source);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.target</code> attribute defined at extension <code>core</code>. 
	 * @return the target
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.GETTER)
	public ItemModel getTarget()
	{
		if (this._target!=null)
		{
			return _target;
		}
		return _target = getPersistenceContext().getValue(TARGET, _target);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.language</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		_language = getPersistenceContext().setValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.qualifier</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		_qualifier = getPersistenceContext().setValue(QUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.reverseSequenceNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the reverseSequenceNumber
	 */
	@Accessor(qualifier = "reverseSequenceNumber", type = Accessor.Type.SETTER)
	public void setReverseSequenceNumber(final Integer value)
	{
		_reverseSequenceNumber = getPersistenceContext().setValue(REVERSESEQUENCENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.sequenceNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
	public void setSequenceNumber(final Integer value)
	{
		_sequenceNumber = getPersistenceContext().setValue(SEQUENCENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.source</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the source
	 */
	@Accessor(qualifier = "source", type = Accessor.Type.SETTER)
	public void setSource(final ItemModel value)
	{
		_source = getPersistenceContext().setValue(SOURCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.target</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the target
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.SETTER)
	public void setTarget(final ItemModel value)
	{
		_target = getPersistenceContext().setValue(TARGET, value);
	}
	
}
