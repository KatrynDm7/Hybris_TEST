package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.ordersplitting.model.VendorModel;


public class VendorModelBuilder
{
	private final VendorModel model;

	private VendorModelBuilder()
	{
		model = new VendorModel();
	}

	private VendorModel getModel()
	{
		return this.model;
	}

	public static VendorModelBuilder aModel()
	{
		return new VendorModelBuilder();
	}

	public VendorModel build()
	{
		return getModel();
	}

	public VendorModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

}
