/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.frontend.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.config.HostConfigService;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorservices.storefront.util.PageTitleResolver;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationFacade;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.frontend.UiGroupStatus;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;
import de.hybris.platform.sap.productconfig.frontend.breadcrumb.ProductConfigureBreadcrumbBuilder;
import de.hybris.platform.sap.productconfig.frontend.constants.Sapproductconfigb2baddonConstants;
import de.hybris.platform.sap.productconfig.frontend.validator.ConflictChecker;
import de.hybris.platform.sap.productconfig.runtime.interf.constants.SapproductconfigruntimeinterfaceConstants;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;


@UnitTest
public class ConfigureProductControllerTest extends AbstractProductConfigControllerTest
{
	@Mock
	private View mockView;
	@Mock
	private Model model;
	@Mock
	private HostConfigService hostConfigService;
	@Mock
	private StoreSessionFacade storeSessionFacade;
	@Mock
	private CMSPageService cmsPageService;
	@Mock
	private CMSSiteService cmsSiteService;
	@Mock
	private ProductService productService;
	@Mock
	private ProductFacade productFacade;
	@Mock
	private ProductConfigureBreadcrumbBuilder productConfigurationBreadcrumbBuilder;
	@Mock
	private CustomerFacade customerFacade;
	@Mock
	private PageTitleResolver pageTitleResolver;
	@Mock
	private ProductModel productModel;
	@Mock
	private ProductData productData;
	@Mock
	private ConfigurationFacade configFacade;
	@Mock
	private SessionService sessionService;
	@Mock
	private Session hybriSession;
	@Mock
	protected ConflictChecker conflictChecker;
	@Mock
	protected WebApplicationContext wac; // cached

	protected MockServletContext servletContext; // cached
	protected MockHttpServletRequest request;

	@InjectMocks
	private final ConfigureProductController configController = new ConfigureProductController();

