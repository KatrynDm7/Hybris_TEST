package de.hybris.platform.financialfacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.financialfacades.populators.InsuranceOrderEntryFormDataPopulator;
import de.hybris.platform.financialfacades.util.InsuranceCheckoutHelper;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsfacades.strategy.GetYFormDefinitionsForProductStrategy;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


/**
 * The class of InsuranceOrderEntryFormDataPopulatorTest.
 */
@UnitTest
public class InsuranceOrderEntryFormDataPopulatorTest
{

	@Mock
	private GetYFormDefinitionsForProductStrategy getYFormDefinitionsForProductStrategy;

	@Mock
	private YFormFacade yFormFacade;

	@Mock
	private InsuranceCheckoutHelper insuranceCheckoutHelper;

	@InjectMocks
	private InsuranceOrderEntryFormDataPopulator insuranceOrderEntryFormDataPopulator;

	@Before
	public void setup()
	{
		insuranceOrderEntryFormDataPopulator = new InsuranceOrderEntryFormDataPopulator();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldPopulateFormDataIntoOrderEntryData() throws YFormServiceException
	{
		final String cartCode = "cartCode";
		final String productCode = "productCode";
		final String applicationId = "applicationId";
		final String formId = "formId";
		final Integer entryNo = new Integer(1);
		final String expectRefId = "cartCode_1";

		final AbstractOrderEntryModel orderEntryModel = Mockito.mock(AbstractOrderEntryModel.class);
		final AbstractOrderModel orderModel = Mockito.mock(AbstractOrderModel.class);
		Mockito.when(orderModel.getCode()).thenReturn(cartCode);
		Mockito.when(orderEntryModel.getOrder()).thenReturn(orderModel);

		final OrderEntryData orderEntryData = new OrderEntryData();

		final ProductData productData = new ProductData();
		productData.setCode(productCode);

		orderEntryData.setProduct(productData);
		orderEntryData.setEntryNumber(entryNo);

		final YFormDataData yFormDataData = new YFormDataData();
		final List<YFormDataData> yFormDataDatas = Lists.newArrayList();
		yFormDataDatas.add(yFormDataData);

		final List<YFormDefinitionData> yFormDefinitions = Lists.newArrayList();
		final YFormDefinitionData yFormDefinition = new YFormDefinitionData();
		yFormDefinition.setApplicationId(applicationId);
		yFormDefinition.setFormId(formId);
		yFormDefinitions.add(yFormDefinition);

		Mockito.when(getYFormDefinitionsForProductStrategy.execute(productCode)).thenReturn(yFormDefinitions);
		Mockito.when(yFormFacade.getYFormData(applicationId, formId, expectRefId, YFormDataTypeEnum.DATA))
				.thenReturn(yFormDataData);
		Mockito.when(insuranceCheckoutHelper.buildYFormDataRefId(cartCode, entryNo)).thenReturn(expectRefId);

		insuranceOrderEntryFormDataPopulator.populate(orderEntryModel, orderEntryData);

		Assert.assertEquals(yFormDataDatas, orderEntryData.getFormDataData());

		Mockito.verify(orderEntryModel, Mockito.atLeastOnce()).getOrder();
		Mockito.verify(orderModel, Mockito.atLeastOnce()).getCode();
		Mockito.verify(insuranceCheckoutHelper).buildYFormDataRefId(cartCode, entryNo);
		Mockito.verify(getYFormDefinitionsForProductStrategy).execute(productCode);
		Mockito.verify(yFormFacade).getYFormData(applicationId, formId, expectRefId, YFormDataTypeEnum.DATA);
	}


}
