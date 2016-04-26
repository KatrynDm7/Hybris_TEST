package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.insurance.data.CustomerFormSessionData;
import de.hybris.platform.commercefacades.insurance.data.FormSessionData;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
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
 * The class of DefaultCustomerFormPrePopulateStrategyTest.
 */
@UnitTest
public class DefaultCustomerFormPrePopulateStrategyTest
{

	@InjectMocks
	private DefaultCustomerFormPrePopulateStrategy customerFormPrePopulateStrategy;

	@Mock
	private SessionService sessionService;

	@Mock
	private CustomerFacade customerFacade;

	@Mock
	private CartFacade cartFacade;

	@Mock
	private Session session;

	@Before
	public void setup()
	{
		customerFormPrePopulateStrategy = new DefaultCustomerFormPrePopulateStrategy();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldHasCustomerFormDataStored()
	{
		final CustomerFormSessionData sessionData = new CustomerFormSessionData();
		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(sessionData);

		Assert.assertTrue(customerFormPrePopulateStrategy.hasCustomerFormDataStored());
	}


	@Test
	public void shouldHasNoCustomerFormDataStoredWhenNoSession()
	{
		Mockito.when(sessionService.getCurrentSession()).thenReturn(null);

		Assert.assertFalse(customerFormPrePopulateStrategy.hasCustomerFormDataStored());
	}

	@Test
	public void shouldHasNoCustomerFormDataStoredWhenNoFormInSession()
	{
		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(null);

		Assert.assertFalse(customerFormPrePopulateStrategy.hasCustomerFormDataStored());
	}

	@Test
	public void shouldRemoveStoredCustomerFormData()
	{
		final CustomerFormSessionData sessionData = new CustomerFormSessionData();
		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(sessionData);

		customerFormPrePopulateStrategy.removeStoredCustomerFormData();

		Mockito.verify(session, Mockito.times(1)).removeAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM);
	}

	@Test
	public void shouldCustomerFormDataReturn()
	{
		final String currentCustomerUid = "test_user";
		final CustomerFormSessionData sessionData = new CustomerFormSessionData();
		sessionData.setCustomerUid(currentCustomerUid);
		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(sessionData);

		final CustomerData customerData = new CustomerData();
		customerData.setUid(currentCustomerUid);
		Mockito.when(customerFacade.getCurrentCustomer()).thenReturn(customerData);

		final CustomerFormSessionData returnedSessionData = customerFormPrePopulateStrategy.getCustomerFormData();

		Assert.assertEquals(sessionData, returnedSessionData);
	}

	@Test
	public void shouldNotReturnCustomerFormDataWhenDifferentCustomer()
	{
		final String currentCustomerUid = "test_user";
		final CustomerFormSessionData sessionData = new CustomerFormSessionData();
		sessionData.setCustomerUid(currentCustomerUid);
		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(sessionData);

		final CustomerData customerData = new CustomerData();
		customerData.setUid(currentCustomerUid + " other customer");
		Mockito.when(customerFacade.getCurrentCustomer()).thenReturn(customerData);

		final CustomerFormSessionData returnedSessionData = customerFormPrePopulateStrategy.getCustomerFormData();

		Assert.assertEquals(returnedSessionData, null);
	}

	@Test
	public void shouldNotReturnCustomerFormDataWhenNoSessionCustomer()
	{
		final String currentCustomerUid = "test_user";
		final CustomerFormSessionData sessionData = new CustomerFormSessionData();
		sessionData.setCustomerUid(currentCustomerUid);
		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(sessionData);

		Mockito.when(customerFacade.getCurrentCustomer()).thenReturn(null);

		final CustomerFormSessionData returnedSessionData = customerFormPrePopulateStrategy.getCustomerFormData();

		Assert.assertEquals(returnedSessionData, null);
	}

	@Test
	public void shouldNotReturnCustomerFormDataWhenNoFormStored()
	{
		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(null);

		final CustomerFormSessionData returnedSessionData = customerFormPrePopulateStrategy.getCustomerFormData();

		Assert.assertEquals(returnedSessionData, null);
	}

	@Test
	public void shouldNotReturnCustomerFormDataWhenDifferentTypeFormDataObject()
	{
		final Object sessionData = new Object();
		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(sessionData);

		final CustomerFormSessionData returnedSessionData = customerFormPrePopulateStrategy.getCustomerFormData();

		Assert.assertEquals(returnedSessionData, null);
	}

