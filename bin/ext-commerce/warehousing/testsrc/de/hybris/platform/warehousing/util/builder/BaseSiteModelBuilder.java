package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.store.BaseStoreModel;

import com.google.common.collect.Lists;


public class BaseSiteModelBuilder
{
	private final BaseSiteModel model;

	private BaseSiteModelBuilder()
	{
		model = new BaseSiteModel();
	}

	private BaseSiteModel getModel()
	{
		return this.model;
	}

	public static BaseSiteModelBuilder aModel()
	{
		return new BaseSiteModelBuilder();
	}

	public BaseSiteModel build()
	{
		return getModel();
	}

	public BaseSiteModelBuilder withUid(final String uid)
	{
		getModel().setUid(uid);
		return this;
	}

	public BaseSiteModelBuilder withChannel(final SiteChannel channel)
	{
		getModel().setChannel(channel);
		return this;
	}

	public BaseSiteModelBuilder withStores(final BaseStoreModel... stores)
	{
		getModel().setStores(Lists.newArrayList(stores));
		return this;
	}

}
