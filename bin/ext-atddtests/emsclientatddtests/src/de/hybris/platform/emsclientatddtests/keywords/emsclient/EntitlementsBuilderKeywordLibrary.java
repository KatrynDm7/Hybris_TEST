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
package de.hybris.platform.emsclientatddtests.keywords.emsclient;

import static de.hybris.platform.atddengine.xml.XmlAssertions.assertXPathEvaluatesTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.entitlementatddtests.converters.ObjectXStreamAliasConverter;
import de.hybris.platform.entitlementfacades.CoreEntitlementFacade;
import de.hybris.platform.entitlementfacades.data.EntitlementData;
import de.hybris.platform.entitlementservices.data.EmsGrantData;
import de.hybris.platform.entitlementservices.enums.EntitlementTimeUnit;
import de.hybris.platform.entitlementservices.exception.EntitlementFacadeException;
import de.hybris.platform.entitlementservices.facades.EntitlementFacadeDecorator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hybris.services.entitlements.api.GrantData;
import com.hybris.services.entitlements.conversion.DateTimeConverter;


public class EntitlementsBuilderKeywordLibrary extends AbstractKeywordLibrary
{
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

	private static final Logger LOG = Logger.getLogger(EntitlementsBuilderKeywordLibrary.class);

	private final Random random = new Random();


	@Autowired
	private EntitlementFacadeDecorator entitlementFacadeDecorator;

	@Autowired
	private ObjectXStreamAliasConverter xStreamAliasConverter;

	@Autowired
	@Qualifier("defaultEmsGrantGrantConverter")
	private Converter<EmsGrantData, GrantData> emsGrantGrantConverter;

	@Autowired
	private DateTimeConverter dateTimeConverter;

    @Autowired
    @Qualifier("coreEntitlementFacade")
    private CoreEntitlementFacade coreEntitlementFacade;


	/**
	 * Creates {@link EmsGrantData} instance and fill if with provided data.
	 *
	 * @param entitlementType id of {@link de.hybris.platform.entitlementfacades.data.EntitlementData#getId()}
	 * @param conditionString optional string condition value
	 * @param maxQuantity quantity limit for metered entitlements
	 * @param entitlementTimeUnit optional {@link EntitlementTimeUnit}
	 * @param timeUnitStart optional start offset in time units, begins from 1
	 * @param timeUnitDuration optional duration in time units
	 * @param conditionPath optional condition path value
	 * @param conditionGeo optional geo condition value
	 * @param dateCreatedAt optional grant creation date
	 * @param userId optional owner id
	 * @return created DTO
	 * @throws ParseException in case of invalid format of arguments
	 */
	public EmsGrantData buildEmsGrantData(final String entitlementType, final String conditionString, final Integer maxQuantity,
			final String entitlementTimeUnit, final String timeUnitStart, final String timeUnitDuration, final String conditionPath,
			final String conditionGeo, final String dateCreatedAt, final String userId) throws ParseException
	{
		final EmsGrantData emsGrantData = new EmsGrantData();
		emsGrantData.setEntitlementType(entitlementType);
		emsGrantData.setConditionString(conditionString);
		emsGrantData.setMaxQuantity(maxQuantity);
		if (dateCreatedAt != null)
		{
			final Date date = dateTimeConverter.convertStringToDate(dateCreatedAt);
			emsGrantData.setCreatedAt(date);
		}
		if (conditionGeo != null)
		{
			emsGrantData.setConditionGeo(new ArrayList<>(Arrays.asList(conditionGeo.split(","))));
		}
		if (entitlementTimeUnit != null)
		{
			emsGrantData.setTimeUnit(EntitlementTimeUnit.valueOf(entitlementTimeUnit));
		}

		emsGrantData.setConditionPath(conditionPath);

		if (timeUnitStart != null)
		{
			emsGrantData.setTimeUnitStart(Integer.parseInt(timeUnitStart));
		}

		if (timeUnitDuration != null)
		{
			emsGrantData.setTimeUnitDuration(Integer.parseInt(timeUnitDuration));
		}

		emsGrantData.setUserId(userId == null ? getRandomString("user_id") : userId);
		emsGrantData.setOrderCode(getRandomString("order_code"));
		emsGrantData.setBaseStoreUid(getRandomString("base_store_uid"));
		emsGrantData.setOrderEntryNumber(getRandomString("order_entry_number"));

		return emsGrantData;
	}


	/**
	 * Creates {@link EmsGrantData} instance and fill if with provided data.
	 *
	 * @param entitlementType id of {@link de.hybris.platform.entitlementfacades.data.EntitlementData#getId()}
	 * @param conditionString optional string condition value
	 * @param maxQuantity quantity limit for metered entitlements
	 * @param entitlementTimeUnit optional {@link EntitlementTimeUnit}
	 * @param timeUnitStart optional start offset in time units, begins from 1
	 * @param timeUnitDuration optional duration in time units
	 * @param conditionPath optional condition path value
	 * @param conditionGeo optional geo condition value
	 * @param dateCreatedAt optional grant creation date
	 * @return created DTO
	 * @throws ParseException in case of invalid format of arguments
	 */
	public EmsGrantData buildEmsGrantData(final String entitlementType, final String conditionString, final Integer maxQuantity,
			final String entitlementTimeUnit, final String timeUnitStart, final String timeUnitDuration, final String conditionPath,
			final String conditionGeo, final String dateCreatedAt) throws ParseException
	{
		final EmsGrantData emsGrantData = buildEmsGrantData(entitlementType, conditionString, maxQuantity,
				entitlementTimeUnit, timeUnitStart,
				timeUnitDuration, conditionPath, conditionGeo, dateCreatedAt, null);
		emsGrantData.setUserId(UUID.randomUUID().toString());
		return emsGrantData;
	}


