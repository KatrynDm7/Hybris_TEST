package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.warehousing.util.builder.CatalogVersionModelBuilder;

import org.springframework.beans.factory.annotation.Required;


public class CatalogVersions extends AbstractItems<CatalogVersionModel>
{
	public static final String VERSION_STAGING = "staging";

	private CatalogVersionDao catalogVersionDao;
	private Catalogs catalogs;
	private Currencies currencies;

	public CatalogVersionModel Staging()
	{
		return getFromCollectionOrSaveAndReturn(
				() -> getCatalogVersionDao().findCatalogVersions(Catalogs.ID_PRIMARY, VERSION_STAGING), //
				() -> CatalogVersionModelBuilder.aModel() //
						.withCatalog(getCatalogs().Primary()) //
						.withDefaultCurrency(getCurrencies().AmericanDollar()) //
						.withActive(Boolean.TRUE) //
						.withVersion(VERSION_STAGING) //
						.build());
	}

	public CatalogVersionDao getCatalogVersionDao()
	{
		return catalogVersionDao;
	}

	@Required
	public void setCatalogVersionDao(final CatalogVersionDao catalogVersionDao)
	{
		this.catalogVersionDao = catalogVersionDao;
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
}
