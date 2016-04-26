package de.hybris.platform.warehousing.warehouse.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.daos.CountryDao;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.daos.BaseStoreDao;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.warehousing.constants.WarehousingTestConstants;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class WarehousingWarehouseDaoIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private CountryDao countryDao;

	@Resource
	private BaseStoreDao baseStoreDao;

	@Resource
	private PointOfServiceService pointOfServiceService;

	@Resource
	private AddressService addressService;

	@Resource
	private WarehousingWarehouseDao warehousingWarehouseDao;

	private BaseStoreModel baseStore;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/warehousing/test/impex/warehousedao-test-data.impex", WarehousingTestConstants.ENCODING);

		// verify data integrity
		final WarehouseModel nakano = warehousingWarehouseDao.getWarehouseForCode("Nakano").get(0);
		assertNotNull(nakano);

		baseStore = nakano.getBaseStores().iterator().next();
		assertEquals(2, baseStore.getDeliveryCountries().size());

		final List<CountryModel> countries = countryDao.findCountries();
		assertEquals(3, countries.size());
	}

	@Test
	public void testSuccessQueryForDeliveryCountryUS()
	{
		final CountryModel us = countryDao.findCountriesByCode("US").iterator().next();

		final Collection<WarehouseModel> locations = warehousingWarehouseDao.getWarehousesByBaseStoreDeliveryCountry(baseStore, us);

		assertNotNull(locations);
		assertEquals(4, locations.size());
	}

	@Test
	public void testSuccessQueryForDeliveryCountryCA()
	{
		final CountryModel ca = countryDao.findCountriesByCode("CA").iterator().next();

		final Collection<WarehouseModel> locations = warehousingWarehouseDao.getWarehousesByBaseStoreDeliveryCountry(baseStore, ca);

		assertNotNull(locations);
		assertEquals(4, locations.size());
	}

	@Test
	public void testSuccessNoResultQueryForDeliveryCountry()
	{
		final CountryModel mx = countryDao.findCountriesByCode("MX").iterator().next();

		final Collection<WarehouseModel> locations = warehousingWarehouseDao.getWarehousesByBaseStoreDeliveryCountry(baseStore, mx);

		assertNotNull(locations);
		assertTrue(locations.isEmpty());
	}

}
