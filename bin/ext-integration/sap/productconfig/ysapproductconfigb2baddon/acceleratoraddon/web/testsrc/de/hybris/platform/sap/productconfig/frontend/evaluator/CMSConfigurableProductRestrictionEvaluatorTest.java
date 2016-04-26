package de.hybris.platform.sap.productconfig.frontend.evaluator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.data.impl.DefaultCMSDataFactory;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.frontend.model.CMSConfigurableProductRestrictionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CMSConfigurableProductRestrictionEvaluatorTest
{


	private CMSConfigurableProductRestrictionEvaluator classUnderTest;
	private RestrictionData context;
	private CMSConfigurableProductRestrictionModel restrictionModel;
	@Mock
	private ProductModel mockedProductModel;
	@Mock
	private ModelService mockedModelService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest = new CMSConfigurableProductRestrictionEvaluator();
		classUnderTest.setConfigurableSource("sapCpnfig");
		classUnderTest.setModelService(mockedModelService);
		context = new DefaultCMSDataFactory().createRestrictionData(mockedProductModel);
		Mockito.when(mockedModelService.getAttributeValue(mockedProductModel, "sapCpnfig")).thenReturn(Boolean.TRUE);


	}

	@Test
	public void testEvaluateTrue()
	{
		//Mockito.when(mockedProductModel.getSapConfigurable()).thenReturn(Boolean.TRUE);
		Mockito.when(mockedModelService.getAttributeValue(mockedProductModel, "sapCpnfig")).thenReturn(Boolean.TRUE);
		Assert.assertTrue("Product is configurable, but not matched by restriction",
				classUnderTest.evaluate(restrictionModel, context));
	}

	@Test
	public void testEvaluateFalse()
	{
		//Mockito.when(mockedProductModel.getSapConfigurable()).thenReturn(Boolean.FALSE);
		Mockito.when(mockedModelService.getAttributeValue(mockedProductModel, "sapCpnfig")).thenReturn(Boolean.FALSE);

		Assert.assertFalse("Product is not configurable, but matched by restriction",
				classUnderTest.evaluate(restrictionModel, context));
	}
}
