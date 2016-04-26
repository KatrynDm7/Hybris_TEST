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
package de.hybris.platform.commons.model.translator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.model.translator.JaloTranslatorConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ParserProperty first defined at extension commons.
 */
@SuppressWarnings("all")
public class ParserPropertyModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ParserProperty";
	
	/**<i>Generated relation code constant for relation <code>TranslatorConfig2ParserProperties</code> defining source attribute <code>translatorConfiguration</code> in extension <code>commons</code>.</i>*/
	public final static String _TRANSLATORCONFIG2PARSERPROPERTIES = "TranslatorConfig2ParserProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.name</code> attribute defined at extension <code>commons</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.startExp</code> attribute defined at extension <code>commons</code>. */
	public static final String STARTEXP = "startExp";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.endExp</code> attribute defined at extension <code>commons</code>. */
	public static final String ENDEXP = "endExp";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.parserClass</code> attribute defined at extension <code>commons</code>. */
	public static final String PARSERCLASS = "parserClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. */
	public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";
	
	
	/** <i>Generated variable</i> - Variable of <code>ParserProperty.name</code> attribute defined at extension <code>commons</code>. */
	private String _name;
	
	/** <i>Generated variable</i> - Variable of <code>ParserProperty.startExp</code> attribute defined at extension <code>commons</code>. */
	private String _startExp;
	
	/** <i>Generated variable</i> - Variable of <code>ParserProperty.endExp</code> attribute defined at extension <code>commons</code>. */
	private String _endExp;
	
	/** <i>Generated variable</i> - Variable of <code>ParserProperty.parserClass</code> attribute defined at extension <code>commons</code>. */
	private String _parserClass;
	
	/** <i>Generated variable</i> - Variable of <code>ParserProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. */
	private JaloTranslatorConfigurationModel _translatorConfiguration;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ParserPropertyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ParserPropertyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>ParserProperty</code> at extension <code>commons</code>
	 * @param _startExp initial attribute declared by type <code>ParserProperty</code> at extension <code>commons</code>
	 */
	@Deprecated
	public ParserPropertyModel(final String _name, final String _startExp)
	{
		super();
		setName(_name);
		setStartExp(_startExp);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>ParserProperty</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _startExp initial attribute declared by type <code>ParserProperty</code> at extension <code>commons</code>
	 */
	@Deprecated
	public ParserPropertyModel(final String _name, final ItemModel _owner, final String _startExp)
	{
		super();
		setName(_name);
		setOwner(_owner);
		setStartExp(_startExp);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.endExp</code> attribute defined at extension <code>commons</code>. 
	 * @return the endExp - End expression of the tag/element
	 */
	@Accessor(qualifier = "endExp", type = Accessor.Type.GETTER)
	public String getEndExp()
	{
		if (this._endExp!=null)
		{
			return _endExp;
		}
		return _endExp = getPersistenceContext().getValue(ENDEXP, _endExp);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.name</code> attribute defined at extension <code>commons</code>. 
	 * @return the name - Name of the tag/element
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		if (this._name!=null)
		{
			return _name;
		}
		return _name = getPersistenceContext().getValue(NAME, _name);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.parserClass</code> attribute defined at extension <code>commons</code>. 
	 * @return the parserClass - Special java class that parses the related tag/element
	 */
	@Accessor(qualifier = "parserClass", type = Accessor.Type.GETTER)
	public String getParserClass()
	{
		if (this._parserClass!=null)
		{
			return _parserClass;
		}
		return _parserClass = getPersistenceContext().getValue(PARSERCLASS, _parserClass);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.startExp</code> attribute defined at extension <code>commons</code>. 
	 * @return the startExp - Start expression of the tag/element
	 */
	@Accessor(qualifier = "startExp", type = Accessor.Type.GETTER)
	public String getStartExp()
	{
		if (this._startExp!=null)
		{
			return _startExp;
		}
		return _startExp = getPersistenceContext().getValue(STARTEXP, _startExp);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
	 * @return the translatorConfiguration
	 */
	@Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.GETTER)
	public JaloTranslatorConfigurationModel getTranslatorConfiguration()
	{
		if (this._translatorConfiguration!=null)
		{
			return _translatorConfiguration;
		}
		return _translatorConfiguration = getPersistenceContext().getValue(TRANSLATORCONFIGURATION, _translatorConfiguration);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.endExp</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the endExp - End expression of the tag/element
	 */
	@Accessor(qualifier = "endExp", type = Accessor.Type.SETTER)
	public void setEndExp(final String value)
	{
		_endExp = getPersistenceContext().setValue(ENDEXP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.name</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the name - Name of the tag/element
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		_name = getPersistenceContext().setValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.parserClass</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the parserClass - Special java class that parses the related tag/element
	 */
	@Accessor(qualifier = "parserClass", type = Accessor.Type.SETTER)
	public void setParserClass(final String value)
	{
		_parserClass = getPersistenceContext().setValue(PARSERCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.startExp</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the startExp - Start expression of the tag/element
	 */
	@Accessor(qualifier = "startExp", type = Accessor.Type.SETTER)
	public void setStartExp(final String value)
	{
		_startExp = getPersistenceContext().setValue(STARTEXP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the translatorConfiguration
	 */
	@Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.SETTER)
	public void setTranslatorConfiguration(final JaloTranslatorConfigurationModel value)
	{
		_translatorConfiguration = getPersistenceContext().setValue(TRANSLATORCONFIGURATION, value);
	}
	
}
