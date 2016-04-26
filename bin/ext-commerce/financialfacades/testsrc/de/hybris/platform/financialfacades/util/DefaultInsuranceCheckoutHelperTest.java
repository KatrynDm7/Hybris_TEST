package de.hybris.platform.financialfacades.util;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.insurance.data.CustomerFormSessionData;
import de.hybris.platform.commercefacades.insurance.data.FormSessionData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.financialfacades.strategies.CustomerFormPrePopulateStrategy;
import de.hybris.platform.storefront.form.data.FormDetailData;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.List;

import org.junit.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@UnitTest
public class DefaultInsuranceCheckoutHelperTest
{

	@InjectMocks
	private DefaultInsuranceCheckoutHelper helper;

	@Mock
	private CustomerFormPrePopulateStrategy prePopulateStrategy;

	@Mock
	private YFormFacade yFormFacade;

	@Before
	public void setup()
	{
		helper = new DefaultInsuranceCheckoutHelper();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBuildYFormDataRefId()
	{
		final Integer orderEntryNumber = new Integer(2);
		final String orderCode = "orderCode";
		Assert.assertEquals(orderCode + "_" + orderEntryNumber, helper.buildYFormDataRefId(orderCode, orderEntryNumber));
	}

	@Test
	public void testCreateFormDetailData()
	{
		final String applicationId = "applicationId";
		final String formId = "formId";

		final YFormDefinitionData yFormDefinitionData = new YFormDefinitionData();
		yFormDefinitionData.setApplicationId(applicationId);
		yFormDefinitionData.setFormId(formId);
		final Integer orderEntryNumber = new Integer(2);
		final String orderCode = "orderCode";

		final FormDetailData formDetailData = helper.createFormDetailData(yFormDefinitionData, orderCode, orderEntryNumber);

		Assert.assertNotNull(formDetailData);
		Assert.assertEquals(applicationId, formDetailData.getApplicationId());
		Assert.assertEquals(formId, formDetailData.getFormId());
		Assert.assertEquals(orderCode + "_" + orderEntryNumber, formDetailData.getRefId());
	}

	@Test
	public void testCreateFormDetailDataList()
	{
		final String applicationId = "applicationId";
		final String formId1 = "formId1";
		final String formId2 = "formId2";

		final YFormDefinitionData yFormDefinitionData1 = new YFormDefinitionData();
		yFormDefinitionData1.setApplicationId(applicationId);
		yFormDefinitionData1.setFormId(formId1);

		final YFormDefinitionData yFormDefinitionData2 = new YFormDefinitionData();
		yFormDefinitionData2.setApplicationId(applicationId);
		yFormDefinitionData2.setFormId(formId2);


		final Integer orderEntryNumber = new Integer(2);
		final String orderCode = "orderCode";

		final String expectedRefId = orderCode + "_" + orderEntryNumber;

		final List<YFormDefinitionData> yFormDefinitionDataList = Lists.newArrayList(yFormDefinitionData1, yFormDefinitionData2);

		Mockito.when(prePopulateStrategy.hasCustomerFormDataStored()).thenReturn(Boolean.FALSE);

		final List<FormDetailData> formDetailDataList = helper.createFormDetailData(yFormDefinitionDataList, orderCode,
				orderEntryNumber);

		Assert.assertNotNull(formDetailDataList);
		Assert.assertEquals(2, formDetailDataList.size());

		final FormDetailData formDetailData1 = formDetailDataList.get(0);

		Assert.assertEquals(applicationId, formDetailData1.getApplicationId());
		Assert.assertEquals(formId1, formDetailData1.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData1.getRefId());

		final FormDetailData formDetailData2 = formDetailDataList.get(1);

		Assert.assertEquals(applicationId, formDetailData2.getApplicationId());
		Assert.assertEquals(formId2, formDetailData2.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData2.getRefId());

	}

	@Test
	public void testCreateFormDetailDataListFromSessionWithSameFormDefinitionAndSameVersion() throws YFormServiceException
	{
		final String applicationId = "applicationId";
		final String formId1 = "formId1";
		final String formId2 = "formId2";
		final String formId3 = "formId3";
		final String content = "content";

		final YFormDefinitionData yFormDefinitionData1 = new YFormDefinitionData();
		yFormDefinitionData1.setApplicationId(applicationId);
		yFormDefinitionData1.setFormId(formId1);
		yFormDefinitionData1.setVersion(1);

		final YFormDefinitionData yFormDefinitionData2 = new YFormDefinitionData();
		yFormDefinitionData2.setApplicationId(applicationId);
		yFormDefinitionData2.setFormId(formId2);
		yFormDefinitionData2.setVersion(2);

		final Integer orderEntryNumber = new Integer(2);
		final String orderCode = "orderCode";

		final String expectedRefId = orderCode + "_" + orderEntryNumber;
		final String sessionRefId = orderCode + "_" + (orderEntryNumber + 1);

		final List<YFormDefinitionData> yFormDefinitionDataList = Lists.newArrayList(yFormDefinitionData1, yFormDefinitionData2);

		Mockito.when(prePopulateStrategy.hasCustomerFormDataStored()).thenReturn(Boolean.TRUE);

		final CustomerFormSessionData customerFormSessionData = new CustomerFormSessionData();
		final FormSessionData formSessionData = new FormSessionData();
		formSessionData.setYFormDataId(formId3);
		formSessionData.setYFormDefinition(yFormDefinitionData1);
		formSessionData.setYFormDataRefId(sessionRefId);
		customerFormSessionData.setFormSessionData(Lists.newArrayList(formSessionData));

		Mockito.when(prePopulateStrategy.getCustomerFormData()).thenReturn(customerFormSessionData);

		final YFormDataData yFormData = new YFormDataData();
		yFormData.setId(formId3);
		yFormData.setFormDefinition(yFormDefinitionData1);
		yFormData.setContent(content);

		Mockito.when(yFormFacade.getYFormData(formId3)).thenReturn(yFormData);

		final List<FormDetailData> formDetailDataList = helper.createFormDetailData(yFormDefinitionDataList, orderCode,
				orderEntryNumber);

		Mockito.verify(yFormFacade, Mockito.times(1)).createYFormData(Mockito.eq(applicationId), Mockito.eq(formId1),
				Mockito.anyString(), Mockito.eq(YFormDataTypeEnum.DRAFT), Mockito.eq(StringUtils.EMPTY), Mockito.eq(content));

		Mockito.verify(yFormFacade, Mockito.times(1)).createYFormData(Mockito.eq(applicationId), Mockito.eq(formId1),
				Mockito.anyString(), Mockito.eq(YFormDataTypeEnum.DATA), Mockito.eq(expectedRefId), Mockito.eq(StringUtils.EMPTY));

		Assert.assertNotNull(formDetailDataList);
		Assert.assertEquals(2, formDetailDataList.size());

		final FormDetailData formDetailData1 = formDetailDataList.get(0);

		Assert.assertEquals(applicationId, formDetailData1.getApplicationId());
		Assert.assertEquals(formId1, formDetailData1.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData1.getRefId());

		final FormDetailData formDetailData2 = formDetailDataList.get(1);

		Assert.assertEquals(applicationId, formDetailData2.getApplicationId());
		Assert.assertEquals(formId2, formDetailData2.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData2.getRefId());

	}

	@Test
	public void testCreateFormDetailDataListFromSessionWithDifferentFormDefinitionAndSameVersion() throws YFormServiceException
	{
		final String applicationId = "applicationId";
		final String formId1 = "formId1";
		final String formId2 = "formId2";
		final String formId3 = "formId3";
		final String content = "content";

		final YFormDefinitionData yFormDefinitionData1 = new YFormDefinitionData();
		yFormDefinitionData1.setApplicationId(applicationId);
		yFormDefinitionData1.setFormId(formId1);
		yFormDefinitionData1.setVersion(1);

		final YFormDefinitionData yFormDefinitionData2 = new YFormDefinitionData();
		yFormDefinitionData2.setApplicationId(applicationId);
		yFormDefinitionData2.setFormId(formId2);
		yFormDefinitionData2.setVersion(2);

		final Integer orderEntryNumber = new Integer(2);
		final String orderCode = "orderCode";

		final String expectedRefId = orderCode + "_" + orderEntryNumber;
		final String sessionRefId = orderCode + "_" + (orderEntryNumber + 1);

		final List<YFormDefinitionData> yFormDefinitionDataList = Lists.newArrayList(yFormDefinitionData1, yFormDefinitionData2);

		Mockito.when(prePopulateStrategy.hasCustomerFormDataStored()).thenReturn(Boolean.TRUE);

		final CustomerFormSessionData customerFormSessionData = new CustomerFormSessionData();
		final FormSessionData formSessionData = new FormSessionData();
		formSessionData.setYFormDataId(formId3);
		formSessionData.setYFormDefinition(yFormDefinitionData1);
		formSessionData.setYFormDataRefId(sessionRefId);
		customerFormSessionData.setFormSessionData(Lists.newArrayList(formSessionData));

		Mockito.when(prePopulateStrategy.getCustomerFormData()).thenReturn(customerFormSessionData);

		final YFormDataData yFormData = new YFormDataData();
		yFormData.setId(formId3);
		yFormData.setFormDefinition(yFormDefinitionData2);
		yFormData.setContent(content);

		Mockito.when(yFormFacade.getYFormData(formId3, YFormDataTypeEnum.DRAFT)).thenReturn(yFormData);

		final List<FormDetailData> formDetailDataList = helper.createFormDetailData(yFormDefinitionDataList, orderCode,
				orderEntryNumber);

		Mockito.verify(yFormFacade, Mockito.never()).createYFormData(Mockito.eq(applicationId), Mockito.eq(formId1),
				Mockito.anyString(), Mockito.eq(YFormDataTypeEnum.DRAFT), Mockito.eq(StringUtils.EMPTY), Mockito.eq(content));

		Mockito.verify(yFormFacade, Mockito.never()).createYFormData(Mockito.eq(applicationId), Mockito.eq(formId1),
				Mockito.anyString(), Mockito.eq(YFormDataTypeEnum.DATA), Mockito.eq(expectedRefId), Mockito.eq(StringUtils.EMPTY));

		Assert.assertNotNull(formDetailDataList);
		Assert.assertEquals(2, formDetailDataList.size());

		final FormDetailData formDetailData1 = formDetailDataList.get(0);

		Assert.assertEquals(applicationId, formDetailData1.getApplicationId());
		Assert.assertEquals(formId1, formDetailData1.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData1.getRefId());

		final FormDetailData formDetailData2 = formDetailDataList.get(1);

		Assert.assertEquals(applicationId, formDetailData2.getApplicationId());
		Assert.assertEquals(formId2, formDetailData2.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData2.getRefId());

	}

	@Test
	public void testCreateFormDetailDataListFromSessionWithSameFormDefinitionAndDifferentVersion() throws YFormServiceException
	{
		final String applicationId = "applicationId";
		final String formId1 = "formId1";
		final String formId2 = "formId2";
		final String formId3 = "formId3";
		final String content = "content";

		final YFormDefinitionData yFormDefinitionData1 = new YFormDefinitionData();
		yFormDefinitionData1.setApplicationId(applicationId);
		yFormDefinitionData1.setFormId(formId1);
		yFormDefinitionData1.setVersion(1);

		final YFormDefinitionData yFormDefinitionData2 = new YFormDefinitionData();
		yFormDefinitionData2.setApplicationId(applicationId);
		yFormDefinitionData2.setFormId(formId2);
		yFormDefinitionData2.setVersion(2);

		final Integer orderEntryNumber = new Integer(2);
		final String orderCode = "orderCode";

		final String expectedRefId = orderCode + "_" + orderEntryNumber;
		final String sessionRefId = orderCode + "_" + (orderEntryNumber + 1);

		final List<YFormDefinitionData> yFormDefinitionDataList = Lists.newArrayList(yFormDefinitionData1, yFormDefinitionData2);

		Mockito.when(prePopulateStrategy.hasCustomerFormDataStored()).thenReturn(Boolean.TRUE);

		final CustomerFormSessionData customerFormSessionData = new CustomerFormSessionData();
		final FormSessionData formSessionData = new FormSessionData();
		formSessionData.setYFormDataId(formId3);
		formSessionData.setYFormDefinition(yFormDefinitionData1);
		formSessionData.setYFormDataRefId(sessionRefId);
		customerFormSessionData.setFormSessionData(Lists.newArrayList(formSessionData));

		Mockito.when(prePopulateStrategy.getCustomerFormData()).thenReturn(customerFormSessionData);

		final YFormDefinitionData yFormDefinitionData1Version2 = new YFormDefinitionData();
		yFormDefinitionData1Version2.setApplicationId(applicationId);
		yFormDefinitionData1Version2.setFormId(formId1);
		yFormDefinitionData1Version2.setVersion(2);

		final YFormDataData yFormData = new YFormDataData();
		yFormData.setId(formId3);
		yFormData.setFormDefinition(yFormDefinitionData1Version2);
		yFormData.setContent(content);

		Mockito.when(yFormFacade.getYFormData(formId3, YFormDataTypeEnum.DATA)).thenReturn(yFormData);

		final List<FormDetailData> formDetailDataList = helper.createFormDetailData(yFormDefinitionDataList, orderCode,
				orderEntryNumber);

		Mockito.verify(yFormFacade, Mockito.never()).createYFormData(Mockito.eq(applicationId), Mockito.eq(formId1),
				Mockito.anyString(), Mockito.eq(YFormDataTypeEnum.DRAFT), Mockito.eq(StringUtils.EMPTY), Mockito.eq(content));

		Mockito.verify(yFormFacade, Mockito.never()).createYFormData(Mockito.eq(applicationId), Mockito.eq(formId1),
				Mockito.anyString(), Mockito.eq(YFormDataTypeEnum.DATA), Mockito.eq(expectedRefId), Mockito.eq(StringUtils.EMPTY));

		Assert.assertNotNull(formDetailDataList);
		Assert.assertEquals(2, formDetailDataList.size());

		final FormDetailData formDetailData1 = formDetailDataList.get(0);

		Assert.assertEquals(applicationId, formDetailData1.getApplicationId());
		Assert.assertEquals(formId1, formDetailData1.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData1.getRefId());

		final FormDetailData formDetailData2 = formDetailDataList.get(1);

		Assert.assertEquals(applicationId, formDetailData2.getApplicationId());
		Assert.assertEquals(formId2, formDetailData2.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData2.getRefId());

	}

	@Test
	public void testCreateFormDetailDataListEmptyList()
	{
		final Integer orderEntryNumber = new Integer(2);
		final String orderCode = "orderCode";

		final List<YFormDefinitionData> yFormDefinitionDataList = Lists.newArrayList();
		final List<FormDetailData> formDetailDataList = helper.createFormDetailData(yFormDefinitionDataList, orderCode,
				orderEntryNumber);

		Assert.assertNotNull(formDetailDataList);
		Assert.assertTrue(formDetailDataList.isEmpty());
	}

	@Test
	public void testGetOrderEntryDataByFormPageId()
	{
		final List<OrderEntryData> orderEntries = Lists.newArrayList();

		final OrderEntryData orderEntryData1 = new OrderEntryData();
		orderEntryData1.setBundleNo(2);
		orderEntries.add(orderEntryData1);

		final OrderEntryData orderEntryData2 = new OrderEntryData();
		orderEntryData2.setBundleNo(5);
		orderEntries.add(orderEntryData2);

		final OrderEntryData orderEntryData3 = new OrderEntryData();
		orderEntryData3.setBundleNo(7);
		orderEntries.add(orderEntryData3);

		final CartData cartData = new CartData();
		cartData.setEntries(orderEntries);

		final OrderEntryData orderEntryDataByFormPageId = helper.getOrderEntryDataByFormPageId(cartData, "5");

		Assert.assertEquals(orderEntryData2, orderEntryDataByFormPageId);

	}

	@Test
	public void testGetOrderEntryDataByFormPageIdNoOrderEntyr()
	{
		final List<OrderEntryData> orderEntries = Lists.newArrayList();

		final OrderEntryData orderEntryData1 = new OrderEntryData();
		orderEntryData1.setBundleNo(2);
		orderEntries.add(orderEntryData1);

		final OrderEntryData orderEntryData2 = new OrderEntryData();
		orderEntryData2.setBundleNo(5);
		orderEntries.add(orderEntryData2);


		final CartData cartData = new CartData();
		cartData.setEntries(orderEntries);

		final OrderEntryData orderEntryDataByFormPageId = helper.getOrderEntryDataByFormPageId(cartData, "1");

		Assert.assertNull(orderEntryDataByFormPageId);

	}

	@Test
	public void testGetFormPageIdByOrderEntryData()
	{
		final OrderEntryData orderEntryData = new OrderEntryData();
		orderEntryData.setBundleNo(2);

		Assert.assertEquals("2", helper.getFormPageIdByOrderEntryData(orderEntryData));
	}

	@Test
	public void shouldNotCloneFormDataWhenRefIdsAreSame() throws YFormServiceException
	{
		final String applicationId = "applicationId";
		final String formId1 = "formId1";
		final String formId2 = "formId2";
		final String formId3 = "formId3";
		final String content = "content";

		final YFormDefinitionData yFormDefinitionData1 = new YFormDefinitionData();
		yFormDefinitionData1.setApplicationId(applicationId);
		yFormDefinitionData1.setFormId(formId1);
		yFormDefinitionData1.setVersion(1);

		final YFormDefinitionData yFormDefinitionData2 = new YFormDefinitionData();
		yFormDefinitionData2.setApplicationId(applicationId);
		yFormDefinitionData2.setFormId(formId2);
		yFormDefinitionData2.setVersion(2);

		final Integer orderEntryNumber = new Integer(2);
		final String orderCode = "orderCode";

		final String expectedRefId = orderCode + "_" + orderEntryNumber;
		final String sessionRefId = expectedRefId;

		final List<YFormDefinitionData> yFormDefinitionDataList = Lists.newArrayList(yFormDefinitionData1, yFormDefinitionData2);

		Mockito.when(prePopulateStrategy.hasCustomerFormDataStored()).thenReturn(Boolean.TRUE);

		final CustomerFormSessionData customerFormSessionData = new CustomerFormSessionData();
		final FormSessionData formSessionData = new FormSessionData();
		formSessionData.setYFormDataId(formId3);
		formSessionData.setYFormDefinition(yFormDefinitionData1);
		formSessionData.setYFormDataRefId(sessionRefId);
		customerFormSessionData.setFormSessionData(Lists.newArrayList(formSessionData));

		Mockito.when(prePopulateStrategy.getCustomerFormData()).thenReturn(customerFormSessionData);

		final YFormDataData yFormData = new YFormDataData();
		yFormData.setId(formId3);
		yFormData.setFormDefinition(yFormDefinitionData1);
		yFormData.setContent(content);

		Mockito.when(yFormFacade.getYFormData(formId3)).thenReturn(yFormData);

		final List<FormDetailData> formDetailDataList = helper.createFormDetailData(yFormDefinitionDataList, orderCode,
				orderEntryNumber);

		Mockito.verify(yFormFacade, Mockito.never()).createYFormData(Mockito.eq(applicationId), Mockito.eq(formId1),
				Mockito.anyString(), Mockito.eq(YFormDataTypeEnum.DRAFT), Mockito.eq(StringUtils.EMPTY), Mockito.eq(content));

		Mockito.verify(yFormFacade, Mockito.never()).createYFormData(Mockito.eq(applicationId), Mockito.eq(formId1),
				Mockito.anyString(), Mockito.eq(YFormDataTypeEnum.DATA), Mockito.eq(expectedRefId), Mockito.eq(StringUtils.EMPTY));

		Assert.assertNotNull(formDetailDataList);
		Assert.assertEquals(2, formDetailDataList.size());

		final FormDetailData formDetailData1 = formDetailDataList.get(0);

		Assert.assertEquals(applicationId, formDetailData1.getApplicationId());
		Assert.assertEquals(formId1, formDetailData1.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData1.getRefId());

		final FormDetailData formDetailData2 = formDetailDataList.get(1);

		Assert.assertEquals(applicationId, formDetailData2.getApplicationId());
		Assert.assertEquals(formId2, formDetailData2.getFormId());
		Assert.assertEquals(expectedRefId, formDetailData2.getRefId());
	}

}
