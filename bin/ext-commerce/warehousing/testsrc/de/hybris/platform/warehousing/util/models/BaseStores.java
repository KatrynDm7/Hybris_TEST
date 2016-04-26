package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.daos.BaseStoreDao;
import de.hybris.platform.warehousing.util.builder.BaseStoreModelBuilder;

import org.springframework.beans.factory.annotation.Required;


public class BaseStores extends AbstractItems<BaseStoreModel>
{
	public static final String UID_NORTH_AMERICA = "north-america";

	private BaseStoreDao baseStoreDao;
	private Languages languages;
	private Catalogs catalogs;
	private Currencies currencies;
	private Countries countries;
	private DeliveryModes deliveryModes;

	public BaseStoreModel NorthAmerica()
	{
		return getFromCollectionOrSaveAndReturn(() -> getBaseStoreDao().findBaseStoresByUid(UID_NORTH_AMERICA), //
				() -> BaseStoreModelBuilder.aModel() //
						.withCatalogs(getCatalogs().Primary()) //
						.withCurrencies(getCurrencies().AmericanDollar()) //
						.withDefaultCurrency(getCurrencies().AmericanDollar()) //
						.withDefaultLanguage(getLanguages().English()) //
						.withDeliveryCountries(getCountries().UnitedStates(), getCountries().Canada()) //
						.withLanguages(getLanguages().English()) //
						.withNet(Boolean.FALSE) //
						.withPaymentProvider("Mockup") //
						.withSubmitOrderProcessCode("order-process") //
						.withUid(UID_NORTH_AMERICA) //
						.withDeliveryModes(getDeliveryModes().Pickup(), getDeliveryModes().Regular()) //
						.build());
	}

	public Languages getLanguages()
	{
		return languages;
	}

	@Required
	public void setLanguages(final Languages languages)
	{
		this.languages = languages;
	}

	public BaseStoreDao getBaseStoreDao()
	{
		return baseStoreDao;
	}

	@Required
	public void setBaseStoreDao(final BaseStoreDao baseStoreDao)
	{
		this.baseStoreDao = baseStoreDao;
	}

	public Catalogs getCatalogs()
	{
		return catalogs;
	}

	@Required
	public void setCatalogs(final Catalogs catalogs)
	{
		this.catalogs = catalogs;
	}

	public Currencies getCurrencies()
	{
		return currencies;
	}

	@Required
	public void setCurrencies(final Currencies currencies)
	{
		this.currencies = currencies;
	}

	public Countries getCountries()
	{
		return countries;
	}

	@Required
	public void setCountries(final Countries countries)
	{
		this.countries = countries;
	}

	public DeliveryModes getDeliveryModes()
	{
		return deliveryModes;
	}

	@Required
	public void setDeliveryModes(final DeliveryModes deliveryModes)
	{
		this.deliveryModes = deliveryModes;
	}
}
