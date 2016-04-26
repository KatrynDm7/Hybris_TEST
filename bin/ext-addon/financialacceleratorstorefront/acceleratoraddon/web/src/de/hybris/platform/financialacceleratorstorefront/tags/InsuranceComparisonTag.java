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
package de.hybris.platform.financialacceleratorstorefront.tags;

import de.hybris.platform.acceleratorservices.util.SpringHelper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.financialacceleratorstorefront.comparison.ComparisonTable;
import de.hybris.platform.financialacceleratorstorefront.comparison.ComparisonTableColumn;
import de.hybris.platform.financialacceleratorstorefront.comparison.ComparisonTableFactory;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;


/**
 * Supports the comparison of Insurance products
 * */
public class InsuranceComparisonTag extends HtmlEscapingAwareTag
{
	private int scope = PageContext.REQUEST_SCOPE;
	private String tableFactory;
	private String var;
	private SearchPageData searchPageData;

	@Override
	protected int doStartTagInternal() throws Exception
	{
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException
	{
		if (this.var != null)
		{
			final ComparisonTable comparisonTable = lookupComparisonTableFactory().createTable(getSearchPageData());
			if (!validateTable(comparisonTable))
			{
				pageContext.setAttribute(getVar(), null, getScope());
			}
			else
			{
				pageContext.setAttribute(getVar(), comparisonTable, getScope());
			}
		}
		return EVAL_PAGE;
	}

	protected ComparisonTableFactory lookupComparisonTableFactory()
	{
		return SpringHelper.getSpringBean(pageContext.getRequest(), getTableFactory(), ComparisonTableFactory.class, true);
	}

	protected int getScope()
	{
		return scope;
	}

	public void setScope(final int scope)
	{
		this.scope = scope;
	}

	protected String getTableFactory()
	{
		return tableFactory;
	}

	public void setTableFactory(final String tableFactory)
	{
		this.tableFactory = tableFactory;
	}

	protected String getVar()
	{
		return var;
	}

	public void setVar(final String var)
	{
		this.var = var;
	}

	protected SearchPageData getSearchPageData()
	{
		return searchPageData;
	}

	public void setSearchPageData(final SearchPageData searchPageData)
	{
		this.searchPageData = searchPageData;
	}

	/**
	 * Validates the comparison table based on the logic used in productListerGridItem.tag
	 *
	 * @param comparisonTable
	 * @return true if valid -otherwise false
	 */
	private boolean validateTable(final ComparisonTable comparisonTable)
	{
		boolean isValid = false;
		if (null != comparisonTable)
		{
			final boolean hasRecurringCharge = hasRecurringCharge(comparisonTable);
			final Map<Object, ComparisonTableColumn> map = comparisonTable.getColumns();
			for (final Object key : map.keySet())
			{

				if (key instanceof de.hybris.platform.commercefacades.product.data.ProductData)
				{
					final ProductData product = (ProductData) key;

					final SubscriptionPricePlanData price = (SubscriptionPricePlanData) product.getPrice();
					if (price != null)
					{
						if (hasRecurringCharge && price.getRecurringChargeEntries() != null
								&& !price.getRecurringChargeEntries().isEmpty())
						{
							final RecurringChargeEntryData payOnCheckout = price.getRecurringChargeEntries().get(0);
							if (payOnCheckout.getPrice().getValue() != null
									&& payOnCheckout.getPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
							{
								isValid = true;
							}
						}
						else if (price.getOneTimeChargeEntries() != null && !price.getOneTimeChargeEntries().isEmpty())
						{
							final OneTimeChargeEntryData payOnCheckout = price.getOneTimeChargeEntries().get(0);
							if (payOnCheckout.getBillingTime().getCode().equals("paynow"))
							{
								if (payOnCheckout.getPrice().getValue() != null
										&& payOnCheckout.getPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
								{
									isValid = true;
								}
							}
						}
					}

				}
			}

		}
		return isValid;
	}

	/**
	 * @param comparisonTable
	 * @return
	 */
	private boolean hasRecurringCharge(final ComparisonTable comparisonTable)
	{
		boolean hasRecurringCharge = false;
		final Map<Object, ComparisonTableColumn> map = comparisonTable.getColumns();
		for (final Object key : map.keySet())
		{

			if (key.toString().equals("recurringAnnualPrice"))
			{
				hasRecurringCharge = true;
				break;
			}
		}
		return hasRecurringCharge;
	}
}
