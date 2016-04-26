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
package de.hybris.platform.solrfacetsearch.common.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.common.ListenersFactory;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;


@UnitTest
public class DefaultListenersFactoryTest
{
	private static final String LISTENER1_NAME = "listener1";
	private static final String LISTENER2_NAME = "listener2";
	private static final String LISTENER3_NAME = "listener3";
	private static final String NOT_A_LISTENER_NAME = "otherListener";

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private TestListener listener1;

	@Mock
	private TestListener listener2;

	@Mock
	private TestListener listener3;

	private FacetSearchConfig facetSearchConfig;
	private IndexConfig indexConfig;
	private IndexedType indexedType;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		facetSearchConfig = new FacetSearchConfig();
		indexConfig = new IndexConfig();
		indexedType = new IndexedType();

		facetSearchConfig.setIndexConfig(indexConfig);

		when(applicationContext.getBean(LISTENER1_NAME, TestListener.class)).thenReturn(listener1);
		when(Boolean.valueOf(applicationContext.isTypeMatch(LISTENER1_NAME, TestListener.class))).thenReturn(Boolean.TRUE);

		when(applicationContext.getBean(LISTENER2_NAME, TestListener.class)).thenReturn(listener2);
		when(Boolean.valueOf(applicationContext.isTypeMatch(LISTENER2_NAME, TestListener.class))).thenReturn(Boolean.TRUE);

		when(applicationContext.getBean(LISTENER3_NAME, TestListener.class)).thenReturn(listener3);
		when(Boolean.valueOf(applicationContext.isTypeMatch(LISTENER3_NAME, TestListener.class))).thenReturn(Boolean.TRUE);

