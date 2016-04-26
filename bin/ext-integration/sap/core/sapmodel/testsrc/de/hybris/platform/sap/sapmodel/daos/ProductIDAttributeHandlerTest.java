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
package de.hybris.platform.sap.sapmodel.daos;



import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.RelationQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.TranslationResult;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sap.hybris.integration.models.model.SAPProductIDDataConversionModel;

@UnitTest
public class ProductIDAttributeHandlerTest
{

	class FlexibleSearchServiceMock implements FlexibleSearchService
	{

		@SuppressWarnings("unchecked")
		public <T> T getModelByExample(final T example)
		{
			try
			{
				Thread.sleep(2);
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
			return (T) data;
		}

		public <T> List<T> getModelsByExample(final T example)
		{
			return null;
		}

		public <T> SearchResult<T> search(final FlexibleSearchQuery searchQuery)
		{
			return null;
		}

		public <T> SearchResult<T> search(final String query)
		{
			return null;
		}

		public <T> SearchResult<T> search(final String query, final Map<String, ? extends Object> queryParams)
		{
			return null;
		}

		public <T> SearchResult<T> searchRelation(final ItemModel model, final String attribute, final int start, final int count)
		{
			return null;
		}

		public <T> SearchResult<T> searchRelation(final RelationQuery query)
		{
			return null;
		}

		public <T> T searchUnique(final FlexibleSearchQuery searchQuery)
		{
			return null;
		}

		public TranslationResult translate(final FlexibleSearchQuery searchQuery)
		{
			return null;
		}

	}

	SAPProductIDDataConversionModel data;
	ProductIDAttributeHandler handler;

	private Object get(final String string)
	{
		final ProductModel p = new ProductModel();
		p.setCode(string);
		return handler.get(p);
	}

