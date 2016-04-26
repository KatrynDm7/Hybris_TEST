/*
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
 *  
 */
package de.hybris.platform.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;

import java.util.HashMap;
import java.util.Locale;

import org.junit.Before;
import org.mockito.Mock;


public abstract class AbstractLocalizedValueResolverTest extends AbstractValueResolverTest
{
	protected static final String EN_LANGUAGE_NAME = "English";
	protected static final String EN_LANGUAGE_ISOCODE = "en";
	protected static final String EN_LANGUAGE_QUALIFIER = "en";

	protected static final String DE_LANGUAGE_NAME = "German";
	protected static final String DE_LANGUAGE_ISOCODE = "de";
	protected static final String DE_LANGUAGE_QUALIFIER = "de";

	protected static final String LOCALIZED_INDEXED_PROPERTY_NAME = "localizedIndexedProperty";

	@Mock
	private LanguageModel enLanguage;

	@Mock
	private LanguageModel deLanguage;

	@Mock
	private Qualifier enQualifier;

	@Mock
	private Qualifier deQualifier;

	private IndexedProperty localizedIndexedProperty;

	protected LanguageModel getEnLanguage()
	{
		return enLanguage;
	}

	protected LanguageModel getDeLanguage()
	{
		return deLanguage;
	}

	protected Qualifier getEnQualifier()
	{
		return enQualifier;
	}

	protected Qualifier getDeQualifier()
	{
		return deQualifier;
	}

	protected IndexedProperty getLocalizedIndexedProperty()
	{
		return localizedIndexedProperty;
	}

	@Before
	public void setUpAbstractLocalizedValueResolverTest()
	{
		localizedIndexedProperty = new IndexedProperty();
		localizedIndexedProperty.setName(LOCALIZED_INDEXED_PROPERTY_NAME);
		localizedIndexedProperty.setValueProviderParameters(new HashMap<String, String>());
		localizedIndexedProperty.setLocalized(true);

		when(enLanguage.getName()).thenReturn(EN_LANGUAGE_NAME);
		when(enLanguage.getIsocode()).thenReturn(EN_LANGUAGE_ISOCODE);
		when(deLanguage.getName()).thenReturn(DE_LANGUAGE_NAME);
		when(deLanguage.getIsocode()).thenReturn(DE_LANGUAGE_ISOCODE);

		when(enQualifier.toFieldQualifier()).thenReturn(EN_LANGUAGE_QUALIFIER);
		when(enQualifier.getValueForType(LanguageModel.class)).thenReturn(enLanguage);
		when(enQualifier.getValueForType(Locale.class)).thenReturn(Locale.ENGLISH);
		when(deQualifier.toFieldQualifier()).thenReturn(DE_LANGUAGE_QUALIFIER);
		when(deQualifier.getValueForType(LanguageModel.class)).thenReturn(deLanguage);
		when(deQualifier.getValueForType(Locale.class)).thenReturn(Locale.GERMAN);

		when(Boolean.valueOf(getQualifierProvider().canApply(getIndexedProperty()))).thenReturn(Boolean.FALSE);
		when(Boolean.valueOf(getQualifierProvider().canApply(localizedIndexedProperty))).thenReturn(Boolean.TRUE);
	}
}
