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
package de.hybris.platform.sap.sapmodel.daos;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import sap.hybris.integration.models.model.SAPProductIDDataConversionModel;



public class ProductIDAttributeHandler implements DynamicAttributeHandler<String, ProductModel>
{
	protected SAPProductIDDataConversionModel customizing;

	protected static final Logger LOGGER = Logger.getLogger(ProductIDAttributeHandler.class.getName());

	@Autowired
	protected FlexibleSearchService flexibleSearchService; //NOPMD

	protected String convertID(final String productID)
	{
		if (customizing == null)
		{
			final SAPProductIDDataConversionModel data = new SAPProductIDDataConversionModel();
			data.setConversionID("MATCONV");
			try
			{
				customizing = flexibleSearchService.getModelByExample(data);
			}
			catch (final ModelNotFoundException e)
			{
				LOGGER.logp(Level.WARNING, ProductIDAttributeHandler.class.getName(), "convertID",
						"Missing SAPProductIDDataConversion customizing, using default value", e);
			}

			if (customizing == null)
			{
				data.setMatnrLength(18);
				data.setDisplayLeadingZeros(false);
				data.setDisplayLexicographic(false);
				data.setMask("");
				customizing = data;
			}
		}
		if (productID == null || productID.isEmpty() || customizing.getDisplayLexicographic())
		{
			return productID;
		}

		final int size = productID.length();
		final int maxLength = customizing.getMatnrLength();
		String mask = customizing.getMask();
		mask = mask != null ? mask : "";

		final Set<Character> symbols = new HashSet<Character>();
		for (int i = 0; i < mask.length(); i++)
		{
			if (mask.charAt(i) != '_')
			{
				symbols.add(mask.charAt(i));
			}
		}
		boolean leadZero = true;
		int leadZeroCount = 0;

		//check if inputString is numeric & count leading zeroes
		boolean isNumeric = true;
		for (int i = 0; i < size; i++)
		{
			final char ch = productID.charAt(i);
			if (ch == '0' && leadZero && i < size - 1)
			{
				leadZeroCount++;
			}
			else
			{
				leadZero = false;
			}
			if ((ch > '9' || ch < '0') && !symbols.contains(ch))
			{
				isNumeric = false;
				i = size;
			}
		}

		// Mask processing
		if (mask != null && !mask.isEmpty())
		{
			int nonMarkCount = 0;
			for (int i = 0; i < mask.length(); i++)
			{
				if (mask.charAt(i) != '_')
				{
					nonMarkCount++;
				}
			}

			String workProductID = productID;
			workProductID = workProductID.substring(0, Math.min(workProductID.length(), 18));
			workProductID = isNumeric && !customizing.getDisplayLeadingZeros() ? workProductID.substring(Math.max(leadZeroCount,
					nonMarkCount)) : workProductID;
			if (leadZeroCount < nonMarkCount)
			{
				isNumeric = false;
			}

			final int workSize = workProductID.length();
			final int maskSize = mask.length();
			final StringBuilder sb = new StringBuilder(mask);
			int builtLength = 0;
			for (int i = 1, j = 1; i <= maskSize && j <= workSize; i++)
			{
				final int maskIndex = isNumeric ? maskSize - i : i - 1;
				if (sb.charAt(maskIndex) == '_')
				{
					sb.setCharAt(maskIndex, workProductID.charAt(isNumeric ? workSize - j : j - 1));
					j++;
				}
				builtLength = i;
			}
			if (builtLength == 0)
			{
				return mask.replace("_", " ").trim();
			}
			return isNumeric ? sb.substring(maskSize - builtLength, maskSize) : sb.substring(0, builtLength);
		}

		if (!isNumeric)
		{
			return productID;
		}

		if (customizing.getDisplayLeadingZeros())
		{
			return size + maxLength < 19 ? "" : productID.substring(18 - maxLength, Math.min(size, 18));
		}

		//remove leading zeros
		return productID.substring(leadZeroCount);
	}

	public String get(final ProductModel model)
	{
		return this.convertID(model.getCode());
	}

	public void set(final ProductModel model, final String value)
	{
		throw new UnsupportedOperationException();
	}
}
