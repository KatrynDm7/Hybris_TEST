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
package de.hybris.platform.financialservices.bundle.impl;

import de.hybris.platform.configurablebundleservices.daos.impl.AbstractBundleRuleDao;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * Default implementation of the {@link AbstractBundleRuleDao} for sub-type {@link DisableProductBundleRuleModel}.
 */
public class GeoDisableProductBundleRuleDao extends AbstractBundleRuleDao<DisableProductBundleRuleModel>
{
	/**
	 * Used to find rules for a given product
	 */
	protected static final String FIND_BUNDLE_RULES_BY_TARGET_PRODUCT = "SELECT {rule:" + DisableProductBundleRuleModel.PK
			+ "} FROM {" + DisableProductBundleRuleModel._TYPECODE + " AS rule JOIN "
			+ ProductModel._ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION + " AS targetRel ON {targetRel:source}={rule:"
			+ DisableProductBundleRuleModel.PK + "}} WHERE {targetRel:target}=?product";


	/**
	 * Used to locate bundle rules by their target product and their template
	 */
	protected static final String FIND_BUNDLE_RULES_BY_TARGET_PRODUCT_AND_TEMPLATE_QUERY = FIND_BUNDLE_RULES_BY_TARGET_PRODUCT
			+ " AND {rule:" + DisableProductBundleRuleModel.BUNDLETEMPLATE + "}=?bundleTemplate";

	/**
	 * Used to locate bundle rules by their product and root template
	 */
	protected static final String FIND_BUNDLE_RULES_BY_PRODUCT_AND_ROOT_TEMPLATE_QUERY = "SELECT DISTINCT {rule:"
			+ DisableProductBundleRuleModel.PK + "} FROM {" + DisableProductBundleRuleModel._TYPECODE + " AS rule JOIN "
			+ BundleTemplateModel._TYPECODE + " AS templ ON {templ:PK}={rule:" + DisableProductBundleRuleModel.BUNDLETEMPLATE
			+ "} JOIN " + ProductModel._ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION
			+ " AS targetRel ON {targetRel:source}={rule:PK} LEFT OUTER JOIN "
			+ ProductModel._ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION + " AS condRel ON {condRel:source}={rule:PK}}"
			+ " WHERE {templ:parentTemplate}=?rootBundleTemplate AND "
			+ " ({targetRel:target}=?product OR {condRel:target}=?product)";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFindBundleRulesByTargetProductQuery()
	{
		return FIND_BUNDLE_RULES_BY_TARGET_PRODUCT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFindBundleRulesByTargetProductAndTemplateQuery()
	{
		return FIND_BUNDLE_RULES_BY_TARGET_PRODUCT_AND_TEMPLATE_QUERY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFindBundleRulesByProductAndRootTemplateQuery()
	{
		return FIND_BUNDLE_RULES_BY_PRODUCT_AND_ROOT_TEMPLATE_QUERY;
	}

}
