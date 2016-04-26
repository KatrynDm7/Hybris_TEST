package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.warehousing.util.builder.CurrencyModelBuilder;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class Currencies extends AbstractItems<CurrencyModel>
{
	public static final String ISOCODE_USD = "USD";
	public static final String SYMBOL_DOLLAR = "$";

	private CurrencyDao currencyDao;

	public CurrencyModel AmericanDollar()
	{
		return getFromCollectionOrSaveAndReturn(() -> getCurrencyDao().findCurrenciesByCode(ISOCODE_USD), //
				() -> CurrencyModelBuilder.aModel() //
						.withIsoCode(ISOCODE_USD) //
						.withName("US Dollar", Locale.ENGLISH) //
						.withActive(Boolean.TRUE) //
						.withConversion(Double.valueOf(1)) //
						.withDigits(Integer.valueOf(2)) //
						.withSymbol(SYMBOL_DOLLAR) //
						.build());
	}

	public CurrencyDao getCurrencyDao()
	{
		return currencyDao;
	}

	@Required
	public void setCurrencyDao(final CurrencyDao currencyDao)
	{
		this.currencyDao = currencyDao;
	}
}
