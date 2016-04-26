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

import de.hybris.platform.commerceservices.model.process.FindAgentEmailProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * This class listen all FindAgentMailEvent events.
 */
public class FindAgentMailEventListener extends AbstractEventListener<FindAgentMailEvent>
{

    private static Logger LOG = Logger.getLogger(FindAgentMailEventListener.class);

    private ModelService modelService;
    private BusinessProcessService businessProcessService;

    protected BusinessProcessService getBusinessProcessService()
    {
        return businessProcessService;
    }

    @Required
    public void setBusinessProcessService(final BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }

    /**
     * @return the modelService
     */
    protected ModelService getModelService()
    {
        return modelService;
    }

    /**
     * @param modelService
     *           the modelService to set
     */
    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    /**
     * Launch findAgentEmailProcess.
     *
     * @param registerEvent
     */
    @Override
    protected void onEvent(FindAgentMailEvent registerEvent)
    {
        LOG.info(registerEvent);
        final FindAgentEmailProcessModel findAgentEmailProcess = getBusinessProcessService()
                .createProcess(
                        "findAgentEmailProcess-" + registerEvent.hashCode(),
                        "findAgentEmailProcess");
        findAgentEmailProcess.setSite(registerEvent.getSite());
        findAgentEmailProcess.setStore(registerEvent.getBaseStore());
        findAgentEmailProcess.setCustomer(registerEvent.getCustomer());
        findAgentEmailProcess.setAgentEmail(registerEvent.getAgentEmail());
        findAgentEmailProcess.setUserMessage(registerEvent.getUserMessage());
        findAgentEmailProcess.setLanguage(registerEvent.getLanguage());
        findAgentEmailProcess.setUserEmail(registerEvent.getUserEmail());
        findAgentEmailProcess.setCallback(registerEvent.getCallback());
        findAgentEmailProcess.setPhone(registerEvent.getPhone());
        findAgentEmailProcess.setUserName(registerEvent.getUserName());
        findAgentEmailProcess.setInterest(registerEvent.getInterest());
        findAgentEmailProcess.setUserUid(registerEvent.getUserUid());
        findAgentEmailProcess.setCartCode(registerEvent.getCartCode());
        findAgentEmailProcess.setAnonymousUser(registerEvent.isAnonymousUser());

        getModelService().save(findAgentEmailProcess);
        getBusinessProcessService().startProcess(findAgentEmailProcess);
    }
}
