package de.hybris.platform.warehousing.sourcing.util;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;
import de.hybris.platform.warehousing.sourcing.factor.SourcingFactorFactory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;


public class SourcingConfigurator implements InitializingBean
{
	private SourcingFactorFactory sourcingFactorFactory;
	private Map<SourcingFactorIdentifiersEnum, Integer> defaultFactorWeights;

	public void setFactorWeight(final SourcingFactorIdentifiersEnum factor, final int weight)
	{
		getSourcingFactorFactory().getSourcingFactor(factor).setWeight(weight);
	}

	public void assertFactorWeight(final SourcingFactorIdentifiersEnum factor, final Integer expectedWeight)
	{
		final Integer actualWeight = getSourcingFactorFactory().getSourcingFactor(factor).getWeight();
		assertEquals(expectedWeight, actualWeight);
	}

	public void resetWeights()
	{
		getSourcingFactorFactory().getAllSourcingFactors()
				.forEach(factor -> factor.setWeight(defaultFactorWeights.get(factor.getFactorId())));
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		defaultFactorWeights = new HashMap<>();
		getSourcingFactorFactory().getAllSourcingFactors()
				.forEach(factor -> defaultFactorWeights.put(factor.getFactorId(), Integer.valueOf(factor.getWeight())));
	}

	public SourcingFactorFactory getSourcingFactorFactory()
	{
		return sourcingFactorFactory;
	}

	@Required
	public void setSourcingFactorFactory(final SourcingFactorFactory sourcingFactorFactory)
	{
		this.sourcingFactorFactory = sourcingFactorFactory;
	}

}
