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
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.systemsetup.datacreator.impl.EncodingsDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.deltadetection.collector.BatchingCollector;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@IntegrationTest
public class DefaultC4CAggregatingCollectorTest extends ServicelayerTest
{
	private final static Logger LOG = Logger.getLogger(DefaultC4CAggregatingCollectorTest.class);
	private ApplicationContext applicationContext;

	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private ChangeDetectionService changeDetectionService;

	@Before
	public void setup() throws Exception
	{
		applicationContext = Registry.getApplicationContext();
		new EncodingsDataCreator().populateDatabase();
		ServicelayerTest.createCoreData();
	}

	@Test
	public void shouldConsumeEverything()
	{
		final String streamId = UUID.randomUUID().toString();
		final List<ItemChangeDTO> receiver = new ArrayList<>();
		C4CAggregatingCollector collector = getDeltaCollector(receiver);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);
		changeDetectionService.consumeChanges(receiver);
		dump(receiver);
		receiver.clear();
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);
		dump(receiver);
		assertThat(receiver, emptyIterable());
	}

	@Test
	public void whenOneOfAddressesIsDeleted()
	{
		final String streamId = UUID.randomUUID().toString();
		final List<ItemChangeDTO> receiver = new ArrayList<>();
		C4CAggregatingCollector collector = getDeltaCollector(receiver);
		// Given we have a customer and related some addresses
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		modelService.save(customer);
		final AddressModel address1 = new AddressModel();
		address1.setOwner(customer);
		address1.setLine1("line 1");
		modelService.save(address1);
		final AddressModel address2 = new AddressModel();
		address2.setOwner(customer);
		address2.setLine1("line 1");
		modelService.save(address2);
		receiver.clear();
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);
		changeDetectionService.consumeChanges(receiver);

		// When we delete one of the addresses
		modelService.remove(address2);

		// And grab changes again
		receiver.clear();
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);

		// Then we should get the owner of the deleted address and all remaining addresses
		assertThat(receiver, hasItem(hasProperty("itemPK", is(customer.getPk().getLongValue()))));
		assertThat(receiver, hasItem(hasProperty("itemPK", is(address1.getPk().getLongValue()))));
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(address2.getPk().getLongValue())))));
	}

	@Test
	public void whenTheOnlyAddressIsDeleted()
	{
		// Given we have a customer with an address
		final String streamId = UUID.randomUUID().toString();
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		modelService.save(customer);
		final AddressModel address = new AddressModel();
		address.setOwner(customer);
		address.setLine1("line 1");
		modelService.save(address);

		// and these records have been synchronized
		final List<ItemChangeDTO> receiver = new ArrayList<>();
		C4CAggregatingCollector collector = getDeltaCollector(receiver);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);
		assertThat(receiver, hasItem(hasProperty("itemPK", is(customer.getPk().getLongValue()))));
		assertThat(receiver, hasItem(hasProperty("itemPK", is(address.getPk().getLongValue()))));
		changeDetectionService.consumeChanges(receiver);

		// When we delete the address
		modelService.remove(address);

		// Then it's owner should be sent
		receiver.clear();
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);
		assertThat(receiver, hasItem(hasProperty("itemPK", is(customer.getPk().getLongValue()))));
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(address.getPk().getLongValue())))));
		changeDetectionService.consumeChanges(receiver);

		// only one time
		receiver.clear();
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(customer.getPk().getLongValue())))));
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(address.getPk().getLongValue())))));
	}

	@Test
	public void whenCustomerIsDeleted()
	{
		// Given we have a customer and related some addresses
		final String streamId = UUID.randomUUID().toString();
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		modelService.save(customer);
		final AddressModel address1 = new AddressModel();
		address1.setOwner(customer);
		address1.setLine1("line 1");
		modelService.save(address1);
		final AddressModel address2 = new AddressModel();
		address2.setOwner(customer);
		address2.setLine1("line 1");
		modelService.save(address2);
		final List<ItemChangeDTO> receiver = new ArrayList<>();
		final ChangesCollector collector = getDeltaCollector(receiver);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);
		changeDetectionService.consumeChanges(receiver);

		// When we delete the owner
		modelService.remove(address2);
		modelService.remove(customer);

		// And grab changes again
		receiver.clear();

		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);

		// Then we shouldn't have any change records
//		assertThat(receiver, emptyIterable());
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(customer.getPk().getLongValue())))));
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(address1.getPk().getLongValue())))));
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(address2.getPk().getLongValue())))));
	}

	@Test
	public void whenEverythingIsDeleted()
	{
		// Given we have a customer and related some addresses
		final String streamId = UUID.randomUUID().toString();
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		modelService.save(customer);
		final AddressModel address1 = new AddressModel();
		address1.setOwner(customer);
		address1.setLine1("line 1");
		modelService.save(address1);
		final AddressModel address2 = new AddressModel();
		address2.setOwner(customer);
		address2.setLine1("line 1");
		modelService.save(address2);
		final List<ItemChangeDTO> receiver = new ArrayList<>();
		C4CAggregatingCollector collector = getDeltaCollector(receiver);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);
		changeDetectionService.consumeChanges(receiver);

		// When we delete the owner and all his addresses
		modelService.remove(customer);
		modelService.remove(address1);
		modelService.remove(address2);

		// And grab changes again
		receiver.clear();
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);

		// Then we shouldn't have any change records
