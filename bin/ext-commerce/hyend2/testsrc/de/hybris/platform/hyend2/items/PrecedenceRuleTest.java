/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.hyend2.items;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.hyend2.recordstore.constants.PrecedenceRules;

import org.junit.Test;

import com.endeca.itl.record.Record;


/**
 * @author michal.flasinski
 * 
 */
@UnitTest
public class PrecedenceRuleTest
{
	@Test
	public void testConvertPrecedenceRuleToRecords()
	{
		final PrecedenceRule rule1 = new PrecedenceRule("product.category:digital_cameras:product.screen_size", "product.category",
				"tvs", "product.screen_size", "standard");

		final Record record = rule1.getRecord();

		assertThat(record.getAllPropertyValues()).hasSize(5);
		assertThat(record.getPropertySingleValue(PrecedenceRules.QUALIFIED_SPEC).getValue()).isEqualTo(
				"product.category:digital_cameras:product.screen_size");
		assertThat(record.getPropertySingleValue(PrecedenceRules.SOURCE_DIMENSION_NAME).getValue()).isEqualTo("product.category");
		assertThat(record.getPropertySingleValue(PrecedenceRules.SOURCE_DIMVAL_SPEC).getValue()).isEqualTo("tvs");
		assertThat(record.getPropertySingleValue(PrecedenceRules.TARGET_DIMENSION_NAME).getValue())
				.isEqualTo("product.screen_size");
		assertThat(record.getPropertySingleValue(PrecedenceRules.TYPE).getValue()).isEqualTo("standard");
	}
}
