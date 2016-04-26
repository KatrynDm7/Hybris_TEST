package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;


public class AddressModelBuilder
{
	private final AddressModel model;

	private AddressModelBuilder()
	{
		this.model = new AddressModel();
	}

	private AddressModelBuilder(final AddressModel model)
	{
		this.model = model;
	}

	private AddressModel getModel()
	{
		return this.model;
	}

	public static AddressModelBuilder aModel()
	{
		return new AddressModelBuilder();
	}

	public static AddressModelBuilder fromModel(final AddressModel model)
	{
		return new AddressModelBuilder();
	}

	public AddressModel build()
	{
		return getModel();
	}

	public AddressModelBuilder withStreetNumber(final String streetNumber)
	{
		getModel().setStreetnumber(streetNumber);
		return this;
	}

	public AddressModelBuilder withStreetName(final String streetName)
	{
		getModel().setStreetname(streetName);
		return this;
	}

	public AddressModelBuilder withFirstName(final String firstName)
	{
		getModel().setFirstname(firstName);
		return this;
	}

	public AddressModelBuilder withLastName(final String lastName)
	{
		getModel().setLastname(lastName);
		return this;
	}

	public AddressModelBuilder withTown(final String town)
	{
		getModel().setTown(town);
		return this;
	}

	public AddressModelBuilder withPostalCode(final String postalCode)
	{
		getModel().setPostalcode(postalCode);
		return this;
	}

	public AddressModelBuilder withDuplicate(final Boolean duplicate)
	{
		getModel().setDuplicate(duplicate);
		return this;
	}

	public AddressModelBuilder withCountry(final CountryModel country)
	{
		getModel().setCountry(country);
		return this;
	}

	public AddressModelBuilder withShippingAddress(final Boolean shippingAddress)
	{
		getModel().setShippingAddress(shippingAddress);
		return this;
	}

	public AddressModelBuilder withBillingAddress(final Boolean billingAddress)
	{
		getModel().setBillingAddress(billingAddress);
		return this;
	}

	public AddressModelBuilder withContactAddress(final Boolean contactAddress)
	{
		getModel().setContactAddress(contactAddress);
		return this;
	}

	public AddressModelBuilder withUnloadingAddress(final Boolean unloadingAddress)
	{
		getModel().setUnloadingAddress(unloadingAddress);
		return this;
	}

	public AddressModelBuilder withOwner(final ItemModel owner)
	{
		getModel().setOwner(owner);
		return this;
	}

	public AddressModelBuilder withLatitude(final Double latitude)
	{
		getModel().setLatitude(latitude);
		return this;
	}

	public AddressModelBuilder withLongitude(final Double longitude)
	{
		getModel().setLongitude(longitude);
		return this;
	}

}
