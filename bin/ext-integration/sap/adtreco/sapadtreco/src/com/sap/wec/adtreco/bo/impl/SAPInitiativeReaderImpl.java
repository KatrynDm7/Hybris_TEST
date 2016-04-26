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
package com.sap.wec.adtreco.bo.impl;

import de.hybris.platform.sap.core.bol.backend.BackendType;
import de.hybris.platform.sap.core.bol.businessobject.BusinessObjectBase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.springframework.beans.factory.annotation.Required;

import com.sap.wec.adtreco.be.HMCConfigurationReader;
import com.sap.wec.adtreco.be.intf.ADTInitiativesBE;
import com.sap.wec.adtreco.bo.CookieHelper;
import com.sap.wec.adtreco.bo.intf.SAPInitiativeReader;


/**
 *
 */
@BackendType("CEI")
public class SAPInitiativeReaderImpl extends BusinessObjectBase implements SAPInitiativeReader
{
	protected static final String DT_selectTerms = "Name,Description,InitiativeId,InitiativeIdExt,LifecycleStatus,TargetGroup/CustomerMemberCount";
	protected static final String RT_selectTerms = "Name,Description,InitiativeId,InitiativeIdExt";
	protected static final String ORDERBY = null;
	protected static final String INITIATIVES = "Initiatives";
	protected static final String TARGETGROUPS = "TargetGroup";
	protected static final String IN_PREPARATION = "1";
	protected static final String RELEASED = "2";
	protected static final String ACTIVE = "1";
	protected static final String PLANNED = "2";
	protected static final String EQ_UTF8 = " eq '";
	protected static final String QUOT_UTF8 = "'";
	protected static final String AND_UTF8 = " and ";
	protected static final String OR_UTF8 = " or ";

	protected ADTInitiativesBE accessBE;
	
	private HMCConfigurationReader configuration;
	

	public List<SAPInitiative> getAllInitiatives() throws ODataException, URISyntaxException, IOException, RuntimeException
	{
		this.configuration.loadADTConfiguration();
		final String filterStatus = "LifecycleStatus/StatusCode" + EQ_UTF8 + "2" + QUOT_UTF8;
		final ODataFeed feed = accessBE.getInitiatives(DT_selectTerms, filterStatus, INITIATIVES, "TargetGroups", ORDERBY);
		final List<ODataEntry> foundEntries = feed.getEntries();
		return extractInitiatives(foundEntries);
	}

	public List<SAPInitiative> searchInitiatives(final String search) throws ODataException, URISyntaxException, IOException, RuntimeException
	{
		this.configuration.loadADTConfiguration();
		final String filterDescription = "Search/SearchTerm" + EQ_UTF8 + search + QUOT_UTF8;

		final String filterStatus = "(" + getSearchTileFilterCategoryTerm(ACTIVE) + OR_UTF8
				+ getSearchTileFilterCategoryTerm(PLANNED) + ")";

		final String filterTerms = filterDescription + AND_UTF8 + getFilterCategory() + AND_UTF8 + filterStatus;
		final ODataFeed feed = accessBE.getInitiatives(DT_selectTerms, filterTerms, INITIATIVES, TARGETGROUPS, ORDERBY);

		return getInitiativesFromFeed(feed);
	}

	public List<SAPInitiative> searchInitiativesForBP(final String businessPartner, final boolean isAnonymous) throws ODataException, URISyntaxException, IOException, RuntimeException
	{
		this.configuration.loadADTConfiguration();

		return getRTInitiativesForFilter(getFilterTerms(getBusinessPartnerFilter(businessPartner, isAnonymous)));
	}
	
	public List<SAPInitiative> searchInitiativesForMultiBP(final String[] businessPartners) throws ODataException, URISyntaxException, IOException, RuntimeException
	{
		this.configuration.loadADTConfiguration();

		return getRTInitiativesForFilter(getFilterTerms(getMultiBusinessPartnerFilter(businessPartners)));
	}
	
	protected String getMultiBusinessPartnerFilter(final String[] businessPartners)
	{
		String filter = "(";
		if (businessPartners != null & businessPartners.length > 0)
		{	
			filter += "Filter/InteractionContactId" + EQ_UTF8 + businessPartners[0] + QUOT_UTF8;
			for(int i = 1; i < businessPartners.length; i++)
			{
				if(businessPartners[i] != null)
				{
					filter += " or Filter/InteractionContactId" + EQ_UTF8 + businessPartners[i] + QUOT_UTF8;
				}
			}
		}
		filter += ") and (Filter/InteractionContactIdOrigin" + EQ_UTF8 + configuration.getIdOrigin() + QUOT_UTF8 + 
				" or Filter/InteractionContactIdOrigin" + EQ_UTF8 + configuration.getAnonIdOrigin() + QUOT_UTF8 + ")";
		
		return filter;
	}

