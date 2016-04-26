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
package de.hybris.platform.hyend2.si.prules;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.hyend2.items.PrecedenceRule;

import java.util.List;

import org.junit.Test;

import com.endeca.itl.record.Record;
import com.google.common.collect.Lists;


/**
 * @author michal.flasinski
 * 
 */
@UnitTest
public class PrecedenceRulesToRecordsConverterTest
{
	PrecedenceRulesToRecordsConverter precedenceRulesToRecordsConverter = new PrecedenceRulesToRecordsConverter();

	@Test
	public void testConvertRecords()
	{
		final PrecedenceRule rule1 = new PrecedenceRule("id1", "Product.Categories", "HW2100", "Product.clockSpeed", "standard");
		final PrecedenceRule rule2 = new PrecedenceRule("id2", "Product.Categories", "HW2300", "Product.size", "leaf");

		final List<Record> records = precedenceRulesToRecordsConverter.convert(Lists.newArrayList(rule1, rule2));

		assertThat(records).hasSize(2);
	}
}
