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
 */

package com.hybris.datahub.core.dto;

import com.hybris.datahub.core.facades.ImportError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A DTO which holds the information needed to run an ImpexImport job
 */
public class ItemImportTaskData implements Serializable
{
	private String poolName;
	private Long publicationId;
	private String resultCallbackUrl;
	private byte[] impexMetaData;
	private Map<String, Object> sessionAttrs;
	private List<ImportError> headerErrors;

	public ItemImportTaskData()
	{
		headerErrors = new ArrayList<>();
	}

	public ItemImportTaskData(final String poolName, final Long publicationId, final String resultCallbackUrl,
			final byte[] impexMetaData, final Map sessionAttrs)
	{
		this.poolName = poolName;
		this.publicationId = publicationId;
		this.resultCallbackUrl = resultCallbackUrl;
		this.impexMetaData = impexMetaData;
		this.sessionAttrs = sessionAttrs;
		headerErrors = new ArrayList<>();
	}

	public String getPoolName()
	{
		return poolName;
	}

	public void setPoolName(final String poolName)
	{
		this.poolName = poolName;
	}

	public Long getPublicationId()
	{
		return publicationId;
	}

	public void setPublicationId(final Long publicationId)
	{
		this.publicationId = publicationId;
	}

	public String getResultCallbackUrl()
	{
		return resultCallbackUrl;
	}

	public void setResultCallbackUrl(final String resultCallbackUrl)
	{
		this.resultCallbackUrl = resultCallbackUrl;
	}

	public byte[] getImpexMetaData()
	{
		return impexMetaData;
	}

	public void setImpexMetaData(final byte[] impexMetaData)
	{
		this.impexMetaData = impexMetaData;
	}

	public Map<java.lang.String, Object> getSessionAttrs()
	{
		return sessionAttrs;
	}

	public void setSessionAttrs(final Map<String, Object> sessionAttrs)
	{
		this.sessionAttrs = sessionAttrs;
	}

	public void addSesstionAttribute(final String key, final Object value)
	{
		if (this.sessionAttrs == null)
		{
			this.sessionAttrs = new HashMap<String, Object>();
		}

		this.sessionAttrs.put(key, value);
	}

	public List<ImportError> getHeaderErrors()
	{
		return headerErrors;
	}

	public void setHeaderErrors(final List<ImportError> headerErrors)
	{
		this.headerErrors = headerErrors;
	}

	@Override
	public String toString()
	{
		return "ItemImportTaskData{" +
				"poolName='" + poolName + '\'' +
				", publicationId=" + publicationId +
				", resultCallbackUrl='" + resultCallbackUrl + '\'' +
				", impexMetaData=" + Arrays.toString(impexMetaData) +
				", sessionAttrs=" + sessionAttrs +
				", headerErrors=" + headerErrors +
				'}';
	}
}
