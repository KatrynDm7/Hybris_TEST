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
package de.hybris.platform.bmecat.session;



import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.jalo.PriceChangeDescriptor;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.EJBTypecodeNotSupportedException;
import de.hybris.platform.persistence.ExtensibleItemRemote;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.VersionProvider;
import de.hybris.platform.persistence.extension.ExtensionEJB;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.jeeapi.YRemoveException;

import org.apache.log4j.Logger;


public class BMECatManagerEJB extends ExtensionEJB
{
	private static final Logger LOG = Logger.getLogger(BMECatManagerEJB.class.getName());


	@Override
	public String getName()
	{
		return BMECatConstants.EXTENSIONNAME;
	}


	public ItemRemote findRemoteObjectByPK(final String pk) throws EJBItemNotFoundException, EJBTypecodeNotSupportedException
	{
		throw new EJBTypecodeNotSupportedException(null, "BMECat does not support any typecode ", 290134415);
	}


	@Override
	public void notifyItemRemove(final ItemRemote item)
	{
		try
		{
			/** deletes all features for removed feature relations */
			if (item.getComposedType().getJaloClassName().equals(PriceChangeDescriptor.class.getName()))
			{
				final ExtensibleItemRemote desc = (ExtensibleItemRemote) item;

				final Object object = desc.getProperty(PriceChangeDescriptor.PRICECOPY);
				if (object != null)
				{
					final ItemRemote priceCopy = EJBTools.instantiatePK(((ItemPropertyValue) object).getPK());
					if (priceCopy != null)
					{
						priceCopy.remove();
					}
				}
			}
		}
		catch (final YRemoveException x)
		{
			LOG.info("RemoveException" + x.toString());
			throw new JaloSystemException(x, "!!", 0);
		}
	}

	/* VERSION */
	public String getEJBImplementationVersion()
	{
		return VersionProvider.getImplementationVersion("de.hybris.platform.bmecat.ejb");
	}

	public String getEJBSpecificationVersion()
	{
		return VersionProvider.getSpecificationVersion("de.hybris.platform.bmecat.ejb");
	}
}
