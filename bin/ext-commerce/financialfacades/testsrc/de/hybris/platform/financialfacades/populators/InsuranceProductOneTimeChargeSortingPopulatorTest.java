package de.hybris.platform.financialfacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.populators.InsuranceProductOneTimeChargeSortingPopulator;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;


/**
 * Test suite for {@link InsuranceProductOneTimeChargeSortingPopulatorTest}.
 */
@UnitTest
public class InsuranceProductOneTimeChargeSortingPopulatorTest
{

	@Mock
	private Comparator<OneTimeChargeEntryData> oneTimeChargeEntryDataComparator;

	@InjectMocks
	private InsuranceProductOneTimeChargeSortingPopulator<ProductModel, ProductData> insuranceProductOneTimeChargeSortingPopulator;

	private ProductData productData;

	@Before
	public void setup()
	{

		insuranceProductOneTimeChargeSortingPopulator = new InsuranceProductOneTimeChargeSortingPopulator<>();

		MockitoAnnotations.initMocks(this);

		final SubscriptionPricePlanData subscriptionPricePlanData = new SubscriptionPricePlanData();

		final List<OneTimeChargeEntryData> oneTimeChargeEntries = Lists.newArrayList();
		subscriptionPricePlanData.setOneTimeChargeEntries(oneTimeChargeEntries);


		productData = new ProductData();
		productData.setPrice(subscriptionPricePlanData);

		final OneTimeChargeEntryData chargeEntryDataA = new OneTimeChargeEntryData();
		chargeEntryDataA.setName("A");

		final OneTimeChargeEntryData chargeEntryDataD = new OneTimeChargeEntryData();
		chargeEntryDataD.setName("D");

		final OneTimeChargeEntryData chargeEntryDataC = new OneTimeChargeEntryData();
		chargeEntryDataC.setName("C");

		final OneTimeChargeEntryData chargeEntryDataE = new OneTimeChargeEntryData();
		chargeEntryDataE.setName("E");

		final OneTimeChargeEntryData chargeEntryDataB = new OneTimeChargeEntryData();
		chargeEntryDataB.setName("B");

		final OneTimeChargeEntryData chargeEntryDataE2 = new OneTimeChargeEntryData();
		chargeEntryDataE2.setName("E");

		subscriptionPricePlanData.setOneTimeChargeEntries(new ArrayList<OneTimeChargeEntryData>());
		subscriptionPricePlanData.getOneTimeChargeEntries().add(chargeEntryDataD);
		subscriptionPricePlanData.getOneTimeChargeEntries().add(chargeEntryDataB);
		subscriptionPricePlanData.getOneTimeChargeEntries().add(chargeEntryDataE2);
		subscriptionPricePlanData.getOneTimeChargeEntries().add(chargeEntryDataA);
		subscriptionPricePlanData.getOneTimeChargeEntries().add(chargeEntryDataE);
		subscriptionPricePlanData.getOneTimeChargeEntries().add(chargeEntryDataC);
	}

	@Test
	public void testPopulate()
	{
		Mockito.doAnswer(new Answer()
		{

			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();

				final OneTimeChargeEntryData oneTimeChargeEntryData1 = (OneTimeChargeEntryData) args[0];
				final OneTimeChargeEntryData oneTimeChargeEntryData2 = (OneTimeChargeEntryData) args[1];

				return Integer.valueOf(oneTimeChargeEntryData1.getName().compareTo(oneTimeChargeEntryData2.getName()));
			}
		}).when(oneTimeChargeEntryDataComparator)
				.compare(Mockito.any(OneTimeChargeEntryData.class), Mockito.any(OneTimeChargeEntryData.class));

		insuranceProductOneTimeChargeSortingPopulator.populate(new ProductModel(), productData);

		final SubscriptionPricePlanData pricePlanData = (SubscriptionPricePlanData) productData.getPrice();
		Assert.assertEquals(6, pricePlanData.getOneTimeChargeEntries().size());
		Assert.assertEquals("A", pricePlanData.getOneTimeChargeEntries().get(0).getName());
		Assert.assertEquals("B", pricePlanData.getOneTimeChargeEntries().get(1).getName());
		Assert.assertEquals("C", pricePlanData.getOneTimeChargeEntries().get(2).getName());
		Assert.assertEquals("D", pricePlanData.getOneTimeChargeEntries().get(3).getName());
		Assert.assertEquals("E", pricePlanData.getOneTimeChargeEntries().get(4).getName());
		Assert.assertEquals("E", pricePlanData.getOneTimeChargeEntries().get(5).getName());
	}
}
