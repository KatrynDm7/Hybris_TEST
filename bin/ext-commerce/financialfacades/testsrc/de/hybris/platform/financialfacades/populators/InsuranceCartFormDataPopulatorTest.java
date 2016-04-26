package de.hybris.platform.financialfacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.financialfacades.strategies.CustomerFormPrePopulateStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * The class of InsuranceCartFormDataPopulatorTest.
 */
@UnitTest
public class InsuranceCartFormDataPopulatorTest
{

	@Mock
	private CustomerFormPrePopulateStrategy customerFormPrePopulateStrategy;

	@InjectMocks
	private InsuranceCartFormDataPopulator insuranceCartFormDataPopulator;


	@Before
	public void setup()
	{
		insuranceCartFormDataPopulator = new InsuranceCartFormDataPopulator();

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldCartDataHasSessionForm()
	{
		final CartModel cartModel = Mockito.mock(CartModel.class);
		final CartData cartData = new CartData();

		Mockito.when(customerFormPrePopulateStrategy.hasCustomerFormDataStored()).thenReturn(Boolean.TRUE);

		insuranceCartFormDataPopulator.populate(cartModel, cartData);

		Assert.assertTrue(cartData.getHasSessionFormData());
	}

	@Test
	public void shouldCartDataHasNotSessionForm()
	{
		final CartModel cartModel = Mockito.mock(CartModel.class);
		final CartData cartData = new CartData();

		Mockito.when(customerFormPrePopulateStrategy.hasCustomerFormDataStored()).thenReturn(Boolean.FALSE);

		insuranceCartFormDataPopulator.populate(cartModel, cartData);

		Assert.assertFalse(cartData.getHasSessionFormData());
	}
}
