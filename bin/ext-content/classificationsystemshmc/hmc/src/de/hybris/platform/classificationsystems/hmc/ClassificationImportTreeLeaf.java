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
package de.hybris.platform.classificationsystems.hmc;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassificationImportCronJob;
import de.hybris.platform.classificationsystems.constants.ClassificationsystemsConstants;
import de.hybris.platform.classificationsystems.jalo.ClassificationsystemsManager;
import de.hybris.platform.cronjob.hmc.CronJobTreeLeafChip;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.hmc.webchips.Chip;
import de.hybris.platform.hmc.webchips.DisplayState;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;

import java.text.MessageFormat;


public abstract class ClassificationImportTreeLeaf extends CronJobTreeLeafChip
{
	private final String name;
	private final String code;

	public ClassificationImportTreeLeaf(final DisplayState displayState, final Chip parent, final String cronJobCode,
			final String name)
	{
		super(displayState, parent);
		this.code = cronJobCode;
		this.name = name;
	}

	@Override
	public String getName()
	{
		return getDisplayState().getLocalizedString(this.name);
	}

	@Override
	public String getIcon()
	{
		return "images/ext/catalog/e_catalog_import.gif";
	}

	@Override
	protected final String getJobCode()
	{
		return ClassificationsystemsManager.DEFAULT_CLASS_IMPORT_JOB;
	}

	@Override
	protected final String getCronJobCode()
	{
		return this.code;
	}

	@Override
	protected final ComposedType getCronJobComposedType()
	{
		return TypeManager.getInstance().getComposedType(ClassificationsystemsConstants.TC.CLASSIFICATIONIMPORTCRONJOB);
	}

	@Override
	protected void edit()
	{
		final ClassificationImportCronJob cronJob = (ClassificationImportCronJob) CronJobManager.getInstance()
				.getFirstItemByAttribute(ClassificationImportCronJob.class, ClassificationImportCronJob.CODE, getCronJobCode());
		if (cronJob == null)
		{
			try
			{
				if ((getCronJobCode().equals(ClassificationsystemsConstants.ECLASS_4_1_FR)
						|| getCronJobCode().equals(ClassificationsystemsConstants.ECLASS_5_0_FR) || getCronJobCode().equals(
						ClassificationsystemsConstants.ECLASS_5_1_FR))
						&& CatalogManager.getInstance().getLanguageIfExists("fr") == null)
				{
					final String msg = MessageFormat.format(getDisplayState().getLocalizedString("classification.missing.language"),
							"fr");
					postErrorMessage(msg);
					return;
				}
				else
				{
					ClassificationsystemsManager.getInstance().createClassificationImportCronJob(getCronJobCode());
				}
			}
			catch (final Exception e)
			{
				postErrorMessage(e.getMessage());
				return;
			}
		}
		super.edit();
	}
}