	/**
	 * Creates {@link EmsGrantData} instance and fill if with provided data.
	 *
	 * @param entitlementType id of {@link de.hybris.platform.entitlementfacades.data.EntitlementData#getId()}
	 * @param conditionString optional string condition value
	 * @param maxQuantity quantity limit for metered entitlements
	 * @return created DTO
	 * @throws ParseException in case of invalid format of arguments
	 */
	public EmsGrantData buildEmsGrantData(final String entitlementType, final String conditionString, final Integer maxQuantity)
			throws ParseException
	{
		return buildEmsGrantData(entitlementType, conditionString, maxQuantity, null, null, null, null, null, null);
	}


	/**
	 * Grants given entitlement in EMS.
	 *
	 * @param emsGrantData grant info
	 * @return {@link com.hybris.services.entitlements.api.GrantData#getId()}
	 * @throws EntitlementFacadeException
	 */
	public String grantEmsEntitlement(final EmsGrantData emsGrantData) throws EntitlementFacadeException
	{
		//		final Date createdAtDate = new Date();// TODO parse from createdAt
		return entitlementFacadeDecorator.createEntitlement(emsGrantData);
	}


	/**
	 * Ensure that xml serialization of given object corresponds to expected structure.
	 * Date and time values are out of consideration.
	 *
	 * @param object object is being serialized
	 * @param xpath which part of object should be compared
	 * @param expectedXml expected value
	 */
	public void verifyObjectXml(final Object object, final String xpath, final String expectedXml)
	{
		try
		{
			final String objectXml = xStreamAliasConverter.getXStreamXmlFromObject(object);
			assertXPathEvaluatesTo("The product XML does not match the expectations:", objectXml, xpath, expectedXml,
					"transformation/IgnoreGrantIdsAndTime.xsl");
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Either the expected XML is malformed or the product code is null", e);
			fail("Either the expected XML is malformed or the product code is null");
		}
	}


	/**
	 * Compare serialization of grant to given XML.
	 *
	 * @param emsGrantData grant to compare
	 * @param xpath which part of object should be compared
	 * @param expectedXml expected value
	 */
	public void verifyEmsGrantXml(final EmsGrantData emsGrantData, final String xpath, final String expectedXml)
	{
		assertNotNull(emsGrantGrantConverter);
		assertNotNull(xStreamAliasConverter);
		final GrantData grantData = emsGrantGrantConverter.convert(emsGrantData);
		verifyObjectXml(grantData, xpath, expectedXml);
	}

	/**
	 * Compare grants of given user to XML.
	 *
	 * @param userId grant owner id
	 * @param expectedXml expected value
	 * @param transformResource path to XSLT transformation relative to resources
	 */
	public void verifyGrantsWithTransformation(final String userId, final String expectedXml, final String transformResource)
	{
		assertNotNull(coreEntitlementFacade);
		assertNotNull(xStreamAliasConverter);
		try
		{
			final Collection<EntitlementData> userGrants = coreEntitlementFacade.getUserGrants(userId);

			final EntitlementDataList entitlementDataList = new EntitlementDataList();
			entitlementDataList.setEntitlements(userGrants);

			xStreamAliasConverter.getXstream().alias("entitlementsList", EntitlementDataList.class);
			xStreamAliasConverter.getXstream().aliasField("entitlements", EntitlementDataList.class, "entitlements");

			final String entitlementXml = xStreamAliasConverter.getXStreamXmlFromObject(entitlementDataList);
			assertXPathEvaluatesTo("The entitlements XML does not match the expectations:", entitlementXml,
					"/entitlementsList/entitlements", expectedXml,
					transformResource);
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Either the expected XML is malformed or the user id is null", e);
			fail("Either the expected XML is malformed or the user id is null");
		}
	}

	/**
	 * Compare grants of given user to XML.
	 * Date and time values are out of consideration.
	 *
	 * @param userId grant owner id
	 * @param expectedXml expected value
	 */
	public void verifyGrantsXmlWithoutTime(final String userId, final String expectedXml)
	{
		verifyGrantsWithTransformation(userId, expectedXml, "transformation/IgnoreGrantIdsAndTime.xsl");
	}


    /**
     * Java implementation of the robot keyword.
     * <p>
     * <i>verify grants xml for user</i>
     * <p>
     *
     * @param userId
     *           the identificator of the user to verify
     * @param expectedXml
     *           the expected XML
     */
    public void verifyGrantsXmlForUser(final String userId, final String expectedXml)
    {
		verifyGrantsWithTransformation(userId, expectedXml, "transformation/IgnoreGrantIds.xsl");
    }

	private String getRandomString(final String prefix)
	{
		return prefix + '_' + random.nextInt();
	}

}
