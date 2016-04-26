/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.eventtracking.publisher.csv.model;


/**
 * @author stevo.slavic
 *
 */
public class TrackingEventCsvData
{
	private String eventType;
	private String url;
	private String timestamp;
	private String sessionId;
	private String userId;
	private String userEmail;
	private String categoryId;
	private String categoryName;
	private String productId;
	private String productName;
	private String productMediaType;
	private String searchTerm;
	private String postcode;
	private String bannerId;
	private String cartId;
	private String cartAbandonmentReason;
	private String quantity;
	private String piwikId;
	private String refUrl;

	/**
	 * @return the eventType
	 */
	public String getEventType()
	{
		return eventType;
	}

	/**
	 * @param eventType
	 *           the eventType to set
	 */
	public void setEventType(final String eventType)
	{
		this.eventType = eventType;
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 *           the url to set
	 */
	public void setUrl(final String url)
	{
		this.url = url;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp()
	{
		return timestamp;
	}

	/**
	 * @param timestamp
	 *           the timestamp to set
	 */
	public void setTimestamp(final String timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId()
	{
		return sessionId;
	}

	/**
	 * @param sessionId
	 *           the sessionId to set
	 */
	public void setSessionId(final String sessionId)
	{
		this.sessionId = sessionId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * @param userId
	 *           the userId to set
	 */
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail()
	{
		return userEmail;
	}

	/**
	 * @param userEmail
	 *           the userEmail to set
	 */
	public void setUserEmail(final String userEmail)
	{
		this.userEmail = userEmail;
	}

	/**
	 * @return the categoryId
	 */
	public String getCategoryId()
	{
		return categoryId;
	}

	/**
	 * @param categoryId
	 *           the categoryId to set
	 */
	public void setCategoryId(final String categoryId)
	{
		this.categoryId = categoryId;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName()
	{
		return categoryName;
	}

	/**
	 * @param categoryName
	 *           the categoryName to set
	 */
	public void setCategoryName(final String categoryName)
	{
		this.categoryName = categoryName;
	}

	/**
	 * @return the productId
	 */
	public String getProductId()
	{
		return productId;
	}

	/**
	 * @param productId
	 *           the productId to set
	 */
	public void setProductId(final String productId)
	{
		this.productId = productId;
	}

	/**
	 * @return the productName
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * @param productName
	 *           the productName to set
	 */
	public void setProductName(final String productName)
	{
		this.productName = productName;
	}

	/**
	 * @return the productMediaType
	 */
	public String getProductMediaType()
	{
		return productMediaType;
	}

	/**
	 * @param productMediaType
	 *           the productMediaType to set
	 */
	public void setProductMediaType(final String productMediaType)
	{
		this.productMediaType = productMediaType;
	}

	/**
	 * @return the searchTerm
	 */
	public String getSearchTerm()
	{
		return searchTerm;
	}

	/**
	 * @param searchTerm
	 *           the searchTerm to set
	 */
	public void setSearchTerm(final String searchTerm)
	{
		this.searchTerm = searchTerm;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode()
	{
		return postcode;
	}

	/**
	 * @param postcode
	 *           the postcode to set
	 */
	public void setPostcode(final String postcode)
	{
		this.postcode = postcode;
	}

	/**
	 * @return the bannerId
	 */
	public String getBannerId()
	{
		return bannerId;
	}

	/**
	 * @param bannerId
	 *           the bannerId to set
	 */
	public void setBannerId(final String bannerId)
	{
		this.bannerId = bannerId;
	}

	/**
	 * @return the cartId
	 */
	public String getCartId()
	{
		return cartId;
	}

	/**
	 * @param cartId
	 *           the cartId to set
	 */
	public void setCartId(final String cartId)
	{
		this.cartId = cartId;
	}

	/**
	 * @return the cartAbandonmentReason
	 */
	public String getCartAbandonmentReason()
	{
		return cartAbandonmentReason;
	}

	/**
	 * @param cartAbandonmentReason
	 *           the cartAbandonmentReason to set
	 */
	public void setCartAbandonmentReason(final String cartAbandonmentReason)
	{
		this.cartAbandonmentReason = cartAbandonmentReason;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity()
	{
		return quantity;
	}

	/**
	 * @param quantity
	 *           the quantity to set
	 */
	public void setQuantity(final String quantity)
	{
		this.quantity = quantity;
	}
	
	/**
	* @return the piwikId
	*/
	public String getPiwikId()
	{
		return piwikId;
	}

	/**
	* @param piwikId
	*           the piwikId to set
	*/
	public void setPiwikId(final String piwikId)
	{
		this.piwikId = piwikId;
	}
	
	/**
	* @return the refUrl
	*/
	public String getRefUrl()
	{
		return refUrl;
	}

	/**
	* @param refUrl
	*           the refUrl to set
	*/
	public void setRefUrl(final String refUrl)
	{
		this.refUrl = refUrl;
	}
}
