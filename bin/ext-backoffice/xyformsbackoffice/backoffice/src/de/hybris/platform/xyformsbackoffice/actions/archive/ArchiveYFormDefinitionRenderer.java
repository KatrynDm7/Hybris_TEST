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
 */
package de.hybris.platform.xyformsbackoffice.actions.archive;

import static de.hybris.platform.xyformsbackoffice.constants.XyformsbackofficeConstants.BUILDER;
import static de.hybris.platform.xyformsbackoffice.constants.XyformsbackofficeConstants.ORBEON;

import de.hybris.platform.xyformsservices.enums.YFormDefinitionStatusEnum;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import org.apache.commons.lang.StringUtils;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;


public class ArchiveYFormDefinitionRenderer extends DefaultActionRenderer<java.lang.String, java.lang.Object>
{
	protected static final String ACTION_ON_ICON_URI = "icons/icon_action_archive_on_default.png";
	protected static final String ACTION_ON_DISABLED_ICON_URI = "icons/icon_action_archive_on_disabled.png";
	protected static final String ACTION_OFF_ICON_URI = "icons/icon_action_archive_off_default.png";
	protected static final String ACTION_OFF_DISABLED_ICON_URI = "icons/icon_action_archive_off_disabled.png";

	protected static final String UNARCHIVE_TITLE = "title.unarchive";
	protected static final String ARCHIVE_TITLE = "title.archive";

	public boolean isArchived(final ActionContext<?> ctx)
	{
		boolean archived = false;

		if (ctx.getData() instanceof YFormDefinitionModel)
		{
			final YFormDefinitionModel yformDefinition = (YFormDefinitionModel) ctx.getData();
			archived = YFormDefinitionStatusEnum.DISABLED.equals(yformDefinition.getStatus());

			// the form builder cannot be enabled, since it is only a placeholder
			archived = archived
					&& !(ORBEON.equals(yformDefinition.getApplicationId()) && BUILDER.equals(yformDefinition.getFormId()));
		}

		return archived;
	}

	@Override
	protected String getIconUri(final ActionContext context, final boolean canPerform)
	{
		String iconURI;
		final boolean isArchived = isArchived(context);
		if (canPerform)
		{
			if (isArchived)
			{
				iconURI = ACTION_OFF_ICON_URI;
			}
			else
			{
				iconURI = ACTION_ON_ICON_URI;
			}
		}
		else
		{
			if (isArchived)
			{
				iconURI = ACTION_OFF_DISABLED_ICON_URI;
			}
			else
			{
				iconURI = ACTION_ON_DISABLED_ICON_URI;
			}
		}

		if (!StringUtils.isBlank(iconURI))
		{
			if (!(iconURI.charAt(0) == '/'))
			{
				iconURI = "/" + iconURI;
			}
			iconURI = context.getParameter(CockpitWidgetEngine.COMPONENT_ROOT_PARAM) + iconURI;
		}
		return iconURI;
	}

	@Override
	protected String getLocalizedName(final ActionContext<?> context)
	{

		return isArchived(context) ? context.getLabel(UNARCHIVE_TITLE) : context.getLabel(ARCHIVE_TITLE);
	}

}