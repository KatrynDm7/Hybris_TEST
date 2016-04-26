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
package de.hybris.platform.commerceservices.request.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Annotation allows override existing request mapping, defined by {@link RequestMapping @RequestMapping} annotation. It
 * can be used, to set priorities for methods with identical {@code @RequestMapping} annotation. Thanks to that, error
 * like java.lang.IllegalStateException: Ambiguous mapping found.., can be avoided. Method with highest priority will be
 * added as handler mapping.
 */
@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMappingOverride
{
	/**
	 * Name for property, which stores priority value
	 */
	String priorityProperty() default "";
}