	@Before
	public void setUp() throws Exception
	{
		handler = new ProductIDAttributeHandler();
		data = new SAPProductIDDataConversionModel();
		handler.flexibleSearchService = new FlexibleSearchServiceMock();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testConvertID()
	{
	}

	@Test
	public void testDefault() throws Exception
	{
		data = null;
		Assert.assertEquals("", get("")); // Initial
		Assert.assertEquals("12345", get("12345"));
		Assert.assertEquals("12345", get("012345"));
		Assert.assertEquals("12345", get("000000000000012345"));
		Assert.assertEquals("100000000000012345", get("100000000000012345"));
		Assert.assertEquals("char", get("char"));
		Assert.assertEquals("0char", get("0char"));
		Assert.assertEquals("EPHBR03", get("EPHBR03"));
		Assert.assertEquals("0EPHBR03", get("0EPHBR03"));
		Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", get("ABCDEFGHIJKLMNOPQRSTUVWXYZ")); // 26 character > 18
		Assert.assertEquals("0", get("000000000000000000")); // All zeroes
		Assert.assertEquals("7", get("000000000000000007")); // 7 default
		Assert.assertEquals("7", get("00000000000000000000000007")); // 7 more than 18
		Assert.assertEquals("12345678901234567890", get("12345678901234567890")); // 20 digits
		Assert.assertEquals("12345678901234567890", get("012345678901234567890")); // 20 digits
		Assert.assertEquals("7", get("07")); //  7 less than 18
		Assert.assertEquals("-00000000000000007", get("-00000000000000007")); // 7 negative
		Assert.assertEquals("00000000000000000???", get("00000000000000000???")); // 7 unicode full width	
	}


	@Test
	public void testGet()
	{
	}

	@Test
	public void testKeepLeadingZeros() throws Exception
	{
		data.setMatnrLength(18);
		data.setDisplayLeadingZeros(true);
		data.setDisplayLexicographic(false);
		data.setMask("");

		Assert.assertEquals("", get("")); // Initial
		Assert.assertEquals("000000000000012345", get("000000000000012345"));
		Assert.assertEquals("100000000000000123", get("100000000000000123"));
		Assert.assertEquals("00000000000012345", get("00000000000012345"));
		Assert.assertEquals("10000000012345", get("10000000012345"));
		Assert.assertEquals("char", get("char"));
		Assert.assertEquals("0char", get("0char"));
		Assert.assertEquals("EPHBR03", get("EPHBR03"));
		Assert.assertEquals("0EPHBR03", get("0EPHBR03"));
		Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", get("ABCDEFGHIJKLMNOPQRSTUVWXYZ")); // 26 character > 18		
		Assert.assertEquals("000000000000000000", get("000000000000000000")); // All zeroes
		Assert.assertEquals("000000000000000007", get("000000000000000007")); // 7 default
		Assert.assertEquals("000000000000000001", get("00000000000000000100000007")); // 7 more than 18, interesting...
		Assert.assertEquals("0000000000000000010000000A", get("0000000000000000010000000A")); // 7 more than 18, alpha after
		Assert.assertEquals("07", get("07")); //  7 less than 18
		Assert.assertEquals("-00000000000000007", get("-00000000000000007")); //  7 negative
		Assert.assertEquals("00000000000000000???", get("00000000000000000???")); //  7 unicode full width
	}

	@Test
	public void testKeepLeadingZerosIDLength10() throws Exception
	{
		data.setMatnrLength(10);
		data.setDisplayLeadingZeros(true);
		data.setDisplayLexicographic(false);
		data.setMask("");

		Assert.assertEquals("", get("")); //initial
		Assert.assertEquals("1234567891", get("000000001234567891")); //18 chars to 10
		Assert.assertEquals("0000000123", get("000000000000000123")); //18 chars to 10
		Assert.assertEquals("000000123", get("00000000000000123")); //17 chars goes to 9 chars
		Assert.assertEquals("00123", get("0000000000123")); //13 chars goes to 5 chars
		Assert.assertEquals("0123", get("000000000123")); //12 chars goes to 4 chars
		Assert.assertEquals("123", get("00000000123")); //11 chars goes to 3 chars
		Assert.assertEquals("23", get("0000000123")); // 10 chars to 2 chars
		Assert.assertEquals("3", get("000000123")); // 9 chars to 1 char
		Assert.assertEquals("", get("00000123")); // 8 chars to 0 chars
		Assert.assertEquals("", get("00123")); // 5 chars to 0 chars
		Assert.assertEquals("0000000123", get("000450000000000123")); //18 chars to 10
		Assert.assertEquals("0000012345", get("100000000000012345")); //18 chars to 17
		Assert.assertEquals("00045000000000012???", get("00045000000000012???")); //7 unicode full width: read as non numeric
		Assert.assertEquals("char", get("char"));
		Assert.assertEquals("000char", get("000char"));
		Assert.assertEquals("charcharcharcharch", get("charcharcharcharch")); //18 stays 18 for char
		Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", get("ABCDEFGHIJKLMNOPQRSTUVWXYZ")); // 26 character > 18
	}



	@Test
	public void testKeepLeadingZerosIDLength17() throws Exception
	{
		data.setMatnrLength(17);
		data.setDisplayLeadingZeros(true);
		data.setDisplayLexicographic(false);
		data.setMask("");
		Assert.assertEquals("", get("")); //initial
		Assert.assertEquals("00000000000012345", get("100000000000012345")); //18 chars to 17
		Assert.assertEquals("charcharcharcharch", get("charcharcharcharch")); //18 chars stays 18 for alphanum
		Assert.assertEquals("00045000000000012???", get("00045000000000012???")); //7 unicode full width: read as non numeric
		Assert.assertEquals("12345678912345678", get("012345678912345678")); //18 chars to 17
		Assert.assertEquals("00000000000000123", get("000000000000000123")); //18 chars to 17
		Assert.assertEquals("00450000000000123", get("000450000000000123")); //18 chars to 17
		Assert.assertEquals("00000000000000000", get("000000000000000000")); //18 chars to 17
		Assert.assertEquals("0000000000000123", get("00000000000000123")); //17 chars to 16
		Assert.assertEquals("000000000000123", get("0000000000000123")); //16 chars to 15
		Assert.assertEquals("0123", get("00123")); // 5 chars to 4		
		Assert.assertEquals("char", get("char"));
		Assert.assertEquals("000char", get("000char"));
		Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", get("ABCDEFGHIJKLMNOPQRSTUVWXYZ")); // 26 character > 18		
	}


	@Test
	public void testKeepLeadingZerosIDLength5() throws Exception
	{
		data.setMatnrLength(5);
		data.setDisplayLeadingZeros(true);
		data.setDisplayLexicographic(false);
		data.setMask("");
		Assert.assertEquals("", get("")); //initial
		Assert.assertEquals("12345", get("000000000000012345"));
		Assert.assertEquals("00123", get("000000000000000123"));
		Assert.assertEquals("0123", get("00000000000000123"));
		Assert.assertEquals("123", get("0000000000000123"));
		Assert.assertEquals("3", get("00000000000123"));
		Assert.assertEquals("3", get("10000000000123"));
		Assert.assertEquals("", get("0000000000123"));
		Assert.assertEquals("", get("00123"));
		Assert.assertEquals("", get("0123"));
		Assert.assertEquals("00123", get("000450000000000123"));
		Assert.assertEquals("00045000000000012???", get("00045000000000012???")); // 7 unicode full width
		Assert.assertEquals("char", get("char"));
		Assert.assertEquals("00char", get("00char"));
	}

	@Test
	public void testLexicographical() throws Exception
	{
		data.setMatnrLength(18);
		data.setDisplayLeadingZeros(false);
		data.setDisplayLexicographic(true);
		data.setMask("");
		Assert.assertEquals("", get("")); //initial
		Assert.assertEquals("000000000000012345", get("000000000000012345"));
		Assert.assertEquals("00000000000012345", get("00000000000012345"));
		Assert.assertEquals("0000000000012345", get("0000000000012345"));
		Assert.assertEquals("000000000012345", get("000000000012345"));
		Assert.assertEquals("char", get("char"));
	}

	@Test
	public void testMask() throws Exception
	{
		data.setMatnrLength(18);
		data.setDisplayLeadingZeros(false);
		data.setDisplayLexicographic(false);
		data.setMask("_-_#______________");
		Assert.assertEquals("0", get("000000000000000000")); //initial
		Assert.assertEquals("12345", get("000000000000012345"));
		Assert.assertEquals("1-2#34567890123456", get("001234567890123456"));
		Assert.assertEquals("c-h#archarcharchar", get("charcharcharchar"));

		Assert.assertEquals("12345", get("00000000000012345"));
		Assert.assertEquals("0-0#00000000012345", get("100000000000012345"));
		Assert.assertEquals("0-0#0000000012345", get("10000000000012345"));
		Assert.assertEquals("0-0#000000012345", get("1000000000012345"));
		Assert.assertEquals("0-0#00000012345", get("100000000012345"));
		Assert.assertEquals("0-0#0000012345", get("10000000012345"));
		Assert.assertEquals("7", get("000000000000000007"));
		Assert.assertEquals("1", get("00000000000000000100000007"));
		Assert.assertEquals("0-0#00000000000000", get("0000000000000000010000000A"));
		Assert.assertEquals("- #", get("07"));
		Assert.assertEquals("0-0#00000000000007", get("-00000000000000007"));
		Assert.assertEquals("+-0#00000000000000", get("+00000000000000007"));
		Assert.assertEquals("0-0#00000000000000", get("00000000000000000???"));
		Assert.assertEquals("c-h#ar", get("char"));
		Assert.assertEquals("0-c#har", get("0char"));
		Assert.assertEquals("E-P#HBR03", get("EPHBR03"));
		Assert.assertEquals("0-E#PHBR03", get("0EPHBR03"));
		Assert.assertEquals("A-B#CDEFGHIJKLMNOP", get("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assert.assertEquals("A", get("A"));
		Assert.assertEquals("A-B", get("AB"));
		Assert.assertEquals("A-B#C", get("ABC"));
	}

	//	@Test
	//	public void testPerformance() throws IOException
	//	{
	//		final String text = readFile("Products_INITIAL.txt");
	//	}
	//
	//	protected final static String readFile(final String path) throws IOException
	//	{
	//		return readFile(path, Charset.forName("UTF-8"));
	//	}
	//
	//	protected final static String readFile(final String path, final Charset encoding) throws IOException
	//	{
	//		try (final InputStream is = ProductIDAttributeHandlerTest.class.getResourceAsStream("/".concat(path)))
	//		{
	//			try (final Scanner scanner = new java.util.Scanner(is))
	//			{
	//				try (final Scanner s = scanner.useDelimiter("\\A"))
	//				{
	//					return s.hasNext() ? s.next() : "";
	//				}
	//			}
	//		}
	//	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSet()
	{
		handler.set(null, null);
	}

}
