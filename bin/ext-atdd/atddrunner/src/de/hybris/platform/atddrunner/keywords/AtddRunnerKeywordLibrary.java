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
package de.hybris.platform.atddrunner.keywords;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.atddrunner.service.IntegrationServletContainer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Server managing ATDD library.
 */
public class AtddRunnerKeywordLibrary extends AbstractKeywordLibrary
{
    private static final Logger LOG = LoggerFactory.getLogger(AtddRunnerKeywordLibrary.class);

    // ATDD does not support {@code ApplicationContextAware}, so we have to
    // autowire the context.
    @Autowired
    private ApplicationContext appContext;

    /**
     * Start servlet container with given servlet.
     *
     * @param beanId Spring bean id of servlet definition. Must be a descendant of {@code atddServletContainer}.
     */
    public void webServerRun(final String beanId)
    {
        LOG.trace("Starting bean {} as a servlet", beanId);
        getBean(beanId).start();
    }

    /**
     * Terminate server, which was started by {@link AtddRunnerKeywordLibrary#webServerRun(String)}.
     *
     * @param beanId Spring bean id of servlet definition. Must be a descendant of {@code atddServletContainer}.
     */
    public void webServerTerminate(final String beanId)
    {
        LOG.trace("Stopping servlet produced by bean {}", beanId);
        getBean(beanId).stop();
    }

    protected IntegrationServletContainer getBean(final String id)
    {
        final IntegrationServletContainer bean = appContext.getBean(id, IntegrationServletContainer.class);
        if (bean == null)
        {
            throw new IllegalArgumentException("Bean " + id + " does not exist");
        }
        return bean;
    }
}
