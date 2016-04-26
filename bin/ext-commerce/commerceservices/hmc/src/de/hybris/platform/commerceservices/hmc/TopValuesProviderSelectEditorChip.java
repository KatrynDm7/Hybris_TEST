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
package de.hybris.platform.commerceservices.hmc;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.TopValuesProvider;
import de.hybris.platform.core.Registry;
import de.hybris.platform.hmc.attribute.StringSelectEditorChip;
import de.hybris.platform.hmc.webchips.Chip;
import de.hybris.platform.hmc.webchips.DisplayState;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Populates the topValuesProvider selector on the solr indexed property configuration panel. It takes all beans of type
 * {@link TopValuesProvider} from the spring context.
 */
public class TopValuesProviderSelectEditorChip extends StringSelectEditorChip
{
	public TopValuesProviderSelectEditorChip(final DisplayState displayState, final Chip parent)
	{
		this(displayState, parent, Collections.EMPTY_LIST);
	}

	public TopValuesProviderSelectEditorChip(final DisplayState displayState, final Chip parent, final List values)
	{
		super(displayState, parent, values);
	}

	@Override
	protected List getAllValues()
	{
		return Arrays.asList(Registry.getGlobalApplicationContext().getBeanNamesForType(TopValuesProvider.class));
	}

}
