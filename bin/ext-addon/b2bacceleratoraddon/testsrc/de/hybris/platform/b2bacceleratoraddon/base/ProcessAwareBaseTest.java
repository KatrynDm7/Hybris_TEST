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
package de.hybris.platform.b2bacceleratoraddon.base;

import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.processengine.enums.ProcessState;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.springframework.util.CollectionUtils;


@Ignore
public class ProcessAwareBaseTest extends BaseCommerceBaseTest
{
	private static final Logger LOG = Logger.getLogger(ProcessAwareBaseTest.class);

	protected boolean waitForProcessToBeCreated(final String processDefinitionName, final long maxWait)
			throws InterruptedException
	{
		final long start = System.currentTimeMillis();
		List processes = Collections.EMPTY_LIST;
		while (CollectionUtils.isEmpty(processes))
		{
			Thread.sleep(1L);
			processes = getProcesses(processDefinitionName, Arrays.asList(new ProcessState[]
			{ ProcessState.RUNNING, ProcessState.CREATED, ProcessState.WAITING }));

			if (System.currentTimeMillis() - start > maxWait)
			{
				LOG.warn(String.format("Process '%s' has not been created in %d ms.", processDefinitionName, maxWait));
				return false;
			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Process '%s' has been created in %d ms.", processDefinitionName, System.currentTimeMillis()
					- start));
		}
		return true;
	}
}
