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
package de.hybris.platform.bmecat.xmlwriter;

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.catalog.jalo.Company;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Company&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public abstract class CompanyTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public CompanyTagWriter(final XMLTagWriter parent, final boolean mandatory)
	{
		super(parent, mandatory);
		final Map idTagMap = new LinkedMap();
		idTagMap.put("undefined", new SimpleTagWriter(this, getIdTagName()));
		idTagMap.put("duns", new SimpleTypedTagWriter(this, getIdTagName(), "duns"));
		idTagMap.put("iln", new SimpleTypedTagWriter(this, getIdTagName(), "iln"));
		idTagMap.put("buyer_specific", new SimpleTypedTagWriter(this, getIdTagName(), "buyer_specific"));
		idTagMap.put("supplier_specific", new SimpleTypedTagWriter(this, getIdTagName(), "supplier_specific"));
		addSubTagWriter(getIdTagName(), idTagMap);

		addSubTagWriter(new SimpleTagWriter(this, getNameTagName(), true));
		addSubTagWriter(new AddressTagWriter(this, getAddressType()), getAddressType());
		addSubTagWriter(new MimeInfoTagWriter(this));
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportCtx = (BMECatExportContext) object;
		final Company company = exportCtx.getCompany();

		/*
		 * if( company == null && isMandatory() ) { throw new RuntimeException( "tag: " + getTagName() +
		 * " is not optional!" ); }
		 */
		getSubTagWriter(getIdTagName(), "undefined").write(xmlOut, company != null ? company.getId() : "default_supplier_id");
		getSubTagWriter(getIdTagName(), "duns").write(xmlOut, company != null ? company.getDunsID() : "default_supplier_duns_id");
		getSubTagWriter(getIdTagName(), "iln").write(xmlOut, company != null ? company.getIlnID() : "default_supplier_iln_id");
		getSubTagWriter(getIdTagName(), "buyer_specific").write(xmlOut,
				company != null ? company.getBuyerSpecificID() : "default_supplier_buyerspecific_id");
		getSubTagWriter(getIdTagName(), "supplier_specific").write(xmlOut,
				company != null ? company.getSupplierSpecificID() : "default_supplier_specific_id");

		getSubTagWriter(getNameTagName()).write(xmlOut, company != null ? company.getUID() : "default_supplier_uid");
		final Collection addresses = company != null ? company.getAddresses() : null;
		if (addresses != null && !addresses.isEmpty())
		{
			getSubTagWriter(BMECatConstants.XML.TAG.ADDRESS, getAddressType()).write(xmlOut, addresses.iterator().next());
		}

		final Collection companyMedias = company != null ? company.getMedias() : null;
		if (companyMedias != null && !companyMedias.isEmpty())
		{
			final Map companyMediaMap = new HashMap();
			companyMediaMap.put("logo", companyMedias);
			exportCtx.setStringPurpose2MediaCollectionMap(companyMediaMap);
			getSubTagWriter(BMECatConstants.XML.TAG.MIME_INFO).write(xmlOut, exportCtx);
		}

	}

	protected abstract String getIdTagName();

	protected abstract String getNameTagName();

	protected abstract String getAddressType();
}
