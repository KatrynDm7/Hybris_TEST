/**
 *
 */
package de.hybris.platform.financialfacades.order.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.financialfacades.facades.impl.MockPolicyServiceFacade;
import de.hybris.platform.financialfacades.strategies.PolicyPdfUrlGeneratorStrategy;
import de.hybris.platform.financialservices.model.InsurancePolicyModel;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.sql.Date;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Note : the calling of 'submitOrder' is covered in <DefaultCommerceCheckoutServiceTest> in commerceservices
 *
 */
@UnitTest
public class PolicyCreationSubmitOrderStrategyTest
{
	@InjectMocks
	private final PolicyCreationSubmitOrderStrategy submitCartStrategy = new PolicyCreationSubmitOrderStrategy();

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration cfg;

	@Mock
	private OrderModel orderModel;

	@Mock
	private InsuranceQuoteModel quoteModel;

	@Mock
	private InsurancePolicyModel policyModel;

	@Mock
	private ModelService modelService;

	@Mock
	Set<InsurancePolicyModel> mockSet;

	@Mock
	private PolicyPdfUrlGeneratorStrategy policyPdfUrlGeneratorStrategy;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testMockSubmitOrderStrategy()
	{
		final MockPolicyServiceFacade mockPolicyServiceFacade = new MockPolicyServiceFacade();
		mockPolicyServiceFacade.setDateFormat("dd-MM-yyyy");
		mockPolicyServiceFacade.setConfigurationService(configurationService);
		mockPolicyServiceFacade.setPolicyPdfUrlGeneratorStrategy(policyPdfUrlGeneratorStrategy);

		submitCartStrategy.setDateFormat("dd-MM-yyyy");
		submitCartStrategy.setPolicyServiceFacade(mockPolicyServiceFacade);

		final Date date1 = new Date(0);
		final Date date2 = new Date(10);
		final PK orderPK = PK.fromLong(123456789L);
		Mockito.when(modelService.create(InsurancePolicyModel.class)).thenReturn(policyModel);
		Mockito.when(orderModel.getInsuranceQuote()).thenReturn(quoteModel);
		Mockito.when(orderModel.getOrderPolicies()).thenReturn(mockSet);
		Mockito.when(orderModel.getPk()).thenReturn(orderPK);
		Mockito.when(orderModel.getCode()).thenReturn(orderPK.toString());
		Mockito.when(mockSet.isEmpty()).thenReturn(true);
		Mockito.when(quoteModel.getQuoteId()).thenReturn("quote-id-1");
		Mockito.when(quoteModel.getExpiryDate()).thenReturn(date2);
		Mockito.when(quoteModel.getStartDate()).thenReturn(date1);
		Mockito.when(configurationService.getConfiguration()).thenReturn(cfg);
		Mockito.when(policyPdfUrlGeneratorStrategy.generatePdfUrlForPolicy(Matchers.anyString())).thenReturn(
				"https://financialservices.local:9002/yacceleratorstorefront");

		submitCartStrategy.submitOrder(orderModel);

		Mockito.verify(modelService).save(orderModel);
		Mockito.verify(modelService).save(policyModel);

		Mockito.verify(policyModel).setPolicyId(orderPK.toString());
		Mockito.verify(policyModel).setPolicyExpiryDate(Mockito.any(Date.class));
		Mockito.verify(policyModel).setPolicyStartDate(Mockito.any(Date.class));
		Mockito.verify(policyModel).setPolicyUrl(Mockito.anyString());

	}
}
