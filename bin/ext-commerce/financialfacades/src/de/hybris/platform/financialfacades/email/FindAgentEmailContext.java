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

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.model.process.FindAgentEmailProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Velocity context for a customer email.
 */
public class FindAgentEmailContext extends AbstractEmailContext<FindAgentEmailProcessModel>
{
    private Logger LOG = Logger.getLogger(FindAgentEmailContext.class);


    public static final String USER_MESSAGE = "userMessage";
    public static final String USER_EMAIL = "userEmail";
    public static final String CALLBACK = "callback";
    public static final String PHONE = "phone";
    public static final String USER_NAME = "userName";
    public static final String INTEREST= "interest";
    private static final String CART_CODE = "cartCode";
    private static final String USER_UID = "userUid";

    public static final String STRING_ESCAPE_UTILS= "stringEscapeUtils";


    private Converter<UserModel, CustomerData> customerConverter;
    private CustomerData customerData;
    private String userId;
    private boolean anonymousUser;

    @Override
    protected CustomerModel getCustomer(FindAgentEmailProcessModel businessProcessModel)
    {
        return businessProcessModel.getCustomer();
    }

    @Override
    public void init(final FindAgentEmailProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
    {
        super.init(storeFrontCustomerProcessModel, emailPageModel);
        put(USER_MESSAGE, storeFrontCustomerProcessModel.getUserMessage());
        put(USER_EMAIL, storeFrontCustomerProcessModel.getUserEmail());
        put(CALLBACK, storeFrontCustomerProcessModel.getCallback());
        put(PHONE, storeFrontCustomerProcessModel.getPhone());
        put(USER_NAME, storeFrontCustomerProcessModel.getUserName());
        put(INTEREST, storeFrontCustomerProcessModel.getInterest());
        put(CART_CODE, storeFrontCustomerProcessModel.getCartCode());
        put(USER_UID, storeFrontCustomerProcessModel.getUserUid());

        put(STRING_ESCAPE_UTILS, StringEscapeUtils.class);

        customerData = getCustomerConverter().convert(getCustomer(storeFrontCustomerProcessModel));
        userId = storeFrontCustomerProcessModel.getUserUid();
        anonymousUser = storeFrontCustomerProcessModel.isAnonymousUser();
    }

    @Override
    protected BaseSiteModel getSite(final FindAgentEmailProcessModel storeFrontCustomerProcessModel)
    {
        return storeFrontCustomerProcessModel.getSite();
    }

    protected Converter<UserModel, CustomerData> getCustomerConverter()
    {
        return customerConverter;
    }

    @Required
    public void setCustomerConverter(final Converter<UserModel, CustomerData> customerConverter)
    {
        this.customerConverter = customerConverter;
    }

    public CustomerData getCustomer()
    {
        return customerData;
    }

    @Override
    protected LanguageModel getEmailLanguage(final FindAgentEmailProcessModel businessProcessModel)
    {
        return businessProcessModel.getLanguage();
    }

    public String getUserId()
    {
        return userId;
    }

    public boolean isAnonymousUser()
    {
        return anonymousUser;
    }
}
