package de.hybris.platform.platformbackoffice.labelproviders;/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TaxDiscountValueRendererTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	TaxDiscountValueRenderer taxDiscountValueRenderer;

	@Resource
	ModelService modelService;

	@Before
	public void before()
	{
		final CurrencyModel currency = modelService.create(CurrencyModel.class);
		currency.setSymbol("zł");
		currency.setDigits(4);
		currency.setIsocode("PLN");

		modelService.save(currency);
	}

	@Test
	public void absoluteTaxShouldRespectLanguageDigits()
	{
		final String renderedValue = taxDiscountValueRenderer.render("Disc-001", true, "PLN", 10.40, 0.0);
		assertThat(renderedValue).isEqualTo("Disc-001 : 10.4000 PLN");
	}

	@Test
	public void absoluteTaxShouldNotShowAppliedValue()
	{
		final String renderedValue = taxDiscountValueRenderer.render("Disc-001", true, "PLN", 10.40, 21.0);
		assertThat(renderedValue).isEqualTo("Disc-001 : 10.4000 PLN");
	}

	@Test(expected = IllegalArgumentException.class)
	public void absoluteTaxShouldNotShowAppliedValue2()
	{
		final String renderedValue = taxDiscountValueRenderer.render("Disc-001", true, null, 10.40, 21.0);
		assertThat(renderedValue).isEqualTo("Disc-001 : 10.4000 PLN");
	}

	@Test
	public void invalidLanguageShouldFallbackToDefault()
	{
		final String renderedValue = taxDiscountValueRenderer.render("Disc-001", true, "noneSuch", 10.40, 21.0);
		assertThat(renderedValue).isEqualTo("Disc-001 : 10.40");
	}

	@Test
	public void relativeTaxShouldShowAppliedValueWith2Digits()
	{
		final String renderedValue = taxDiscountValueRenderer.render("Disc-001", false, null, 10.40, 23.12012312);
		assertThat(renderedValue).isEqualTo("Disc-001 : 10.40% = 23.12");
	}
}
