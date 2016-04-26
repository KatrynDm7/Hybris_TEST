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
package com.sap.hybris.reco.dao;

import java.util.ArrayList;
import java.util.List;

import com.sap.hybris.reco.model.CMSSAPRecommendationComponentModel;
import org.apache.commons.lang.StringUtils;


/**
 * 
 */
public class RecommendationContext
{
	private String productId;
	private String itemType;
	private boolean includeCart;
	private String userId;
	private String userType;
	private String recotype;
	private String usage;
	private String cookieId;
	
	private final List<String> cartItems = new ArrayList<String>();
	private CMSSAPRecommendationComponentModel componentModel;

	public String getProductId()
	{
		return productId;
	}

	public String getUserId()
	{
		return userId;
	}

	public List<String> getCartItems()
	{
		return cartItems;
	}

	public void addCartItem(final String cartItem)
	{
		cartItems.add(cartItem);
	}

	public void setProductId(final String productId)
	{
		this.productId = productId;
	}

	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	public CMSSAPRecommendationComponentModel getComponentModel()
	{
		return componentModel;
	}

	public void setComponentModel(final CMSSAPRecommendationComponentModel componentModel)
	{
		this.componentModel = componentModel;
	}

	public String getItemDataSourceType()
	{
		return itemType;
	}

	public void setItemDataSourceType(String itemType)
	{
      this.itemType = itemType;
	}

	public boolean getIncludeCart()
	{
      return includeCart;
	}

	public void setIncludeCart(boolean includeCart)
	{
      this.includeCart = includeCart;
	}

	public String getRecotype()
	{
		return recotype;
	}

	public void setRecotype(String recotype)
	{
		this.recotype = recotype;
	}
	
	public String getUsage()
	{
		return usage;
	}

	public void setUsage(String usage)
	{
		this.usage = usage;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{		
		this.userType = userType;
	}
	
	public String getSessionKey()
	{
		if (this.getComponentModel() != null)
		{
			return this.getComponentModel().getUid();
		}
		return StringUtils.EMPTY;
	}

	public String getCookieId()
	{
		return cookieId;
	}

	public void setCookieId(String cookieId)
	{
		this.cookieId = cookieId;
	}

}