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
package de.hybris.platform.acceleratorcms.component.cache.impl;

import de.hybris.platform.acceleratorcms.component.cache.CmsCacheKeyProvider;
import de.hybris.platform.acceleratorcms.model.restrictions.CMSUiExperienceRestrictionModel;
import de.hybris.platform.acceleratorcms.services.CMSPageContextService;
import de.hybris.platform.acceleratorcms.utils.SpringHelper;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.exceptions.RestrictionEvaluationException;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCatalogRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSInverseRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class DefaultCmsCacheKeyProvider implements CmsCacheKeyProvider<SimpleCMSComponentModel>
{
	private CMSRestrictionService cmsRestrictionService;
	private CMSPageContextService cmsPageContextService;
	private CommerceCommonI18NService commerceCommonI18NService;

	protected CMSRestrictionService getCmsRestrictionService(final HttpServletRequest request)
	{
		if (cmsRestrictionService == null)
		{
			cmsRestrictionService = SpringHelper.getSpringBean(request, "cmsRestrictionService", CMSRestrictionService.class, true);
		}
		return cmsRestrictionService;
	}

	protected CMSPageContextService getCmsPageContextService(final HttpServletRequest request)
	{
		if (cmsPageContextService == null)
		{
			cmsPageContextService = SpringHelper.getSpringBean(request, "cmsPageContextService", CMSPageContextService.class, true);
		}
		return cmsPageContextService;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService(final HttpServletRequest request)
	{
		if (commerceCommonI18NService == null)
		{
			commerceCommonI18NService = SpringHelper.getSpringBean(request, "commerceCommonI18NService",
					CommerceCommonI18NService.class, true);
		}
		return commerceCommonI18NService;
	}

	@Override
	public CacheKey getKey(final HttpServletRequest request, final SimpleCMSComponentModel component)
	{
		final CacheKey key = new CmsCacheKey(getKeyInternal(request, component).toString(), Registry.getCurrentTenant()
				.getTenantID());
		return key;
	}

	protected StringBuilder getKeyInternal(final HttpServletRequest request, final SimpleCMSComponentModel component)
	{
		final StringBuilder key = new StringBuilder();
		key.append(component.getPk().getLongValueAsString());
		key.append(component.getModifiedtime());
		final CurrencyModel currentCurrency = getCommerceCommonI18NService(request).getCurrentCurrency();
		key.append(currentCurrency.getIsocode());
		final LanguageModel currentLanguage = getCommerceCommonI18NService(request).getCurrentLanguage();
		key.append(currentLanguage.getIsocode());

		final List<AbstractRestrictionModel> restrictions = component.getRestrictions();
		if (restrictions != null && !restrictions.isEmpty())
		{
			for (final AbstractRestrictionModel restriction : component.getRestrictions())
			{

				try
				{
					if (getCmsRestrictionService(request).evaluate(restriction, getRestrictionData(request)))
					{
						key.append(getKeyForRestriction(request, restriction));
					}
				}
				catch (final RestrictionEvaluationException e)
				{
					key.append(handleRestrictionEvaluationException(request, component, restriction, e));
				}
			}
		}

		return key;
	}

	protected String handleRestrictionEvaluationException(final HttpServletRequest request,
			final SimpleCMSComponentModel component, final AbstractRestrictionModel restriction,
			final RestrictionEvaluationException e)
	{
		// No Restriction evaluator for this type of restriction
		return "";
	}

	protected StringBuilder getKeyForRestriction(final HttpServletRequest request, final AbstractRestrictionModel restriction)
	{
		final StringBuilder key = new StringBuilder();
		if (restriction instanceof CMSCatalogRestrictionModel)
		{
			final CMSCatalogRestrictionModel catalogRestrictionModel = (CMSCatalogRestrictionModel) restriction;
			if (catalogRestrictionModel.getCatalogs() != null)
			{
				for (final CatalogModel catalog : catalogRestrictionModel.getCatalogs())
				{
					key.append(catalog.getId());
				}
			}
		}
		else if (restriction instanceof CMSCategoryRestrictionModel)
		{
			final RequestContextData context = getRequestContextData(request);
			if (context != null && context.getCategory() != null)
			{
				key.append(context.getCategory().getPk().getLongValueAsString());
			}
		}
		else if (restriction instanceof CMSInverseRestrictionModel)
		{
			final CMSInverseRestrictionModel inverseRestrictionModel = (CMSInverseRestrictionModel) restriction;
			key.append("not");
			key.append(getKeyForRestriction(request, inverseRestrictionModel.getOriginalRestriction()));
		}
		else if (restriction instanceof CMSProductRestrictionModel)
		{
			final RequestContextData context = getRequestContextData(request);
			if (context != null && context.getProduct() != null)
			{
				key.append(context.getProduct().getPk().getLongValueAsString());
			}
		}
		else if (restriction instanceof CMSTimeRestrictionModel)
		{
			final CMSTimeRestrictionModel timeRestrictionModel = (CMSTimeRestrictionModel) restriction;
			key.append(timeRestrictionModel.getActiveFrom());
			key.append(timeRestrictionModel.getActiveUntil());
		}
		else if (restriction instanceof CMSUserGroupRestrictionModel)
		{
			final CMSUserGroupRestrictionModel userGroupRestrictionModel = (CMSUserGroupRestrictionModel) restriction;
			if (userGroupRestrictionModel.getUserGroups() != null)
			{
				for (final UserGroupModel userGroupModel : userGroupRestrictionModel.getUserGroups())
				{
					key.append(userGroupModel.getUid());
				}
			}
		}
		else if (restriction instanceof CMSUserRestrictionModel)
		{
			final CMSUserRestrictionModel userRestrictionModel = (CMSUserRestrictionModel) restriction;
			if (userRestrictionModel.getUsers() != null)
			{
				for (final UserModel userModel : userRestrictionModel.getUsers())
				{
					key.append(userModel.getUid());
				}
			}
		}
		else if (restriction instanceof CMSUiExperienceRestrictionModel)
		{
			final CMSUiExperienceRestrictionModel uiExperienceRestrictionModel = (CMSUiExperienceRestrictionModel) restriction;
			key.append(uiExperienceRestrictionModel.getUiExperience());
		}
		return key;
	}

	/*
	 * Helper method to lookup RequestContextData from request.
	 */
	protected RequestContextData getRequestContextData(final HttpServletRequest request)
	{
		return SpringHelper.getSpringBean(request, "requestContextData", RequestContextData.class, true);
	}

	protected RestrictionData getRestrictionData(final HttpServletRequest request)
	{
		return (RestrictionData) getCmsPageContextService(request).getCmsPageRequestContextData(request).getRestrictionData();
	}

	public static class CmsCacheKey implements CacheKey
	{
		private static final String CMS_CACHE_UNIT_CODE = "__CMS_CACHE__";
		private final String key;
		private final String tenantId;

		public CmsCacheKey(final String key, final String tenantId)
		{
			this.key = key;
			this.tenantId = tenantId;
		}

		@Override
		public Object getTypeCode()
		{
			return CMS_CACHE_UNIT_CODE;
		}

		@Override
		public String getTenantId()
		{
			return tenantId;
		}

		@Override
		public CacheUnitValueType getCacheValueType()
		{
			return CacheUnitValueType.SERIALIZABLE;
		}

		@Override
		public String toString()
		{
			return "CmsCacheKey [key=" + key + ", tenantId=" + tenantId + "]";
		}

		@Override
		public int hashCode()
		{
			int result = 1;
			result = 31 * result + ((key == null) ? 0 : key.hashCode());
			result = 31 * result + ((tenantId == null) ? 0 : tenantId.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (super.getClass() != obj.getClass())
			{
				return false;
			}
			final CmsCacheKey other = (CmsCacheKey) obj;
			if (tenantId == null)
			{
				if (other.tenantId != null)
				{
					return false;
				}
			}
			else if (!(tenantId.equals(other.tenantId)))
			{
				return false;
			}
			if (key == null)
			{
				if (other.key != null)
				{
					return false;
				}
			}
			else if (!(key.equals(other.key)))
			{
				return false;
			}
			return true;
		}
	}
}
