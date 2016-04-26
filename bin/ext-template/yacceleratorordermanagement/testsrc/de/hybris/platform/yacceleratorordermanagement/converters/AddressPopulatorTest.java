package de.hybris.platform.yacceleratorordermanagement.converters;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.yacceleratorordermanagement.converters.AddressPopulator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

@UnitTest
public class AddressPopulatorTest
{
    AddressPopulator populator;

    @Before
    public void setup()
    {
        populator = new AddressPopulator();
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldFailIfSourceParamIsNull()
    {
        populator.populate(null, new AddressData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldFailIfTargetParamIsNull()
    {
        populator.populate(new AddressModel(), null);
    }

    @Test
    public void populateAllFields()
    {
        CountryModel unitedStates = new CountryModel();
        unitedStates.setIsocode("US");

        AddressModel broadwayNewYork = new AddressModel();
        broadwayNewYork.setCountry(unitedStates);
        broadwayNewYork.setDistrict("New York");
        broadwayNewYork.setStreetname("Broadway avenue");
        broadwayNewYork.setPostalcode("100001");

        AddressData addressData = new AddressData();

        populator.populate(broadwayNewYork, addressData);

        assertTrue("US".equals(addressData.getCountryCode()));
        assertTrue("New York".equals(addressData.getCity()));
        assertTrue("Broadway avenue".equals(addressData.getStreet()));
        assertTrue("100001".equals(addressData.getZip()));
    }

    @Test
    public void itShouldNotFailIfCountryCodeIsNotProvided()
    {
        AddressModel broadwayNewYork = new AddressModel();
        broadwayNewYork.setCountry(null);
        broadwayNewYork.setDistrict("New York");
        broadwayNewYork.setStreetname("Broadway avenue");
        broadwayNewYork.setPostalcode("100001");

        AddressData addressData = new AddressData();

        populator.populate(broadwayNewYork, addressData);

        assertTrue(null == addressData.getCountryCode());
        assertTrue("New York".equals(addressData.getCity()));
        assertTrue("Broadway avenue".equals(addressData.getStreet()));
        assertTrue("100001".equals(addressData.getZip()));
    }

}