	@Mock
	protected BindingResult bindingResults;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);


		kbKey = createKbKey();
		csticList = createCsticsList();
		configData = createConfigurationData();
	}



	@Test
	public void testUpdateConfigureProductRedirectIsCorrect() throws Exception
	{
		initializeFirstCall();

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		final String forward = configController.configureProduct(PRODUCT_CODE, null, model, request);
		assertEquals("addon:/ysapproductconfigb2baddon/pages/configuration/configurationPage", forward);
	}

	@Test
	public void testConfigureProductForwardIsCorrect() throws Exception
	{
		initializeFirstCall();
		given(configFacade.getConfiguration(configData)).willReturn(configData);
		when(Boolean.valueOf(bindingResults.hasErrors())).thenReturn(Boolean.FALSE);

		final UiStatus uiStatus = createUiStatus("1");
		given(sessionService.getAttribute(PRODUCT_SESSION_PREFIX + "YSAP_SIMPLE_POC")).willReturn(uiStatus);

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		configController.updateConfigureProduct(configData, bindingResults, model, request);

		verify(configFacade, times(1)).updateConfiguration(any(ConfigurationData.class));
	}

	@Test
	public void testUpdateConfigureWithErrors() throws Exception
	{
		initializeFirstCall();
		given(configFacade.getConfiguration(configData)).willReturn(configData);
		when(Boolean.valueOf(bindingResults.hasErrors())).thenReturn(Boolean.TRUE);

		final UiStatus uiStatus = createUiStatus("1");
		given(sessionService.getAttribute(PRODUCT_SESSION_PREFIX + "YSAP_SIMPLE_POC")).willReturn(uiStatus);

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		configController.updateConfigureProduct(configData, bindingResults, model, request);

		verify(configFacade, times(1)).updateConfiguration(any(ConfigurationData.class));
	}

	private UiStatus createUiStatus(final String configId)
	{
		final UiStatus uiStatus = new UiStatus();
		uiStatus.setConfigId(configId);
		final List<UiGroupStatus> uiGroups = new ArrayList<>();
		uiStatus.setGroups(uiGroups);
		return uiStatus;
	}

	@Test
	public void testBreadcrumbIsSet() throws Exception
	{
		initializeFirstCall();

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		configController.configureProduct(PRODUCT_CODE, null, model, request);
		Mockito.verify(model).addAttribute(Mockito.eq(WebConstants.BREADCRUMBS_KEY), Mockito.any(List.class));
	}

	@Test
	public void testProductDataIsSet() throws Exception
	{
		initializeFirstCall();

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		configController.configureProduct(PRODUCT_CODE, null, model, request);
		Mockito.verify(model).addAttribute(Mockito.eq("product"), Mockito.any(ProductData.class));
	}

	@SuppressWarnings("unchecked")
	private void initializeFirstCall() throws Exception
	{
		servletContext = new MockServletContext();
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);

		request = new MockHttpServletRequest(servletContext);

		given(productData.getCode()).willReturn(PRODUCT_CODE);
		given(productModel.getCode()).willReturn(PRODUCT_CODE);

		given(sessionService.getCurrentSession()).willReturn(hybriSession);
		given(hybriSession.getSessionId()).willReturn("1");

		given(configFacade.getConfiguration(any(KBKeyData.class))).willReturn(configData);
		given(configFacade.getConfiguration(any(ConfigurationData.class))).willReturn(configData);
		given(productConfigurationBreadcrumbBuilder.getBreadcrumbs(any(ProductModel.class))).willReturn(createBreadcrumbs());
		given(productService.getProductForCode(PRODUCT_CODE)).willReturn(productModel);
		given(productFacade.getProductForOptions(any(ProductModel.class), any(Collection.class))).willReturn(productData);

		given(storeSessionFacade.getCurrentCurrency()).willReturn(createCurrencyData());
		given(cmsPageService.getPageForProduct(any(ProductModel.class))).willReturn(new ProductPageModel());
		given(pageTitleResolver.resolveProductPageTitle(any(ProductModel.class))).willReturn("TEST");
	}

	private List<Breadcrumb> createBreadcrumbs()
	{
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb("productUlr", "test", null));
		return breadcrumbs;
	}


	@Test
	public void testCreateKBKey() throws Exception
	{
		when(productModel.getCode()).thenReturn(PRODUCT_CODE);

		final KBKeyData actualKBKey = configController.createKBKeyForProduct(productModel);

		assertEquals("Must be equals", kbKey.getProductCode(), actualKBKey.getProductCode());
		assertEquals("Must be equals", kbKey.getKbName(), actualKBKey.getKbName());
		assertEquals("Must be equals", kbKey.getKbVersion(), actualKBKey.getKbVersion());
		assertEquals("Must be equals", kbKey.getKbLogsys(), actualKBKey.getKbLogsys());
	}

	@Test
	public void testRemoveNullCStics_InGroup()
	{
		final String name = "XYZ";
		final List<CsticData> dirtyList = createDirtyListWithCstic(name);

		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData group = new UiGroupData();
		group.setCstics(dirtyList);
		groups.add(group);
		assertTrue("Must be 2 groups", groups.get(0).getCstics().size() > 1);

		configController.removeNullCstics(groups);
		assertEquals(1, groups.get(0).getCstics().size());
		Assert.assertEquals("Wrong cstic found", name, groups.get(0).getCstics().get(0).getName());
	}


	protected List<CsticData> createDirtyListWithCstic(final String name)
	{
		final CsticData csticWithNameAndValue = new CsticData();
		csticWithNameAndValue.setName(name);
		csticWithNameAndValue.setValue("value");
		final CsticData emptyCstic = new CsticData();

		final List<CsticData> dirtyList = new ArrayList<>();
		dirtyList.add(emptyCstic);
		dirtyList.add(csticWithNameAndValue);
		dirtyList.add(emptyCstic);
		return dirtyList;
	}

	@Test
	public void testRemoveNullCStics_InSubGroup()
	{
		final String name = "XYZ";
		final List<CsticData> dirtyList = createDirtyListWithCstic(name);

		final List<UiGroupData> subGroups = new ArrayList<>();
		UiGroupData group = new UiGroupData();
		group.setCstics(dirtyList);
		subGroups.add(group);

		final ArrayList<UiGroupData> groups = new ArrayList<>();
		group = new UiGroupData();
		group.setSubGroups(subGroups);
		groups.add(group);

		assertTrue("Must be 2 groups", groups.get(0).getSubGroups().get(0).getCstics().size() > 1);

		configController.removeNullCstics(groups);

		final UiGroupData uiGroup = groups.get(0).getSubGroups().get(0);
		assertEquals(1, uiGroup.getCstics().size());
		Assert.assertSame(name, uiGroup.getCstics().get(0).getName());
	}

	@Test
	public void testHandleValidationErrorsBeforeUpdate_noErr()
	{
		final BindingResult bindingResult = new BeanPropertyBindingResult(configData,
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);
		final Map<String, FieldError> inputToRestore = configController.handleValidationErrorsBeforeUpdate(configData,
				bindingResult);
		assertEquals(0, inputToRestore.size());
	}

	@Test
	public void testHandleValidationErrorsBeforeUpdate_error()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setValue("aaa");
		numericCstic.setCsticStatus(CsticStatusType.ERROR);

		final BindingResult bindingResult = new BeanPropertyBindingResult(configData,
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);
		final FieldError error = createErrorForCstic3();
		bindingResult.addError(error);

		final Map<String, FieldError> inputToRestore = configController.handleValidationErrorsBeforeUpdate(configData,
				bindingResult);

		assertEquals(1, inputToRestore.size());
		assertSame(error, inputToRestore.get("root.WCEM_NUMERIC"));
		assertEquals(numericCstic.getLastValidValue(), numericCstic.getValue());

	}

	@Test
	public void testHandleValidationErrorsBeforeUpdate_error_addInput()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setType(UiType.RADIO_BUTTON_ADDITIONAL_INPUT);
		numericCstic.setAdditionalValue("aaa");
		numericCstic.setCsticStatus(CsticStatusType.ERROR);

		final BindingResult bindingResult = new BeanPropertyBindingResult(configData,
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);
		final FieldError error = createErrorForCstic3();
		bindingResult.addError(error);

		final Map<String, FieldError> inputToRestore = configController.handleValidationErrorsBeforeUpdate(configData,
				bindingResult);

		assertEquals(1, inputToRestore.size());
		assertSame(error, inputToRestore.get("root.WCEM_NUMERIC"));
		assertEquals(numericCstic.getLastValidValue(), numericCstic.getValue());
		assertEquals("", numericCstic.getAdditionalValue());

	}


	protected FieldError createErrorForCstic3()
	{
		final FieldError error = new FieldError(Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, "groups[0].cstics[3]", "aaa",
				false, new String[]
				{ "msg.key" }, null, "error msg");
		return error;
	}

	@Test
	public void testHandleValidationErrorsBeforeUpdate_findErrorInSubgroup0Cstic0()
	{
		final List<UiGroupData> subGroups = createCsticsGroup();
		final String csticKey = subGroups.get(0).getCstics().get(0).getKey();
		configData.getGroups().get(0).setSubGroups(subGroups);
		final CsticData numericCstic = subGroups.get(0).getCstics().get(0);
		numericCstic.setValue("aaa");
		numericCstic.setCsticStatus(CsticStatusType.ERROR);

		final BindingResult bindingResult = new BeanPropertyBindingResult(configData,
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);
		final FieldError error = createErrorForSubgoup0Cstic0();
		bindingResult.addError(error);

		final Map<String, FieldError> inputToRestore = configController.handleValidationErrorsBeforeUpdate(configData,
				bindingResult);

		assertEquals(1, inputToRestore.size());
		assertSame(error, inputToRestore.get(csticKey));
		assertEquals(numericCstic.getLastValidValue(), numericCstic.getValue());

	}


	protected FieldError createErrorForSubgoup0Cstic0()
	{
		final FieldError error = new FieldError(Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE,
				"groups[0].subGroups[0].cstics[0]", "aaa", false, new String[]
				{ "msg.key" }, null, "error msg");
		return error;
	}

	@Test
	public void testRestoreValidationErrorsAfterUpdate_errorInSubgroup0Cstic0()
	{
		final List<UiGroupData> subGroups = createCsticsGroup();
		final String csticKey = subGroups.get(0).getCstics().get(0).getKey();
		configData.getGroups().get(0).setSubGroups(subGroups);
		final CsticData numericCstic = subGroups.get(0).getCstics().get(0);

		final FieldError error = createErrorForSubgoup0Cstic0();
		final Map<String, FieldError> userInputToRestore = new HashMap<>();
		userInputToRestore.put(csticKey, error);

		final BindingResult errors = configController.restoreValidationErrorsAfterUpdate(userInputToRestore, configData);

		assertEquals(1, errors.getErrorCount());
		assertEquals("CStic should have an error", CsticStatusType.ERROR, numericCstic.getCsticStatus());

	}

	@Test
	public void testRestoreValidationErrorsAfterUpdate_visibleCstic()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setAdditionalValue("");
		final FieldError error = createErrorForCstic3();

		final Map<String, FieldError> userInputToRestore = new HashMap<>();
		userInputToRestore.put("root.WCEM_NUMERIC", error);
		final BindingResult errors = configController.restoreValidationErrorsAfterUpdate(userInputToRestore, configData);

		assertEquals(1, errors.getErrorCount());
		assertEquals("CStic should have an error", CsticStatusType.ERROR, numericCstic.getCsticStatus());
		assertEquals("aaa", numericCstic.getValue());
		assertEquals("", numericCstic.getAdditionalValue());


	}

	@Test
	public void testRestoreValidationErrorsAfterUpdate_visibleCstic_addInput()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setType(UiType.RADIO_BUTTON_ADDITIONAL_INPUT);
		final FieldError error = createErrorForCstic3();

		final Map<String, FieldError> userInputToRestore = new HashMap<>();
		userInputToRestore.put("root.WCEM_NUMERIC", error);
		final BindingResult errors = configController.restoreValidationErrorsAfterUpdate(userInputToRestore, configData);

		assertEquals(1, errors.getErrorCount());
		assertEquals("CStic should have an error", CsticStatusType.ERROR, numericCstic.getCsticStatus());
		assertEquals(numericCstic.getLastValidValue(), numericCstic.getValue());
		assertEquals("aaa", numericCstic.getAdditionalValue());


	}

	@Test
	public void testRestoreValidationErrorsAfterUpdate_invisibleCstic()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setVisible(false);
		final FieldError error = createErrorForCstic3();

		final Map<String, FieldError> userInputToRestore = new HashMap<>();
		userInputToRestore.put("root.WCEM_NUMERIC", error);
		final BindingResult errors = configController.restoreValidationErrorsAfterUpdate(userInputToRestore, configData);

		assertEquals(0, errors.getErrorCount());

	}

	@Test
	public void testGetConfigForRestoredProduct()
	{
		final ConfigurationData configData = configController.getConfigDataForRestoredProduct(kbKey, "id");
		Assert.assertNull(configData);
	}

	@Test
	public void testGetConfigForRestoredProductWithNoConfiguration() throws Exception
	{
		initializeFirstCall();
		final ConfigurationData configData = configController.getConfigDataForRestoredProduct(kbKey, null);
		Assert.assertNotNull(configData);
	}

	@Test
	public void testUiStatusFromSessionInCaseOfRestore() throws Exception
	{
		initializeFirstCall();
		given(sessionService.getAttribute(SapproductconfigruntimeinterfaceConstants.PRODUCT_CONFIG_SESSION_PREFIX + "TR"))
				.willReturn("confId");

		final UiStatus stat = configController.getUiStatusFromSession("TR", kbKey);
		Assert.assertNotNull(stat);
		//assertEquals("confId", stat.getConfigId());
	}

	@Test
	public void testUiStatusFromSession() throws Exception
	{
		initializeFirstCall();

		final UiStatus uiStatus = createUiStatus("1");
		given(sessionService.getAttribute(AbstractProductConfigController.CART_ITEM_HANDLE_SESSION_PREFIX + "IT")).willReturn(
				uiStatus);

		final UiStatus stat = configController.getUiStatusFromSession("IT", null);
		assertEquals(uiStatus, stat);
	}

	@Test
	public void testGroupStatusReset()
	{
		final ConfigurationData configData = new ConfigurationData();

		final List<UiGroupData> groups = new ArrayList<>();
		groups.add(createCsticGroup("1", GroupStatusType.ERROR, true));
		groups.add(createCsticGroup("2", GroupStatusType.WARNING, true));
		groups.add(createCsticGroup("3", GroupStatusType.FLAG, true));
		configData.setGroups(groups);

		configController.resetGroupStatus(configData);

		for (final UiGroupData group : configData.getGroups())
		{
			assertEquals(GroupStatusType.DEFAULT, group.getGroupStatus());
		}
	}


}
