package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Locale;

import com.google.common.collect.Lists;


public class LanguageModelBuilder
{
	private final LanguageModel model;

	private LanguageModelBuilder()
	{
		model = new LanguageModel();
	}

	private LanguageModel getModel()
	{
		return this.model;
	}

	public static LanguageModelBuilder aModel()
	{
		return new LanguageModelBuilder();
	}

	public LanguageModel build()
	{
		return getModel();
	}

	public LanguageModelBuilder withIsocode(final String isocode)
	{
		getModel().setIsocode(isocode);
		return this;
	}

	public LanguageModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

	public LanguageModelBuilder withActive(final Boolean active)
	{
		getModel().setActive(active);
		return this;
	}

	public LanguageModelBuilder withBaseStores(final BaseStoreModel... baseStores)
	{
		getModel().setBaseStores(Lists.newArrayList(baseStores));
		return this;
	}

}
