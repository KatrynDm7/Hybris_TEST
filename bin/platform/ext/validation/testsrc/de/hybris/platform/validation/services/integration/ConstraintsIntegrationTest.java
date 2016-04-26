/*
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
 *
 */
package de.hybris.platform.validation.services.integration;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.validation.enums.RegexpFlag;
import de.hybris.platform.validation.model.constraints.jsr303.PatternConstraintModel;
import de.hybris.platform.validation.services.ValidationService;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class ConstraintsIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Resource
	private ValidationService validationService;

	@Test
	public void shouldNotUseRemovedConstraint()
	{
		final PatternConstraintModel constraint = createConstraint("requiredtitle");
		modelService.save(constraint);
		validationService.reloadValidationEngine();

		final TitleModel goodTitle = modelService.create(TitleModel.class);
		goodTitle.setCode("requiredtitle");
		modelService.save(goodTitle);

		final TitleModel badTitle = modelService.create(TitleModel.class);
		badTitle.setCode(":(");

		try
		{
			modelService.save(badTitle);
			fail("Validation exception was expexted");
		}
		catch (final ModelSavingException mse)
		{
			assertThat(mse.getMessage()).containsIgnoringCase("requiredtitle");
		}

		modelService.remove(constraint);
		validationService.reloadValidationEngine();

		final TitleModel nowGoodTitle = modelService.create(TitleModel.class);
		nowGoodTitle.setCode(":)");
		modelService.save(nowGoodTitle);
	}

	private <T extends ItemModel> T createConstraint(final String pattern)
	{
		final PatternConstraintModel constraint = modelService.create(PatternConstraintModel.class);
		constraint.setId("failOnCreate");
		constraint.setDescriptor(typeService.getAttributeDescriptor(TitleModel._TYPECODE, TitleModel.CODE));
		constraint.setType(typeService.getComposedTypeForClass(TitleModel.class));
		constraint.setRegexp(pattern);
		constraint.setFlags(Collections.singleton(RegexpFlag.DOTALL));

		return (T) constraint;
	}
}
