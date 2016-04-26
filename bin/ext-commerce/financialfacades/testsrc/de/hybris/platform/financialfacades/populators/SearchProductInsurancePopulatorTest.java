package de.hybris.platform.financialfacades.populators;


import com.google.common.collect.Maps;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.populators.SearchProductInsurancePopulator;
import de.hybris.platform.product.ProductService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test suite for {@link SearchProductInsurancePopulator}
 */
@UnitTest
public class SearchProductInsurancePopulatorTest
{
	@InjectMocks
	private SearchProductInsurancePopulator searchProductInsurancePopulator;

	@Mock
	private ProductService productService;

	@Mock
	private AbstractProductPopulator mockedProductAPopulator;

	@Mock
	private AbstractProductPopulator mockedProductBPopulator;

	private static final String CODE = "code";
	private static final String ALL_CATEGORIES = "allCategories";
    private static final String productCode = "WED_2STAR";
    
	@Before
	public void setup()
	{
		searchProductInsurancePopulator = new SearchProductInsurancePopulator();
		MockitoAnnotations.initMocks(this);


	}

	@Test
	public void testGetPopulatorsForCategories()
	{
		final SearchResultValueData searchResultValueData = new SearchResultValueData();
		final Map<String, Object> searchValueMap = new HashMap<>();
		searchValueMap.put(ALL_CATEGORIES, Arrays.asList("insurance_wedding", "insurances"));
		searchValueMap.put(CODE, productCode);
		searchResultValueData.setValues(searchValueMap);
		Map<String, AbstractProductPopulator> populators = Maps.newHashMap();
		populators.put("insurance_wedding", mockedProductAPopulator);
		populators.put("subscription", mockedProductBPopulator);
		searchProductInsurancePopulator.setCategoryProductInsurancePopulators(populators);


		List<Populator> list = searchProductInsurancePopulator.getPopulatorsForCategories(searchResultValueData);

		assertEquals(1, list.size());
		assertEquals(mockedProductAPopulator, list.get(0));

	}

	@Test
	public void testPopulate()
	{
		
        final SearchResultValueData searchResultValueData = new SearchResultValueData();
		final Map<String, Object> searchValueMap = new HashMap<>();
		searchValueMap.put(ALL_CATEGORIES, Arrays.asList("insurance_wedding", "insurances"));
		searchValueMap.put(CODE, productCode);
		searchResultValueData.setValues(searchValueMap);
		Map<String, AbstractProductPopulator> populators = Maps.newHashMap();
		populators.put("insurance_wedding", mockedProductAPopulator);
		populators.put("subscription", mockedProductBPopulator);
        searchProductInsurancePopulator.setCategoryProductInsurancePopulators(populators);
        
        ProductModel productModel = new ProductModel();
        productModel.setCode(productCode);
        
        given(productService.getProductForCode(productCode)).willReturn(productModel);
        
        ProductData productData = new ProductData();
        
        searchProductInsurancePopulator.populate(searchResultValueData, productData);
        
        verify(mockedProductAPopulator, times(1)).populate(productModel, productData);
        verify(mockedProductBPopulator, times(0)).populate(productModel, productData);
	}
}