	protected String getBusinessPartnerFilter(final String businessPartner, final boolean isAnonymous)
	{
		if (StringUtils.isNotEmpty(businessPartner))
		{
			String idOrigin = configuration.getIdOrigin();
			if(isAnonymous == true){
				idOrigin = configuration.getAnonIdOrigin();
			}
			return "Filter/InteractionContactIdOrigin" + EQ_UTF8 + idOrigin + QUOT_UTF8
					+ " and Filter/InteractionContactId" + EQ_UTF8 + businessPartner + QUOT_UTF8;
		}
		else
		{
			return StringUtils.EMPTY;
		}
	}

	protected List<SAPInitiative> getRTInitiativesForFilter(final String filter) throws IOException, ODataException, URISyntaxException
	{
		final ODataFeed feed = accessBE.getInitiatives(RT_selectTerms, filter, INITIATIVES, null, ORDERBY);
		return getInitiativesFromFeed(feed);
	}

	protected List<SAPInitiative> getInitiativesFromFeed(final ODataFeed feed) {
		if (feed == null)
		{
			return new ArrayList<>();
		}
		else
		{
			final List<ODataEntry> foundEntries = feed.getEntries();
			return extractInitiatives(foundEntries);
		}
	}

	protected String getFilterTerms(final String filterBP)
	{
		return getFilterCategory() + AND_UTF8 + getSearchTileFilterCategoryTerm(ACTIVE) + AND_UTF8 + filterBP;
	}

	protected String getFilterCategory()
	{
		return "Category/CategoryCode" + EQ_UTF8 + configuration.getFilterCategory() + QUOT_UTF8;
	}

	protected String getSearchTileFilterCategoryTerm(final String term) {
		return "Search/TileFilterCategory" + EQ_UTF8 + term + QUOT_UTF8;
	}

	protected String convertToInternalKey(final String id)
	{
		final Integer in = Integer.valueOf(id);
		final String intKey = String.format("%010d", in);
		return intKey;
	}

	public SAPInitiative getSelectedInitiative(final String id) throws ODataException, IOException, URISyntaxException, RuntimeException
	{
		final String keyValue = "'" + convertToInternalKey(id) + "'";
		final ODataEntry entry = accessBE.getInitiative(DT_selectTerms, keyValue, INITIATIVES);
		if (entry != null)
		{
			return extractInitiative(entry);
		}
		else
		{
			return null;
		}
	}

	public SAPInitiative getInitiative(final String id) throws ODataException, IOException, URISyntaxException, RuntimeException
	{
		final String keyValue = "'" + convertToInternalKey(id) + "'";
		final ODataEntry entry = accessBE.getInitiative(RT_selectTerms, keyValue, INITIATIVES);
		if (entry != null)
		{
			return extractInitiative(entry);
		}
		else
		{
			return null;
		}
	}

	/**
	 *
	 */
	protected List<SAPInitiative> extractInitiatives(final List<ODataEntry> foundEntities)
	{
		final List<SAPInitiative> initiatives = new ArrayList<SAPInitiative>();
		if (foundEntities != null)
		{
			final Iterator<ODataEntry> iter = foundEntities.iterator();

			while (iter.hasNext())
			{
				final ODataEntry entity = iter.next();
				final SAPInitiative initiative = extractInitiative(entity);
				initiatives.add(initiative);

			}
		}
		return initiatives;
	}

	protected boolean checkInitiativeSchedule(final SAPInitiative init)
	{
		final Date currentDate = new Date();

		if ((init.getStartDate().before(currentDate) && init.getEndDate().after(currentDate)) //Initiative is Active
				|| (init.getStartDate().after(currentDate) && init.getEndDate().after(currentDate))) //Initiative is Planned
		{
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	protected SAPInitiative extractInitiative(final ODataEntry entity)
	{
		final SAPInitiative initiative = new SAPInitiative();
		final Map<String, Object> props = entity.getProperties();
		if (props != null)
		{
			initiative.setName(props.get("Name").toString());
			initiative.setDescription(props.get("Description").toString());
			initiative.setId(props.get("InitiativeIdExt").toString());
			final HashMap<String, String> status = (HashMap<String, String>) props.get("LifecycleStatus");
			if (status != null)
			{
				final String statusName = status.get("StatusDescription");
				initiative.setStatus(statusName);
			}

			final ODataEntry tg = (ODataEntry) props.get("TargetGroup");
			if (tg != null)
			{
				final Map<String, Object> tgProps = tg.getProperties();
				initiative.setMemberCount(tgProps.get("CustomerMemberCount").toString());
			}
		}
		return initiative;
	}

	public ADTInitiativesBE getAccessBE()
	{
		return accessBE;
	}

	public void setAccessBE(final ADTInitiativesBE accessBE)
	{
		this.accessBE = accessBE;
	}
	
	public HMCConfigurationReader getConfiguration()
	{
		return configuration;
	}

	public void setConfiguration(HMCConfigurationReader configuration)
	{
		this.configuration = configuration;
	}
	
}
