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
package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Assert;
import org.junit.Test;


public class SolrSchemaTest extends AbstractIntegrationTest
{
	@Test
	public void testDynamicInt() throws Exception
	{
		final String dynamicField = "dynamic_int";
		final String dynamicFieldMultiValued = "dynamic_int_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, Integer.valueOf(1));
		document.addField(dynamicFieldMultiValued, Integer.valueOf(3));
		document.addField(dynamicFieldMultiValued, Integer.valueOf(4));
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), Integer.valueOf(1));

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(Integer.valueOf(3)));
		Assert.assertTrue(dynamicFieldValues.contains(Integer.valueOf(4)));
	}

	@Test
	public void testDynamicString() throws Exception
	{
		final String dynamicField = "dynamic_string";
		final String dynamicFieldMultiValued = "dynamic_string_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, "1");
		document.addField(dynamicFieldMultiValued, "3");
		document.addField(dynamicFieldMultiValued, "4");
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), "1");

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains("3"));
		Assert.assertTrue(dynamicFieldValues.contains("4"));
	}

	@Test
	public void testDynamicLong() throws Exception
	{
		final String dynamicField = "dynamic_long";
		final String dynamicFieldMultiValued = "dynamic_long_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, Long.valueOf(1));
		document.addField(dynamicFieldMultiValued, Long.valueOf(Long.MIN_VALUE));
		document.addField(dynamicFieldMultiValued, Long.valueOf(Long.MAX_VALUE));
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), Long.valueOf(1));

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(Long.valueOf(Long.MIN_VALUE)));
		Assert.assertTrue(dynamicFieldValues.contains(Long.valueOf(Long.MAX_VALUE)));

	}

	@Test
	public void testDynamicText() throws Exception
	{
		final String dynamicField = "dynamic_text";
		final String dynamicFieldMultiValued = "dynamic_text_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, "Hello World");
		document.addField(dynamicFieldMultiValued, "Hello World 1");
		document.addField(dynamicFieldMultiValued, "Hello World 2");
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), "Hello World");

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains("Hello World 1"));
		Assert.assertTrue(dynamicFieldValues.contains("Hello World 2"));
	}

	@Test
	public void testDynamicBoolean() throws Exception
	{
		final String dynamicField = "dynamic_boolean";
		final String dynamicFieldMultiValued = "dynamic_boolean_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, Boolean.TRUE);
		document.addField(dynamicFieldMultiValued, Boolean.FALSE);
		document.addField(dynamicFieldMultiValued, Boolean.TRUE);
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), Boolean.TRUE);

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(Boolean.TRUE));
		Assert.assertTrue(dynamicFieldValues.contains(Boolean.FALSE));
	}

	@Test
	public void testDynamicFloat() throws Exception
	{
		final String dynamicField = "dynamic_float";
		final String dynamicFieldMultiValued = "dynamic_float_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, Float.valueOf(1.0f));
		document.addField(dynamicFieldMultiValued, Float.valueOf(2.0f));
		document.addField(dynamicFieldMultiValued, Float.valueOf(3.0f));
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), Float.valueOf(1.0f));

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(Float.valueOf(2.0f)));
		Assert.assertTrue(dynamicFieldValues.contains(Float.valueOf(3.0f)));
	}

	@Test
	public void testDynamicDouble() throws Exception
	{
		final String dynamicField = "dynamic_double";
		final String dynamicFieldMultiValued = "dynamic_double_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, Double.valueOf(1.0f));
		document.addField(dynamicFieldMultiValued, Double.valueOf(2.0f));
		document.addField(dynamicFieldMultiValued, Double.valueOf(3.0f));
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), Double.valueOf(1.0f));

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(Double.valueOf(2.0f)));
		Assert.assertTrue(dynamicFieldValues.contains(Double.valueOf(3.0f)));
	}

	@Test
	public void testDynamicDate() throws Exception
	{
		final String dynamicField = "dynamic_date";
		final String dynamicFieldMultiValued = "dynamic_date_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final Calendar date1 = Calendar.getInstance();
		final Calendar date2 = Calendar.getInstance();
		final Calendar date3 = Calendar.getInstance();
		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, date1.getTime());
		document.addField(dynamicFieldMultiValued, date2.getTime());
		document.addField(dynamicFieldMultiValued, date3.getTime());
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), date1.getTime());

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(date2.getTime()));
		Assert.assertTrue(dynamicFieldValues.contains(date3.getTime()));
	}

	@Test
	public void testDynamicTrieInt() throws Exception
	{
		final String dynamicField = "dynamic_tint";
		final String dynamicFieldMultiValued = "dynamic_tint_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, Integer.valueOf(1));
		document.addField(dynamicFieldMultiValued, Integer.valueOf(3));
		document.addField(dynamicFieldMultiValued, Integer.valueOf(4));
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), Integer.valueOf(1));

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(Integer.valueOf(3)));
		Assert.assertTrue(dynamicFieldValues.contains(Integer.valueOf(4)));
	}

	@Test
	public void testDynamicTrieLong() throws Exception
	{
		final String dynamicField = "dynamic_tlong";
		final String dynamicFieldMultiValued = "dynamic_tlong_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, Long.valueOf(1));
		document.addField(dynamicFieldMultiValued, Long.valueOf(Long.MIN_VALUE));
		document.addField(dynamicFieldMultiValued, Long.valueOf(Long.MAX_VALUE));
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), Long.valueOf(1));

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(Long.valueOf(Long.MIN_VALUE)));
		Assert.assertTrue(dynamicFieldValues.contains(Long.valueOf(Long.MAX_VALUE)));
	}

	@Test
	public void testDynamicTrieFloat() throws Exception
	{
		final String dynamicField = "dynamic_tfloat";
		final String dynamicFieldMultiValued = "dynamic_tfloat_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, Float.valueOf(1.0f));
		document.addField(dynamicFieldMultiValued, Float.valueOf(2.0f));
		document.addField(dynamicFieldMultiValued, Float.valueOf(3.0f));
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), Float.valueOf(1.0f));

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(Float.valueOf(2.0f)));
		Assert.assertTrue(dynamicFieldValues.contains(Float.valueOf(3.0f)));
	}

	@Test
	public void testDynamicTrieDouble() throws Exception
	{
		final String dynamicField = "dynamic_tdouble";
		final String dynamicFieldMultiValued = "dynamic_tdouble_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, Double.valueOf(1.0f));
		document.addField(dynamicFieldMultiValued, Double.valueOf(2.0f));
		document.addField(dynamicFieldMultiValued, Double.valueOf(3.0f));
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), Double.valueOf(1.0f));

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(Double.valueOf(2.0f)));
		Assert.assertTrue(dynamicFieldValues.contains(Double.valueOf(3.0f)));
	}

	@Test
	public void testDynamicTrieDate() throws Exception
	{
		final String dynamicField = "dynamic_tdate";
		final String dynamicFieldMultiValued = "dynamic_tdate_mv";
		final SolrClient solrClient = getSolrClient();
		final String id = UUID.randomUUID().toString();

		final Calendar date1 = Calendar.getInstance();
		final Calendar date2 = Calendar.getInstance();
		final Calendar date3 = Calendar.getInstance();
		final SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField(dynamicField, date1.getTime());
		document.addField(dynamicFieldMultiValued, date2.getTime());
		document.addField(dynamicFieldMultiValued, date3.getTime());
		solrClient.add(document);
		solrClient.commit();

		// Perform the query, getting the response, and validating the results
		final QueryResponse response = solrClient.query(new SolrQuery("id:" + id));
		Assert.assertNotNull(response);
		final SolrDocumentList documents = response.getResults();
		Assert.assertNotNull(documents);
		Assert.assertEquals(1, documents.size());
		final SolrDocument resultDocument = documents.iterator().next();
		Assert.assertNotNull(resultDocument);

		// Verify the identifier field
		Assert.assertEquals(resultDocument.getFieldValue("id"), id);

		// Verify the non-multivalued field
		Assert.assertEquals(resultDocument.getFieldValue(dynamicField), date1.getTime());

		// Verify the multivalued field
		final Collection<Object> dynamicFieldValues = resultDocument.getFieldValues(dynamicFieldMultiValued);
		Assert.assertNotNull(dynamicFieldValues);
		Assert.assertEquals(2, dynamicFieldValues.size());
		Assert.assertTrue(dynamicFieldValues.contains(date2.getTime()));
		Assert.assertTrue(dynamicFieldValues.contains(date3.getTime()));
	}
}