		when(Boolean.valueOf(applicationContext.isTypeMatch(NOT_A_LISTENER_NAME, TestListener.class))).thenReturn(Boolean.FALSE);
	}

	protected DefaultListenersFactory createListenersFactory()
	{
		final DefaultListenersFactory listenersFactory = new DefaultListenersFactory();
		listenersFactory.setSupportedTypes(Collections.<Class<?>> singletonList(TestListener.class));
		listenersFactory.setApplicationContext(applicationContext);
		return listenersFactory;
	}

	@Test
	public void noListenerConfigured() throws Exception
	{
		// given
		final ListenersFactory listenersFactory = createListenersFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(facetSearchConfig, indexedType, TestListener.class);

		// then
		assertTrue(listeners.isEmpty());
	}

	@Test
	public void noListenerOfExpectedType() throws Exception
	{
		// given
		indexConfig.setListeners(Collections.singletonList(NOT_A_LISTENER_NAME));
		indexedType.setListeners(Collections.singletonList(NOT_A_LISTENER_NAME));

		final ListenersFactory listenersFactory = createListenersFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(facetSearchConfig, indexedType, TestListener.class);

		//then
		assertEquals(0, listeners.size());
	}

	@Test
	public void globalListenerConfigured() throws Exception
	{
		// given
		final ListenerDefinition listenerDefinition = new ListenerDefinition();
		listenerDefinition.setPriority(100);
		listenerDefinition.setListener(listener1);

		final Map<String, ListenerDefinition> listenerDefinitions = new HashMap<String, ListenerDefinition>();
		listenerDefinitions.put(LISTENER1_NAME, listenerDefinition);

		when(applicationContext.getBeansOfType(ListenerDefinition.class)).thenReturn(listenerDefinitions);

		final ListenersFactory listenersFactory = createListenersFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(facetSearchConfig, indexedType, TestListener.class);

		// then
		assertEquals(1, listeners.size());
		assertThat(listeners, hasItem(listener1));
	}

	@Test
	public void indexConfigListenerConfigured() throws Exception
	{
		// given
		indexConfig.setListeners(Collections.singletonList(LISTENER1_NAME));

		final ListenersFactory listenersFactory = createListenersFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(facetSearchConfig, indexedType, TestListener.class);

		// then
		assertEquals(1, listeners.size());
		assertThat(listeners, hasItem(listener1));
	}

	@Test
	public void indexedTypeListenerConfigured() throws Exception
	{
		// given
		indexedType.setListeners(Collections.singletonList(LISTENER1_NAME));

		final ListenersFactory listenersFactory = createListenersFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(facetSearchConfig, indexedType, TestListener.class);

		// then
		assertEquals(1, listeners.size());
		assertThat(listeners, hasItem(listener1));
	}

	@Test
	public void globalListenersOrder() throws Exception
	{
		// given
		final ListenerDefinition listenerDefinition1 = new ListenerDefinition();
		listenerDefinition1.setPriority(100);
		listenerDefinition1.setListener(listener1);

		final ListenerDefinition listenerDefinition2 = new ListenerDefinition();
		listenerDefinition2.setPriority(20);
		listenerDefinition2.setListener(listener2);

		final ListenerDefinition listenerDefinition3 = new ListenerDefinition();
		listenerDefinition3.setPriority(250);
		listenerDefinition3.setListener(listener3);

		final Map<String, ListenerDefinition> listenerDefinitions = new HashMap<String, ListenerDefinition>();
		listenerDefinitions.put(LISTENER1_NAME, listenerDefinition1);
		listenerDefinitions.put(LISTENER2_NAME, listenerDefinition2);
		listenerDefinitions.put(LISTENER3_NAME, listenerDefinition3);

		when(applicationContext.getBeansOfType(ListenerDefinition.class)).thenReturn(listenerDefinitions);

		final ListenersFactory listenersFactory = createListenersFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(facetSearchConfig, indexedType, TestListener.class);

		// then
		assertEquals(3, listeners.size());
		assertThat(listeners.get(0), is(listener3));
		assertThat(listeners.get(1), is(listener1));
		assertThat(listeners.get(2), is(listener2));
	}

	@Test
	public void indexConfigListenersOrder() throws Exception
	{
		// given
		indexConfig.setListeners(Arrays.asList(LISTENER2_NAME, LISTENER1_NAME));

		final ListenersFactory listenersFactory = createListenersFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(facetSearchConfig, indexedType, TestListener.class);

		// then
		assertEquals(2, listeners.size());
		assertThat(listeners.get(0), is(listener2));
		assertThat(listeners.get(1), is(listener1));
	}

	@Test
	public void indexedTypeListenersOrder() throws Exception
	{
		// given
		indexedType.setListeners(Arrays.asList(LISTENER2_NAME, LISTENER1_NAME));

		final ListenersFactory listenersFactory = createListenersFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(facetSearchConfig, indexedType, TestListener.class);

		// then
		assertEquals(2, listeners.size());
		assertThat(listeners.get(0), is(listener2));
		assertThat(listeners.get(1), is(listener1));
	}

	@Test
	public void listenersOrder() throws Exception
	{
		// given
		final ListenerDefinition listenerDefinition1 = new ListenerDefinition();
		listenerDefinition1.setPriority(250);
		listenerDefinition1.setListener(listener1);

		final Map<String, ListenerDefinition> listenerDefinitions = new HashMap<String, ListenerDefinition>();
		listenerDefinitions.put(LISTENER1_NAME, listenerDefinition1);

		when(applicationContext.getBeansOfType(ListenerDefinition.class)).thenReturn(listenerDefinitions);

		indexConfig.setListeners(Collections.singletonList(LISTENER2_NAME));
		indexedType.setListeners(Collections.singletonList(LISTENER3_NAME));

		final ListenersFactory listenersFactory = createListenersFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(facetSearchConfig, indexedType, TestListener.class);

		// then
		assertEquals(3, listeners.size());
		assertThat(listeners.get(0), is(listener1));
		assertThat(listeners.get(1), is(listener2));
		assertThat(listeners.get(2), is(listener3));
	}

	public interface TestListener
	{
		// No methods
	}
}
