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
package de.hybris.platform.financialfacades.email;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * Event must launch email-sending.
 * @see FindAgentMailEventListener
 */
public class FindAgentMailEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{

    private static Logger LOG = Logger.getLogger(FindAgentMailEvent.class);

    private String agentEmail;
    private String userMessage;
    private String userEmail;

    private String callback;
    private String phone;
    private String userName;
    private String interest;
    private String userUid;
    private String cartCode;
    private boolean anonymousUser;


    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("agentEmail", agentEmail)
                .append("userMessage", userMessage)
                .append("userEmail", userEmail)
                .append("callback", callback)
                .append("phone", phone)
                .append("userName", userName)
                .append("interest", interest)
                .append("userUid", userUid)
                .append("cartCode", cartCode)
                .append("anonymousUser", anonymousUser)
                .toString();
    }


    public FindAgentMailEvent(String agentEmail, String userMessage, String userEmail, String callback, String phone, String userName, String interest)
    {
        this.agentEmail = agentEmail;
        this.userMessage = userMessage;
        this.userEmail = userEmail;
        this.callback = callback;
        this.phone = phone;
        this.userName = userName;
        this.interest = interest;
    }

    public String getInterest()
    {
        return interest;
    }

    public String getAgentEmail()
    {
        return agentEmail;
    }

    public String getUserMessage()
    {
        return userMessage;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public String getCallback()
    {
        return callback;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserUid(final String userUid)
    {
        this.userUid = userUid;
    }


    public String getUserUid()
    {
        return userUid;
    }

    public void setCartCode(final String cartCode)
    {
        this.cartCode = cartCode;
    }

    public String getCartCode()
    {
        return cartCode;
    }

    public void setAnonymousUser(final boolean anonymousUser)
    {
        this.anonymousUser = anonymousUser;
    }

    public boolean isAnonymousUser()
    {
        return anonymousUser;
    }
}
