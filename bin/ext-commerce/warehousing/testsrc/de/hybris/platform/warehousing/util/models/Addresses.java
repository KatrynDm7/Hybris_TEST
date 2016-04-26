package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.user.daos.AddressDao;
import de.hybris.platform.warehousing.util.builder.AddressModelBuilder;

import org.springframework.beans.factory.annotation.Required;


public class Addresses extends AbstractItems<AddressModel>
{
	private AddressDao addressDao;
	private Countries countries;
	private Users users;

	public AddressModel MontrealDeMaisonneuvePos()
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getAddressDao().findAddressesForOwner(getUsers().ManagerMontrealMaisonneuve()), //
				() -> AddressModelBuilder.aModel() //
						.withStreetNumber("999") //
						.withStreetName("De Maisonneuve") //
						.withTown("Montreal") //
						.withPostalCode("H3A 3L4") //
						.withCountry(getCountries().Canada()) //
						.withDuplicate(Boolean.FALSE) //
						.withBillingAddress(Boolean.FALSE) //
						.withContactAddress(Boolean.FALSE) //
						.withUnloadingAddress(Boolean.FALSE) //
						.withShippingAddress(Boolean.TRUE) //
						.withOwner(getUsers().ManagerMontrealMaisonneuve()) //
						.withLatitude(Double.valueOf(45.5016330)) //
						.withLongitude(Double.valueOf(-73.5740030)) //
						.build());
	}

	public AddressModel MontrealDukePos()
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getAddressDao().findAddressesForOwner(getUsers().ManagerMontrealDuke()), //
				() -> AddressModelBuilder.aModel() //
						.withStreetNumber("111") //
						.withStreetName("Duke") //
						.withTown("Montreal") //
						.withPostalCode("H3C 2M1") //
						.withCountry(getCountries().Canada()) //
						.withDuplicate(Boolean.FALSE) //
						.withBillingAddress(Boolean.FALSE) //
						.withContactAddress(Boolean.FALSE) //
						.withUnloadingAddress(Boolean.FALSE) //
						.withShippingAddress(Boolean.TRUE) //
						.withOwner(getUsers().ManagerMontrealDuke()) //
						.build());
	}

	public AddressModel MontrealNancyHome()
	{
		return getFromCollectionOrSaveAndReturn(() -> getAddressDao().findAddressesForOwner(getUsers().Nancy()), //
				() -> AddressModelBuilder.fromModel(ShippingAddress()) //
						.withStreetNumber("705") //
						.withStreetName("Ste-Catherine") //
						.withTown("Montreal") //
						.withPostalCode("H3B 4G5") //
						.withCountry(getCountries().Canada()) //
						.withOwner(getUsers().Nancy()) //
						.withLatitude(Double.valueOf(45.5027940)) //
						.withLongitude(Double.valueOf(-73.5714720)) //
						.build());
	}

	public AddressModel Boston()
	{
		return getFromCollectionOrSaveAndReturn(() -> getAddressDao().findAddressesForOwner(getUsers().Bob()), //
				() -> AddressModelBuilder.fromModel(ShippingAddress()) //
						.withStreetNumber("33-41") //
						.withStreetName("Farnsworth") //
						.withTown("Boston") //
						.withPostalCode("02210") //
						.withCountry(getCountries().UnitedStates()) //
						.withDuplicate(Boolean.FALSE) //
						.withBillingAddress(Boolean.TRUE) //
						.withContactAddress(Boolean.FALSE) //
						.withUnloadingAddress(Boolean.FALSE) //
						.withShippingAddress(Boolean.TRUE) //
						.withOwner(getUsers().Bob()) //
						.withLatitude(Double.valueOf(42.3519410)) //
						.withLongitude(Double.valueOf(-71.0478470)) //
						.build());
	}

	protected AddressModel ShippingAddress()
	{
		return AddressModelBuilder.aModel() //
				.withDuplicate(Boolean.FALSE) //
				.withBillingAddress(Boolean.FALSE) //
				.withContactAddress(Boolean.FALSE) //
				.withUnloadingAddress(Boolean.FALSE) //
				.withShippingAddress(Boolean.TRUE) //
				.build();
	}

	public AddressDao getAddressDao()
	{
		return addressDao;
	}

	@Required
	public void setAddressDao(final AddressDao addressDao)
	{
		this.addressDao = addressDao;
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

	public Users getUsers()
	{
		return users;
	}

	@Required
	public void setUsers(final Users users)
	{
		this.users = users;
	}

}
