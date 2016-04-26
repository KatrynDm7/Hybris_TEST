package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.catalog.daos.CatalogDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.warehousing.util.builder.CatalogModelBuilder;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class Catalogs extends AbstractItems<CatalogModel>
{
	public static final String ID_PRIMARY = "primary";

	private CatalogDao catalogDao;

	public CatalogModel Primary()
	{
		return getOrSaveAndReturn(() -> getCatalogDao().findCatalogById(ID_PRIMARY), //
				() -> CatalogModelBuilder.aModel() //
						.withDefaultCatalog(Boolean.TRUE) //
						.withId(ID_PRIMARY) //
						.withName(ID_PRIMARY, Locale.ENGLISH) //
						.build());
	}

	public CatalogDao getCatalogDao()
	{
		return catalogDao;
	}

	@Required
	public void setCatalogDao(final CatalogDao catalogDao)
	{
		this.catalogDao = catalogDao;
	}
}
