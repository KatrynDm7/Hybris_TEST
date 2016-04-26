package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;

import com.google.common.collect.Sets;


public class ZoneModelBuilder
{
	private final ZoneModel model;

	private ZoneModelBuilder()
	{
		model = new ZoneModel();
	}

	private ZoneModel getModel()
	{
		return this.model;
	}

	public static ZoneModelBuilder aModel()
	{
		return new ZoneModelBuilder();
	}

	public ZoneModel build()
	{
		return getModel();
	}

	public ZoneModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public ZoneModelBuilder withCountries(final CountryModel... countries)
	{
		getModel().setCountries(Sets.newHashSet(countries));
		return this;
	}

}
