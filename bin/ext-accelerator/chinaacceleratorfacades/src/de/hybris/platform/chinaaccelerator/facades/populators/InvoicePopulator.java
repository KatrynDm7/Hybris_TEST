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
package de.hybris.platform.chinaaccelerator.facades.populators;



import de.hybris.platform.chinaaccelerator.services.model.invoice.InvoiceModel;
import de.hybris.platform.commercefacades.order.data.InvoiceData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class InvoicePopulator<SOURCE extends InvoiceModel, TARGET extends InvoiceData> implements
		Populator<InvoiceModel, InvoiceData>
{


	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final InvoiceModel source, final InvoiceData target) throws ConversionException
	{
		if (source != null)
		{
			target.setInvoicedTitle(source.getTitle() == null ? null : source.getTitle().getCode());
			target.setInvoicedName(source.getInvoicedName());
			target.setInvoicedCategory(source.getCategory() == null ? null : source.getCategory().getCode());
		}
	}

}
