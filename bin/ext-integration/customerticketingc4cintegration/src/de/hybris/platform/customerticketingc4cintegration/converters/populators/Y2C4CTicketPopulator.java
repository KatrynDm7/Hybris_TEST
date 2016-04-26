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
package de.hybris.platform.customerticketingc4cintegration.converters.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.customerticketingc4cintegration.SitePropsHolder;
import de.hybris.platform.customerticketingc4cintegration.constants.Customerticketingc4cintegrationConstants;
import de.hybris.platform.customerticketingc4cintegration.data.Note;
import de.hybris.platform.customerticketingc4cintegration.data.RelatedTransaction;
import de.hybris.platform.customerticketingc4cintegration.data.ServiceRequestData;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;


public class Y2C4CTicketPopulator<SOURCE extends TicketData, TARGET extends ServiceRequestData> implements
		Populator<SOURCE, TARGET>
{
	@Resource
	private SitePropsHolder sitePropsHolder;
	@Resource
	private Map<String, StatusData> statusMapping;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		if (source.getStatus() == null || source.getStatus().getId() == null) // creating
		{
			target.setName(source.getSubject());
			target.setDataOriginTypeCode(Customerticketingc4cintegrationConstants.DATA_ORIGIN_TYPECODE);

			if (sitePropsHolder.isB2C())
			{
				target.setExternalCustomerID(source.getCustomerId());
			}

			if (StringUtils.isNotEmpty(source.getCartId()))
			{
				final RelatedTransaction rt = new RelatedTransaction();
				rt.setID(source.getCartId());
				rt.setBusinessSystemID(sitePropsHolder.getSiteId());
				rt.setTypeCode(Customerticketingc4cintegrationConstants.RELATED_TRANSACTION_TYPECODE);
				rt.setRoleCode(Customerticketingc4cintegrationConstants.ROLE_CODE);
				target.setRelatedTransactions(Arrays.asList(rt));
			}

			final Note textData = new Note();
			textData.setText(source.getMessage());
			textData.setLanguageCode(Customerticketingc4cintegrationConstants.LANGUAGE);
			textData.setTypeCode(Customerticketingc4cintegrationConstants.TYPECODE_10004);
			target.setNotes(Arrays.asList(textData));
		}
		else
		// updating
		{
			for (final String id : statusMapping.keySet())
			{
				if (statusMapping.get(id).getId().equalsIgnoreCase(source.getStatus().getId()))
				{
					target.setStatusCode(id);
				}
			}
		}
	}
}
