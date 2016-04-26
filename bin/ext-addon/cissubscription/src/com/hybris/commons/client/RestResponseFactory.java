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
package com.hybris.commons.client;

import java.net.URI;

import com.sun.jersey.api.client.ClientResponse;

/**
 *  Factory creates instance of RestResponse with stub values.
 */
public class RestResponseFactory {
	public static <T> RestResponse<T> newStubInstance() {
		final ClientResponse response = new ClientResponse(200, null, null, null) { 
			@Override
			public URI getLocation() {
				return null;
			}
		};
		return new RestResponse<T>(response) {
			@Override
			public T getResult() {
				return null;
			}
		};
	}
}
