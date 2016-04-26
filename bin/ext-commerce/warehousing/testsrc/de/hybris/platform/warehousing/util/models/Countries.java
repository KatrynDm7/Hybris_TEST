package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.daos.CountryDao;
import de.hybris.platform.warehousing.util.builder.CountryModelBuilder;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class Countries extends AbstractItems<CountryModel>
{
	public static final String ISOCODE_CANADA = "CA";
	public static final String ISOCODE_UNITED_STATES = "US";

	private CountryDao countryDao;

	public CountryModel Canada()
	{
		return getFromCollectionOrSaveAndReturn(() -> getCountryDao().findCountriesByCode(ISOCODE_CANADA), //
				() -> CountryModelBuilder.aModel() //
						.withIsoCode(ISOCODE_CANADA) //
						.withName("Canada", Locale.ENGLISH) //
						.withActive(Boolean.TRUE) //
						.build());
	}

	public CountryModel UnitedStates()
	{
		return getFromCollectionOrSaveAndReturn(() -> getCountryDao().findCountriesByCode(ISOCODE_UNITED_STATES), //
				() -> CountryModelBuilder.aModel() //
						.withIsoCode(ISOCODE_UNITED_STATES) //
						.withName("United States", Locale.ENGLISH) //
						.withActive(Boolean.TRUE) //
						.build());
	}

	public CountryDao getCountryDao()
	{
		return countryDao;
	}

	@Required
	public void setCountryDao(final CountryDao countryDao)
	{
		this.countryDao = countryDao;
	}

}
