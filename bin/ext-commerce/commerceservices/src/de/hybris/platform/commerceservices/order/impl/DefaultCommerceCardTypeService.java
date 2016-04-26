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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCardTypeService;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.payment.dto.CardType;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of service which returns defined card types
 */
public class DefaultCommerceCardTypeService implements CommerceCardTypeService
{
	private EnumerationService enumerationService;
	private TypeService typeService;

	private Collection<CardType> cardTypes;

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected void createCardTypeCollection()
	{
		final List<CreditCardType> creditCardTypes = getEnumerationService().getEnumerationValues(CreditCardType._TYPECODE);
		cardTypes = new ArrayList<CardType>();
		CardType cardType;
		for (final CreditCardType creditCardType : creditCardTypes)
		{
			cardType = new CardType(creditCardType.getCode(), creditCardType, getTypeService().getEnumerationValue(creditCardType)
					.getName());
			cardTypes.add(cardType);
		}
	}

	@Override
	public Collection<CardType> getCardTypes()
	{
		if (cardTypes == null)
		{
			createCardTypeCollection();
		}

		return cardTypes;
	}

	@Override
	public CardType getCardTypeForCode(final String code)
	{
		if (cardTypes == null)
		{
			createCardTypeCollection();
		}

		for (final CardType cardType : cardTypes)
		{
			if (cardType.getCode().getCode().equals(code))
			{
				return cardType;
			}
		}
		return null;
	}

}
