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
package de.hybris.platform.commerceservices.spring.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * List Merge Directives allow an AddOn to merge additional elements into spring-bean lists AND list properties on
 * Spring Beans. The minimal property to set is the add. This will append to the end of the list and is preferable to
 * using the list merge feature of spring since you are not required to extend and re-alias the original list bean
 * meaning the AddOns changes are more isolated from the AddOns. However, the directive also supports the ability to
 * insert the bean before or after a specified list element bean definition or bean class. List Merge Directive bean
 * definitions must also include a depends-on qualifier which should be the list-bean or the bean enclosing the list
 * property.
 */
public class ListMergeDirective
{

	private Object add;
	private String listPropertyDescriptor;
	private String fieldName;
	private List<String> afterBeanNames;
	private List<String> beforeBeanNames;
	private List<Object> afterValues;
	private List<Object> beforeValues;
	private List<Class> afterClasses;
	private List<Class> beforeClasses;

	/**
	 * @return the add
	 */
	public Object getAdd()
	{
		return add;
	}

	/**
	 * @param add
	 *           The Bean or Element to Add
	 */
	@Required
	public void setAdd(final Object add)
	{
		this.add = add;
	}

	/**
	 * @return the after
	 */
	public List<String> getAfterBeanNames()
	{
		return afterBeanNames;
	}

	/**
	 * Add the element after the specified bean names (if the target list is a list of beans)
	 * 
	 * @param after
	 *           the after to set
	 */
	public void setAfterBeanNames(final List<String> after)
	{
		this.afterBeanNames = after;
	}

	/**
	 * @return the beforeBeanNames
	 */
	public List<String> getBeforeBeanNames()
	{
		return beforeBeanNames;
	}

	/**
	 * Insert the element before the specified bean names (if the target list is a list of beans)
	 * 
	 * @param beforeBeanNames
	 *           the beforeBeanNames to set
	 */
	public void setBeforeBeanNames(final List<String> beforeBeanNames)
	{
		this.beforeBeanNames = beforeBeanNames;
	}

	/**
	 * Add the element after all the elements which are assignable from the specified classes.
	 * 
	 * @return the afterClasses
	 */
	public List<Class> getAfterClasses()
	{
		return afterClasses;
	}

	/**
	 * @param afterClasses
	 *           the afterClasses to set
	 */
	public void setAfterClasses(final List<Class> afterClasses)
	{
		this.afterClasses = afterClasses;
	}

	/**
	 * @return the beforeClasses
	 */
	public List<Class> getBeforeClasses()
	{
		return beforeClasses;
	}

	/**
	 * Insert the element before all the elements which are assignable from the specified classes.
	 * 
	 * @param beforeClasses
	 *           the afterClasses to set
	 */
	public void setBeforeClasses(final List<Class> beforeClasses)
	{
		this.beforeClasses = beforeClasses;
	}

	public String getListPropertyDescriptor()
	{
		return listPropertyDescriptor;
	}

	/**
	 * If the dependency bean is not the actual list, then use the property descriptor to identify the list property. The
	 * Property descriptor uses Apache Commons BeanUtils syntax.
	 */
	public void setListPropertyDescriptor(final String listPropertyDescriptor)
	{
		this.listPropertyDescriptor = listPropertyDescriptor;
	}

	public List<Object> getAfterValues()
	{
		return afterValues;
	}

	/**
	 * Adds the element after the specified values (useful for merging into lists of atomic types)
	 * 
	 * @param afterValues
	 */
	public void setAfterValues(final List<Object> afterValues)
	{
		this.afterValues = afterValues;
	}

	public List<Object> getBeforeValues()
	{
		return beforeValues;
	}

	/**
	 * Inserts the element before the specified values (useful for merging into lists of atomic types)
	 * 
	 * @param beforeValues
	 */
	public void setBeforeValues(final List<Object> beforeValues)
	{
		this.beforeValues = beforeValues;
	}

	public String getFieldName()
	{
		return fieldName;
	}


	/**
	 * field name of list / map - for use with reflection
	 * 
	 * @param fieldName
	 */
	public void setFieldName(final String fieldName)
	{
		this.fieldName = fieldName;
	}

}
