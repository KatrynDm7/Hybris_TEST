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
package de.hybris.platform.customerticketingc4cintegration.converters;


import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.customerticketingc4cintegration.data.Note;
import de.hybris.platform.customerticketingc4cintegration.data.ServiceRequestData;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.site.BaseSiteService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class ConvertorsTest  extends ServicelayerTest
{
    @Resource
    private BaseSiteService baseSiteService;
    @Resource
    private Converter<TicketData, ServiceRequestData> c4cTicketConverter;
    @Resource
    private Converter<ServiceRequestData, TicketData> ticketConverter;
    @Resource
    private Converter<TicketData, Note> updateMessageConverter;
    @Resource
    private Map<String, StatusData> statusMapping;

    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/customerticketingc4cintegration/test/testCustomerTicketing.impex", "UTF-8");

        final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID("testSite");
        baseSiteService.setCurrentBaseSite(baseSite, true);
    }

    @Test
    public void ecpTicketConvertorTest()
    {
        TicketData ticketData = new TicketData();
        ticketData.setSubject("Some subject");
        ticketData.setMessage("MEssge");
        ticketData.setCartId("cid");

        ServiceRequestData converted = c4cTicketConverter.convert(ticketData);

        assertEquals(converted.getName(), ticketData.getSubject());
        assertEquals(converted.getExternalCustomerID(), ticketData.getCustomerId());
        assertEquals(converted.getRelatedTransactions().get(0).getID(), ticketData.getCartId());
    }

    @Test
    public void c4cTicketConvertorTest()
    {
        String subject = "Some text qwe";
        String cid = "Some cid";
        ServiceRequestData data = new ServiceRequestData();
        data.setExternalCustomerID(cid);
        data.setName(subject);
        Note createRequestSubObject = new Note();
        createRequestSubObject.setText("");
        createRequestSubObject.setTypeCode("10004");
        data.setNotes(Arrays.asList(createRequestSubObject));

        TicketData converted = ticketConverter.convert(data);

        assertEquals(converted.getSubject(), subject);
        assertEquals(converted.getCustomerId(), cid);
    }

    @Test
    public void ticketMessageUpdateConversionTest()
    {
        TicketData ticketData = new TicketData();
        ticketData.setMessage("MessAGEeeeeeeeeee");
        ticketData.setId("object Id 155515454");

        Note result = updateMessageConverter.convert(ticketData);

        assertEquals(result.getText(), ticketData.getMessage());
    }

    @Test
    public void ticketStatusUpdateConversionTest()
    {
        TicketData ticketData = new TicketData();
        StatusData statusData = new StatusData();
        statusData.setId("OPEN");
        ticketData.setStatus(statusData);

        ServiceRequestData result = c4cTicketConverter.convert(ticketData);

        assertEquals(statusMapping.get(result.getStatusCode()).getId(), statusData.getId());
    }

    @Test
    public void b2bTicketConversionTest()
    {
        final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID("testb2bSite");
        baseSiteService.setCurrentBaseSite(baseSite, true);

        String subject = "Some text qwe";
        String cid = "Some cid";
        ServiceRequestData data = new ServiceRequestData();
        data.setExternalContactID(cid);
        data.setName(subject);
        Note createRequestSubObject = new Note();
        createRequestSubObject.setText("");
        createRequestSubObject.setTypeCode("10004");
        data.setNotes(Arrays.asList(createRequestSubObject));

        TicketData converted = ticketConverter.convert(data);

        assertEquals(converted.getSubject(), subject);
        assertEquals(converted.getCustomerId(), cid);
    }
}