	@Test
	public void shouldStoreCustomerFormData() throws YFormServiceException
	{
		final String currentCustomerUid = "test_user";
		final String cartCode = "cartCode";
		final String productCode = "productCode";

		final String applicationId = "applicationId";
		final String formId1 = "formId1";
		final String formId2 = "formId2";
		final String formDataId1 = "formDataId1";

		final YFormDefinitionData yFormDefinitionData1 = new YFormDefinitionData();
		yFormDefinitionData1.setApplicationId(applicationId);
		yFormDefinitionData1.setFormId(formId1);
		yFormDefinitionData1.setVersion(1);

		final YFormDefinitionData yFormDefinitionData2 = new YFormDefinitionData();
		yFormDefinitionData2.setApplicationId(applicationId);
		yFormDefinitionData2.setFormId(formId2);
		yFormDefinitionData2.setVersion(2);

		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(null);

		final CustomerData customerData = new CustomerData();
		customerData.setUid(currentCustomerUid);
		Mockito.when(customerFacade.getCurrentCustomer()).thenReturn(customerData);

		final YFormDataData yFormData = new YFormDataData();
		yFormData.setFormDefinition(yFormDefinitionData1);
		yFormData.setId(formDataId1);

		final CartData cartData = new CartData();
		cartData.setCode(cartCode);
		cartData.setEntries(Lists.<OrderEntryData> newArrayList());
		final OrderEntryData entryData = new OrderEntryData();
		entryData.setEntryNumber(new Integer(1));
		entryData.setFormDataData(Lists.<YFormDataData> newArrayList());
		entryData.getFormDataData().add(yFormData);
		final ProductData productData = new ProductData();
		productData.setCode(productCode);
		entryData.setProduct(productData);
		cartData.getEntries().add(entryData);

		Mockito.when(cartFacade.getSessionCart()).thenReturn(cartData);

		customerFormPrePopulateStrategy.storeCustomerFormData();

		Mockito.verify(session, Mockito.times(1)).setAttribute(
				Mockito.eq(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM), Mockito.any(CustomerFormSessionData.class));
	}

	@Test
	public void shouldNotStoreCustomerFormDataWhenNoFormDataModelFound() throws YFormServiceException
	{
		final String currentCustomerUid = "test_user";
		final String cartCode = "cartCode";
		final String productCode = "productCode";

		final String applicationId = "applicationId";
		final String formId1 = "formId1";
		final String formId2 = "formId2";

		final YFormDefinitionData yFormDefinitionData1 = new YFormDefinitionData();
		yFormDefinitionData1.setApplicationId(applicationId);
		yFormDefinitionData1.setFormId(formId1);
		yFormDefinitionData1.setVersion(1);

		final YFormDefinitionData yFormDefinitionData2 = new YFormDefinitionData();
		yFormDefinitionData2.setApplicationId(applicationId);
		yFormDefinitionData2.setFormId(formId2);
		yFormDefinitionData2.setVersion(2);

		Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM)).thenReturn(null);

		final CustomerData customerData = new CustomerData();
		customerData.setUid(currentCustomerUid);
		Mockito.when(customerFacade.getCurrentCustomer()).thenReturn(customerData);

		final CartData cartData = new CartData();
		cartData.setCode(cartCode);
		cartData.setEntries(Lists.<OrderEntryData> newArrayList());
		final OrderEntryData entryData = new OrderEntryData();
		entryData.setEntryNumber(new Integer(1));
		final ProductData productData = new ProductData();
		productData.setCode(productCode);
		entryData.setProduct(productData);
		cartData.getEntries().add(entryData);

		Mockito.when(cartFacade.getSessionCart()).thenReturn(cartData);

		customerFormPrePopulateStrategy.storeCustomerFormData();

		Mockito.verify(session, Mockito.never()).setAttribute(Mockito.eq(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM),
				Mockito.any(CustomerFormSessionData.class));
	}

	@Test
	public void shouldFormSessionDataListNormalized()
	{
		final String applicationId = "applicationId";
		final String formId1 = "formId1";
		final String formDataId1 = "formDataId1";

		final YFormDefinitionData yFormDefinitionData1 = new YFormDefinitionData();
		yFormDefinitionData1.setApplicationId(applicationId);
		yFormDefinitionData1.setFormId(formId1);
		yFormDefinitionData1.setVersion(1);

		final FormSessionData formSessionData1 = new FormSessionData();
		formSessionData1.setYFormDefinition(yFormDefinitionData1);
		formSessionData1.setYFormDataId(formDataId1);

		final FormSessionData formSessionData2 = new FormSessionData();
		formSessionData2.setYFormDefinition(yFormDefinitionData1);
		formSessionData2.setYFormDataId(formDataId1);

		final List<FormSessionData> source = Lists.newArrayList();
		source.add(formSessionData1);
		source.add(formSessionData2);

		final List<FormSessionData> result = customerFormPrePopulateStrategy.normalizeFormSessionData(source);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
	}

}
