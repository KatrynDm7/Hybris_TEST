/*
 *
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
 */
package de.hybris.platform.financialfacades.strategies.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyListingData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.financialfacades.populators.InsurancePolicyListPopulator;
import de.hybris.platform.financialservices.model.InsurancePolicyModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCustomerInsurancePolicyStrategyTest
{
	public static final String USER_ID = "userId";

	public static final String POLICY_ID_1 = "POLICY_ID_1";
	public static final String POLICY_ID_2 = "POLICY_ID_2";
	public static final Date POLICY_DATE_1 = new Date(0);
	public static final Date POLICY_DATE_2 = new Date(5000);
	public static final String POLICY_DATE_STR = "01-00-1970";
	public static final String POLICY_URL = "http://localhost/insurance";
	public static final String POLICY_PRICE = "100";
	public static final String POLICY_PRODUCT_NAME = "A Policy Product";

	@InjectMocks
	private DefaultCustomerInsurancePolicyStrategy defaultCustomerInsurancePolicyStrategy;

	@Mock
	private Converter<InsurancePolicyModel, InsurancePolicyListingData> policyListingConverter;

	@Mock
	private UserService userService;

	@Mock
	private UserModel userModel;

	@Mock
	private OrderModel orderModel;

	@Mock
	private OrderModel orderModel2;

	@Mock
	private AbstractOrderEntryModel entryModel;

	@Mock
	private AbstractOrderEntryModel entryModel2;

	@Mock
	private ProductModel productModel;

	@Mock
	private InsurancePolicyModel policyModel;

	@Mock
	private InsurancePolicyModel policyModel2;

	@Mock
	private InsurancePolicyListingData policyData1;

	@Mock
	private InsurancePolicyListingData policyData2;

	@Mock
	private ProductData productData;

	@Mock
	private Converter<ProductModel, ProductData> productConverter;


	@Before
	public void setup()
	{

		final InsurancePolicyListPopulator policyListingPopulator = new InsurancePolicyListPopulator();
		policyListingPopulator.setDateFormatForDisplay("dd-mm-yyyy");

		defaultCustomerInsurancePolicyStrategy = new DefaultCustomerInsurancePolicyStrategy();
		defaultCustomerInsurancePolicyStrategy.setPolicyListingConverter(policyListingConverter);
		defaultCustomerInsurancePolicyStrategy.setProductConverter(productConverter);

		MockitoAnnotations.initMocks(this);
		given(userService.getUserForUID(USER_ID)).willReturn(userModel);
		given(policyListingConverter.convert(policyModel)).willReturn(policyData1);
		given(policyListingConverter.convert(policyModel2)).willReturn(policyData2);

		given(policyData1.getPolicyNumber()).willReturn(POLICY_ID_1);
		given(policyData1.getPolicyRawStartDate()).willReturn(POLICY_DATE_1);
		given(policyData1.getPolicyRawExpiryDate()).willReturn(POLICY_DATE_1);
		given(policyData1.getPolicyStartDate()).willReturn(POLICY_DATE_STR);
		given(policyData1.getPolicyExpiryDate()).willReturn(POLICY_DATE_STR);
		given(policyData1.getPolicyUrl()).willReturn(POLICY_URL);
		given(policyData1.getPolicyPrice()).willReturn(POLICY_PRICE);
		given(policyData1.getPolicyProduct()).willReturn(productData);

		given(productData.getName()).willReturn(POLICY_PRODUCT_NAME);

		given(policyData2.getPolicyNumber()).willReturn(POLICY_ID_2);
		given(policyData2.getPolicyRawStartDate()).willReturn(POLICY_DATE_2);
		given(policyData2.getPolicyRawExpiryDate()).willReturn(POLICY_DATE_2);
		given(policyData2.getPolicyStartDate()).willReturn(POLICY_DATE_STR);
		given(policyData2.getPolicyExpiryDate()).willReturn(POLICY_DATE_STR);
		given(policyData2.getPolicyUrl()).willReturn(POLICY_URL);
	}

	@Test
	public void testNoResultsButAllsWell()
	{
		final Collection<OrderModel> orders = new HashSet<OrderModel>();
		given(userModel.getOrders()).willReturn(orders);

		final List<InsurancePolicyListingData> policies = defaultCustomerInsurancePolicyStrategy.getPolicyDataForUID(USER_ID);

		Assert.assertNotNull(policies);
		Assert.assertEquals(policies.size(), 0);
	}

	@Test
	public void testSingleOrderAndSinglePolicyResultIsFine()
	{
		final Collection<OrderModel> orders = new HashSet<OrderModel>();
		orders.add(orderModel);

		final Set<InsurancePolicyModel> policyModels = new HashSet<InsurancePolicyModel>();
		policyModels.add(policyModel);

		given(userModel.getOrders()).willReturn(orders);
		given(orderModel.getOrderPolicies()).willReturn(policyModels);
		given(orderModel.getTotalPrice()).willReturn(100d);

		final List<InsurancePolicyListingData> policies = defaultCustomerInsurancePolicyStrategy.getPolicyDataForUID(USER_ID);

		Assert.assertNotNull(policies);
		Assert.assertEquals(policies.size(), 1);
	}

	@Test
	public void testSingleOrderAndTwoPoliciesResultIsFine()
	{
		final Collection<OrderModel> orders = new HashSet<OrderModel>();
		orders.add(orderModel);

		final Set<InsurancePolicyModel> policyModels = new HashSet<InsurancePolicyModel>();
		policyModels.add(policyModel);
		policyModels.add(policyModel2);

		final List<AbstractOrderEntryModel> entryModels = new ArrayList<AbstractOrderEntryModel>();
		entryModels.add(entryModel);

		given(userModel.getOrders()).willReturn(orders);
		given(orderModel.getOrderPolicies()).willReturn(policyModels);
		given(orderModel.getEntries()).willReturn(entryModels);
		given(entryModel.getProduct()).willReturn(productModel);
		given(productModel.getName()).willReturn("product");

		final List<InsurancePolicyListingData> policies = defaultCustomerInsurancePolicyStrategy.getPolicyDataForUID(USER_ID);

		Assert.assertNotNull(policies);
		Assert.assertEquals(policies.size(), 2);
		Assert.assertEquals(policies.get(0).getPolicyNumber(), POLICY_ID_1);
		Assert.assertEquals(policies.get(1).getPolicyNumber(), POLICY_ID_2);
	}

	@Test
	public void testTwoOrdersEachWithOnePolicyIsFine()
	{
		final Collection<OrderModel> orders = new HashSet<OrderModel>();
		orders.add(orderModel);
		orders.add(orderModel2);

		final Set<InsurancePolicyModel> policyModels1 = new HashSet<InsurancePolicyModel>();
		policyModels1.add(policyModel);

		final Set<InsurancePolicyModel> policyModels2 = new HashSet<InsurancePolicyModel>();
		policyModels2.add(policyModel2);

		final List<AbstractOrderEntryModel> entryModels1 = new ArrayList<AbstractOrderEntryModel>();
		entryModels1.add(entryModel);

		final List<AbstractOrderEntryModel> entryModels2 = new ArrayList<AbstractOrderEntryModel>();
		entryModels2.add(entryModel2);

		given(userModel.getOrders()).willReturn(orders);
		given(orderModel.getOrderPolicies()).willReturn(policyModels1);
		given(orderModel.getEntries()).willReturn(entryModels1);
		given(entryModel.getProduct()).willReturn(productModel);

		given(orderModel2.getOrderPolicies()).willReturn(policyModels2);
		given(orderModel2.getEntries()).willReturn(entryModels2);
		given(entryModel2.getProduct()).willReturn(productModel);

		given(productModel.getName()).willReturn("product");

		final List<InsurancePolicyListingData> policies = defaultCustomerInsurancePolicyStrategy.getPolicyDataForUID(USER_ID);

		Assert.assertNotNull(policies);
		Assert.assertEquals(policies.size(), 2);
		Assert.assertEquals(policies.get(0).getPolicyNumber(), POLICY_ID_1);
		Assert.assertEquals(policies.get(1).getPolicyNumber(), POLICY_ID_2);
	}
}
