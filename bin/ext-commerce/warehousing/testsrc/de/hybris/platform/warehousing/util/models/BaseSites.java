package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.site.dao.BaseSiteDao;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.warehousing.util.builder.BaseSiteModelBuilder;

import org.springframework.beans.factory.annotation.Required;


public class BaseSites extends AbstractItems<BaseSiteModel>
{
	public static final String UID_AMERICAS = "americas";

	private BaseSiteDao baseSiteDao;
	private BaseStores baseStores;

	public BaseSiteModel Americas()
	{
		return getOrSaveAndReturn(() -> getBaseSiteDao().findBaseSiteByUID(UID_AMERICAS), //
				() -> BaseSiteModelBuilder.aModel() //
						.withUid(UID_AMERICAS) //
						.withChannel(SiteChannel.B2C) //
						.withStores(getBaseStores().NorthAmerica()) //
						.build());
	}

	public BaseSiteDao getBaseSiteDao()
	{
		return baseSiteDao;
	}

	@Required
	public void setBaseSiteDao(final BaseSiteDao baseSiteDao)
	{
		this.baseSiteDao = baseSiteDao;
	}

	public BaseStores getBaseStores()
	{
		return baseStores;
	}

	@Required
	public void setBaseStores(final BaseStores baseStores)
	{
		this.baseStores = baseStores;
	}

}
