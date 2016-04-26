/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.c4ccustomer.deltadetection;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.systemsetup.datacreator.impl.EncodingsDataCreator;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.model.cronjob.ImpExExportJobModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Test that delta detection is set up correctly.
 */
@IntegrationTest
public class CustomerDeltaTest extends ServicelayerTest
{
	private final static Logger LOG = Logger.getLogger(CustomerDeltaTest.class);

	@Resource
	private ModelService modelService;
	@Resource
	private ChangeDetectionService changeDetectionService;
	@Resource
	private TypeService typeService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void before() throws Exception
	{
		new EncodingsDataCreator().populateDatabase();
		ServicelayerTest.createCoreData();
		de.hybris.deltadetection.jalo.DeltadetectionManager.getInstance().createEssentialData(Collections.emptyMap(), null);
		de.hybris.y2ysync.jalo.Y2ysyncManager.getInstance().createEssentialData(Collections.emptyMap(), null);
		importCsv("/c4ccustomer/import/projectdata-c4cdata-streams.impex", "UTF-8");
		final ImpExExportJobModel exportJob = modelService.create(ImpExExportJobModel.class);
		exportJob.setCode(ImpExConstants.CronJob.DEFAULT_EXPORT_JOB_CODE);
		modelService.save(exportJob);
	}

	@Test
	public void shouldLogCustomerCreation()
	{
		// GIVEN a customer is registered
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		customer.setName("John Doe");
		modelService.save(customer);
		// WHEN we query delta detection service for changed customer
		final ItemChangeDTO change = changeDetectionService.getChangeForExistingItem(customer, UUID.randomUUID().toString());
		// THEN it should return the record of the creation fact
		assertEquals("Incorrect type of change", ChangeType.NEW, change.getChangeType());
		assertEquals("Incorrect item reference", customer.getPk().getLong(), change.getItemPK());
	}

	@Test
	public void shouldLogAddressCreation()
	{
		// GIVEN an address is registered for a customer
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		customer.setName("John Doe");
		modelService.save(customer);
		final AddressModel address = new AddressModel();
		address.setOwner(customer);
		address.setShippingAddress(Boolean.TRUE);
		modelService.save(address);
		// WHEN we query delta detection service for changed addresses
		final ItemChangeDTO change = changeDetectionService.getChangeForExistingItem(address, UUID.randomUUID().toString());
		// THEN it should return the record of the creation fact
		assertEquals("Incorrect type of change", ChangeType.NEW, change.getChangeType());
		assertEquals("Incorrect item reference", address.getPk().getLong(), change.getItemPK());
	}

	@Test
	public void shouldLogCustomerRemoval()
	{
		final String streamId = "c4cCustomerStream";
		// GIVEN a customer is registered and then deleted
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		customer.setName("John Doe");
		modelService.save(customer);
		// Store current customer version in stream
		changeDetectionService.consumeChanges(Collections.singletonList(changeDetectionService.getChangeForExistingItem(customer, streamId)));
		modelService.remove(customer);
		// WHEN we get the status of the customer
		final ItemChangeDTO change = changeDetectionService.getChangeForRemovedItem(customer.getPk(), streamId);
		// THEN we should have 'DELETED' change
		assertNotNull("Null change record", change);
		assertEquals("Incorrect delta type", ChangeType.DELETED, change.getChangeType());

		// WHEN we query delta detection service for changed customers
		changeDetectionService.collectChangesForType(typeService.getComposedTypeForClass(CustomerModel.class),
				streamId,
				new ChangesCollector()
				{
					private ItemChangeDTO item = null;

					@Override
					public boolean collect(final ItemChangeDTO itemChangeDTO)
					{
						if (item == null && itemChangeDTO.getItemPK().equals(customer.getPk().getLong()))
						{
							item = itemChangeDTO;
						}
						return true;
					}

					@Override
					public void finish()
					{
						// THEN we should get the customer we've removed
						assertNotNull("Customer removal has not been logged", item);
						assertEquals("Incorrect delta type", ChangeType.DELETED, item.getChangeType());
					}
				});
	}

	@Test
	public void shouldWatchCustomerAddressesOnly()
	{
		// GIVEN There are two addresses: one of them belongs to customer, while second does not.
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		customer.setName("John Doe");
		modelService.save(customer);
		final AddressModel customerAddress = new AddressModel();
		customerAddress.setOwner(customer);
		customerAddress.setShippingAddress(Boolean.TRUE);
		modelService.save(customerAddress);
		final AddressModel orphanAddress = new AddressModel();
		orphanAddress.setOwner(customerAddress);
		modelService.save(orphanAddress);
		// WHEN we collect changes
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class),
				getStreamConfiguration("c4cAddressStream"),
				new InMemoryChangesCollector()
				{
					@Override
					public void finish()
					{
						// THEN We should get only the customer's address
						assertThat(getChanges(), not(contains(hasProperty("itemPK", is(orphanAddress.getPk().getLongValue())))));
						assertThat(getChanges(), contains(hasProperty("itemPK", is(customerAddress.getPk().getLongValue()))));
					}
				});
	}

	private StreamConfiguration getStreamConfiguration(final String configCode)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Y2YStreamConfiguration} WHERE {streamId}=?streamId");
		fQuery.addQueryParameter("streamId", configCode);
		final StreamConfigurationModel model = flexibleSearchService.searchUnique(fQuery);
		return StreamConfiguration
				.buildFor(model.getStreamId())
				.withItemSelector(model.getWhereClause())
				.withExcludedTypeCodes(model.getExcludedTypes());
	}
}
