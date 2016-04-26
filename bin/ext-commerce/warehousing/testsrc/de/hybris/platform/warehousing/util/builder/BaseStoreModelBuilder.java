package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.store.BaseStoreModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


public class BaseStoreModelBuilder
{
	private final BaseStoreModel model;

	private BaseStoreModelBuilder()
	{
		model = new BaseStoreModel();
	}

	private BaseStoreModel getModel()
	{
		return this.model;
	}

	public static BaseStoreModelBuilder aModel()
	{
		return new BaseStoreModelBuilder();
	}

	public BaseStoreModel build()
	{
		return getModel();
	}

	public BaseStoreModelBuilder withUid(final String uid)
	{
		getModel().setUid(uid);
		return this;
	}

	public BaseStoreModelBuilder withCatalogs(final CatalogModel... catalogs)
	{
		getModel().setCatalogs(Lists.newArrayList(catalogs));
		return this;
	}

	public BaseStoreModelBuilder withCurrencies(final CurrencyModel... currencies)
	{
		getModel().setCurrencies(Sets.newHashSet(currencies));
		return this;
	}

	public BaseStoreModelBuilder withDeliveryCountries(final CountryModel... deliveryCountries)
	{
		getModel().setDeliveryCountries(Lists.newArrayList(deliveryCountries));
		return this;
	}

	public BaseStoreModelBuilder withLanguages(final LanguageModel... languages)
	{
		getModel().setLanguages(Sets.newHashSet(languages));
		return this;
	}

	public BaseStoreModelBuilder withDefaultCurrency(final CurrencyModel defaultCurrency)
	{
		getModel().setDefaultCurrency(defaultCurrency);
		return this;
	}

	public BaseStoreModelBuilder withDefaultLanguage(final LanguageModel defaultLanguage)
	{
		getModel().setDefaultLanguage(defaultLanguage);
		return this;
	}

	public BaseStoreModelBuilder withNet(final Boolean net)
	{
		getModel().setNet(net);
		return this;
	}

	public BaseStoreModelBuilder withSubmitOrderProcessCode(final String submitOrderProcessCode)
	{
		getModel().setSubmitOrderProcessCode(submitOrderProcessCode);
		return this;
	}

	public BaseStoreModelBuilder withPaymentProvider(final String paymentProvider)
	{
		getModel().setPaymentProvider(paymentProvider);
		return this;
	}

	public BaseStoreModelBuilder withDeliveryModes(final DeliveryModeModel... deliveryModes)
	{
		getModel().setDeliveryModes(Sets.newHashSet(deliveryModes));
		return this;
	}

}