//		assertThat(receiver, emptyIterable());
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(customer.getPk().getLongValue())))));
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(address1.getPk().getLongValue())))));
		assertThat(receiver, not(hasItem(hasProperty("itemPK", is(address2.getPk().getLongValue())))));
	}

	@Test
	public void shouldCreateSeparateInstances()
	{
		assertNotNull("Application context has not been initialized", applicationContext);
		assertFalse("The bean is singleton. Did you forget to define scope?",
				applicationContext.getBean("defaultC4CAggregatingCollector") == applicationContext.getBean("defaultC4CAggregatingCollector"));
	}

	@Test
	public void shouldGrabRelatedAddresses()
	{
		final String streamId = UUID.randomUUID().toString();

		// Given we have created a customer with an address
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		modelService.save(customer);
		final AddressModel address = new AddressModel();
		address.setOwner(customer);
		address.setLine1("line 1");
		modelService.save(address);
		consumeAllChanges(streamId);
		customer.setName("name");
		modelService.save(customer);
		final List<ItemChangeDTO> receiver = new ArrayList<>();

		// When we collect customer delta using defaultC4CDeltaCollector
		final C4CAggregatingCollector collector = getDeltaCollector(receiver);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);

		// Then we have to get the address along with the customer
		assertThat(receiver, hasItem(hasProperty("itemPK", is(customer.getPk().getLongValue()))));
		assertThat(receiver, hasItem(hasProperty("itemPK", is(address.getPk().getLongValue()))));
	}

	@Test
	public void shouldGrabRelatedCustomers()
	{
		final String streamId = UUID.randomUUID().toString();
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		modelService.save(customer);
		final AddressModel address = new AddressModel();
		address.setOwner(customer);
		modelService.save(address);
		consumeAllChanges(streamId);
		address.setLine1("1");
		modelService.save(address);
		final List<ItemChangeDTO> receiver = new ArrayList<>();
		final C4CAggregatingCollector collector = getDeltaCollector(receiver);

		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, collector);

		assertThat(receiver, hasItem(hasProperty("itemPK", is(customer.getPk().getLongValue()))));
		assertThat(receiver, hasItem(hasProperty("itemPK", is(address.getPk().getLongValue()))));
	}

	@Test
	public void shouldConsumeSyntheticChanges()
	{
		// Given we have some changes models
		final String streamId = UUID.randomUUID().toString();
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		modelService.save(customer);
		final AddressModel address = new AddressModel();
		address.setOwner(customer);
		modelService.save(address);
		final List<ItemChangeDTO> receiver = new ArrayList<>();
		final C4CAggregatingCollector collector = getDeltaCollector(receiver);

		// After we consume all changes
		final InMemoryChangesCollector tempCollector = new InMemoryChangesCollector();
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, tempCollector);
		changeDetectionService.consumeChanges(tempCollector.getChanges());

		// There shouldn't be any extra changes
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, collector);

		assertTrue(receiver.isEmpty());
	}

	protected C4CAggregatingCollector getDeltaCollector(final List<ItemChangeDTO> receiver)
	{
		C4CAggregatingCollector result = (C4CAggregatingCollector) applicationContext.getBean("defaultC4CAggregatingCollector");
		final BatchingCollector customerCollector = mock(BatchingCollector.class);
		when(customerCollector.collect(any(ItemChangeDTO.class))).thenAnswer(invocationOnMock -> {
			final ItemChangeDTO arg = (ItemChangeDTO) invocationOnMock.getArguments()[0];
			receiver.add(arg);
			return true;
		});
		final BatchingCollector addressCollector = mock(BatchingCollector.class);
		when(addressCollector.collect(any(ItemChangeDTO.class))).thenAnswer(invocationOnMock -> {
			final ItemChangeDTO arg = (ItemChangeDTO) invocationOnMock.getArguments()[0];
			receiver.add(arg);
			return true;
		});
		result.setCustomerCollector(customerCollector);
		result.setAddressCollector(addressCollector);
		result.setCustomerConfigurationId("c4cCustomerStream");
		result.setAddressConfigurationId("c4cAddressStream");
		return result;
	}

	protected void consumeAllChanges(final String streamId)
	{
		final InMemoryChangesCollector tempCollector = new InMemoryChangesCollector();
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(CustomerModel.class), streamId, tempCollector);
		changeDetectionService.collectChangesForType(
				typeService.getComposedTypeForClass(AddressModel.class), streamId, tempCollector);
		changeDetectionService.consumeChanges(tempCollector.getChanges());
	}

	protected void dump(List<ItemChangeDTO> items)
	{
		LOG.info(items.stream().map(ItemChangeDTO::getItemPK).map(Object::toString).collect(Collectors.joining("\n")));
	}
}
