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
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.addons.v2.spock

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.XML
import static org.apache.http.HttpStatus.SC_ACCEPTED
import static org.apache.http.HttpStatus.SC_BAD_REQUEST
import static org.apache.http.HttpStatus.SC_CREATED
import static org.apache.http.HttpStatus.SC_OK
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.carts.AbstractCartTest

import spock.lang.Unroll
import groovy.util.slurpersupport.NodeChild
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient

@Unroll
@ManualTest
class SopTest extends AbstractCartTest {
	protected static final String RESPONSE_URL = 'https://localhost:9002/storefront/sop/response'
	protected static final String MERCHANT_CALLBACK_URL = '/integration/merchant_callback'
	protected static final String EXTENDED_MERCHANT_CALLBACK_URL = '/payment/sop/response'

	def "Get SOP payment request details : #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]

		when:"application get details for SOP payment request"
		HttpResponseDecorator response = restClient.get(
				path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/carts/'+cart.code+'/payment/sop/request',
				query : ['responseUrl' : RESPONSE_URL],
				contentType : format,
				requestContentType : URLENC
				)
		then:
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))
				println(data)

			status == SC_OK
			isNotEmpty(data.postUrl);
			def resp = data.parameters.entry;
			resp.find{it.key == 'billTo_email' && it.value == customer.id}
			resp.find{it.key == 'billTo_firstName' && it.value == CUSTOMER_FIRST_NAME}
			resp.find{it.key == 'billTo_lastName' && it.value == CUSTOMER_LAST_NAME}
			resp.find{it.key == 'billTo_street1' && it.value == CUSTOMER_ADDRESS_LINE1}
			resp.find{it.key == 'billTo_street2' && it.value == CUSTOMER_ADDRESS_LINE2}
			resp.find{it.key == 'billTo_city' && it.value == CUSTOMER_ADDRESS_TOWN}
			resp.find{it.key == 'billTo_country' && it.value == CUSTOMER_ADDRESS_COUNTRY_ISO_CODE}
			resp.find{it.key == 'billTo_postalCode' && it.value == CUSTOMER_ADDRESS_POSTAL_CODE}
			resp.find{it.key == 'shipTo_firstName' && it.value == CUSTOMER_FIRST_NAME}
			resp.find{it.key == 'shipTo_lastName' && it.value == CUSTOMER_LAST_NAME}
			resp.find{it.key == 'shipTo_street1' && it.value == CUSTOMER_ADDRESS_LINE1}
			resp.find{it.key == 'shipTo_street2' && it.value == CUSTOMER_ADDRESS_LINE2}
			resp.find{it.key == 'shipTo_city' && it.value == CUSTOMER_ADDRESS_TOWN}
			resp.find{it.key == 'shipTo_country' && it.value == CUSTOMER_ADDRESS_COUNTRY_ISO_CODE}
			resp.find{it.key == 'shipTo_postalCode' && it.value == CUSTOMER_ADDRESS_POSTAL_CODE}
			resp.find{it.key == 'shipTo_shippingMethod' && it.value == DELIVERY_STANDARD_NET}
			resp.find{it.key == 'orderPage_cancelResponseURL' && it.value == RESPONSE_URL}
			resp.find{it.key == 'orderPage_declineResponseURL' && it.value == RESPONSE_URL}
			resp.find{it.key == 'orderPage_receiptResponseURL' && it.value == RESPONSE_URL}
			def merchantURLPostAddress = resp.find{ it.key == 'orderPage_merchantURLPostAddress' }
			merchantURLPostAddress.value.toString().endsWith(MERCHANT_CALLBACK_URL)
		}

		where:
		format << [JSON]
	}

	def "Get SOP payment request details for extended merchant callback: #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]

		when:"application get details for SOP payment request"
		HttpResponseDecorator response = restClient.get(
				path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/carts/'+cart.code+'/payment/sop/request',
				query : ['responseUrl' : RESPONSE_URL,
					'extendedMerchantCallback':true],
				contentType : format,
				requestContentType : URLENC
				)
		then:
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))
				println(data)

			status == SC_OK
			isNotEmpty(data.postUrl);
			def resp = data.parameters.entry;

			resp.find{it.key == 'billTo_email' && it.value == customer.id}
			resp.find{it.key == 'billTo_firstName' && it.value == CUSTOMER_FIRST_NAME}
			resp.find{it.key == 'billTo_lastName' && it.value == CUSTOMER_LAST_NAME}
			resp.find{it.key == 'billTo_street1' && it.value == CUSTOMER_ADDRESS_LINE1}
			resp.find{it.key == 'billTo_street2' && it.value == CUSTOMER_ADDRESS_LINE2}
			resp.find{it.key == 'billTo_city' && it.value == CUSTOMER_ADDRESS_TOWN}
			resp.find{it.key == 'billTo_country' && it.value == CUSTOMER_ADDRESS_COUNTRY_ISO_CODE}
			resp.find{it.key == 'billTo_postalCode' && it.value == CUSTOMER_ADDRESS_POSTAL_CODE}
			resp.find{it.key == 'shipTo_firstName' && it.value == CUSTOMER_FIRST_NAME}
			resp.find{it.key == 'shipTo_lastName' && it.value == CUSTOMER_LAST_NAME}
			resp.find{it.key == 'shipTo_street1' && it.value == CUSTOMER_ADDRESS_LINE1}
			resp.find{it.key == 'shipTo_street2' && it.value == CUSTOMER_ADDRESS_LINE2}
			resp.find{it.key == 'shipTo_city' && it.value == CUSTOMER_ADDRESS_TOWN}
			resp.find{it.key == 'shipTo_country' && it.value == CUSTOMER_ADDRESS_COUNTRY_ISO_CODE}
			resp.find{it.key == 'shipTo_postalCode' && it.value == CUSTOMER_ADDRESS_POSTAL_CODE}
			resp.find{it.key == 'shipTo_shippingMethod' && it.value == DELIVERY_STANDARD_NET}
			resp.find{it.key == 'orderPage_cancelResponseURL' && it.value == RESPONSE_URL}
			resp.find{it.key == 'orderPage_declineResponseURL' && it.value == RESPONSE_URL}
			resp.find{it.key == 'orderPage_receiptResponseURL' && it.value == RESPONSE_URL}
			def merchantURLPostAddress = resp.find{ it.key == 'orderPage_merchantURLPostAddress' }
			merchantURLPostAddress.value.toString().endsWith(EXTENDED_MERCHANT_CALLBACK_URL)
		}

		where:
		format << [JSON]
	}

	def "Customer create payment info in SOP flow : #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]
		def sopRequestDetails = getSopRequestDetails(restClient,customer,cart.code, format,false, getBasePathWithIntegrationSite())

		when:"customer create subscription in payment provider"
		Map sopResponseParameters = createSubscriptionInPaymentProvider(sopRequestDetails.postUrl,createSopRequestParameters(sopRequestDetails.parameters.entry));
		sopResponseParameters.put("savePaymentInfo",true);


		and: "forward payment provider response to web services to create payment info"
		HttpResponseDecorator paymentInfoResponse = restClient.post(path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/carts/'+cart.code+"/payment/sop/response",
		query : sopResponseParameters,contentType : format,
		requestContentType : URLENC
		)
		then:"payment info is created"
		with(paymentInfoResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status ==  SC_OK
			data.defaultPayment == false
			data.saved == true
			data.accountHolderName == DEFAULT_PAYMENT.accountHolderName
			data.cardNumber.toString().substring(12) == DEFAULT_PAYMENT.cardNumber.substring(12)
			data.cardType.code == DEFAULT_PAYMENT.cardType
			data.expiryYear == DEFAULT_PAYMENT.expiryYear
			Integer.parseInt(data.expiryMonth) == Integer.parseInt(DEFAULT_PAYMENT.expiryMonth)
			data.billingAddress.firstName == CUSTOMER_FIRST_NAME
			data.billingAddress.lastName == CUSTOMER_LAST_NAME
			data.billingAddress.line1 == CUSTOMER_ADDRESS_LINE1
			data.billingAddress.line2 == CUSTOMER_ADDRESS_LINE2
			data.billingAddress.postalCode == CUSTOMER_ADDRESS_POSTAL_CODE
			data.billingAddress.town == CUSTOMER_ADDRESS_TOWN
			data.billingAddress.country.isocode == CUSTOMER_ADDRESS_COUNTRY_ISO_CODE
		}
		where:
		format << [JSON]
	}

	def "Customer doesnt fill credit cart details in SOP flow : #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]
		def sopRequestDetails = getSopRequestDetails(restClient,customer,cart.code, format,false, getBasePathWithIntegrationSite())

		when:"customer send incorrect request to payment provider"
		Map sopResponseParameters = createSubscriptionInPaymentProvider(sopRequestDetails.postUrl,createSopRequestParameters(sopRequestDetails.parameters.entry,false));
		and: "forward payment provider response to web services to create payment info"
		HttpResponseDecorator paymentInfoResponse = restClient.post(path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/carts/'+cart.code+"/payment/sop/response",
		query : sopResponseParameters,contentType : format,
		requestContentType : URLENC
		)
		then:"get error information"
		with(paymentInfoResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status ==  SC_BAD_REQUEST
			data.errors.find{it.subject=='card_cvNumber' && it.reason=='missing'}
			data.errors.find{it.subject=='card_expirationMonth' && it.reason=='missing'}
			data.errors.find{it.subject=='card_cardType' && it.reason=='missing'}
			data.errors.find{it.subject=='card_expirationYear' && it.reason=='missing'}
			data.errors.find{it.subject=='card_accountNumber' && it.reason=='missing'}
		}
		where:
		format << [JSON]
	}

	def "Customer send incorrect data for payment provider : #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]
		def sopRequestDetails = getSopRequestDetails(restClient,customer,cart.code, format,false, getBasePathWithIntegrationSite())

		when:"customer send incorrect request to payment provider"
		Map sopRequestParameters = createSopRequestParameters(sopRequestDetails.parameters);
		sopRequestParameters.put("billTo_country", "    ");
		Map sopResponseParameters = createSubscriptionInPaymentProvider(sopRequestDetails.postUrl,sopRequestParameters);

		and: "forward payment provider response to web services to create payment info"
		HttpResponseDecorator paymentInfoResponse = restClient.post(path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/carts/'+cart.code+"/payment/sop/response",
		query : sopResponseParameters,contentType : format,
		requestContentType : URLENC
		)
		then:"get error information"
		with(paymentInfoResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status ==  SC_BAD_REQUEST
			data.errors.find{it.subject=='billTo_country' && it.reason=='invalid'}
		}
		where:
		format << [JSON]
	}

	def "Customer places oder using SOP : #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]
		def sopRequestDetails = getSopRequestDetails(restClient,customer,cart.code, format, false,getBasePathWithIntegrationSite())

		when:"customer create subscription in payment provider"
		Map sopResponseParameters = createSubscriptionInPaymentProvider(sopRequestDetails.postUrl,createSopRequestParameters(sopRequestDetails.parameters.entry));

		and: "forward payment provider response to web services to create payment info"
		HttpResponseDecorator paymentInfoResponse = restClient.post(path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/carts/'+cart.code+"/payment/sop/response",
		query : sopResponseParameters,contentType : format,
		requestContentType : URLENC
		)
		assert paymentInfoResponse.status == SC_OK

		and:"place order"
		HttpResponseDecorator response = restClient.post(
				path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/orders',
				query : ['cartId' : cart.code,
					'securityCode' : '123',
					'fields' : FIELD_SET_LEVEL_FULL
				],
				contentType : format,
				requestContentType : URLENC
				)
		then: "order is properly placed"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_CREATED
			isNotEmpty(data.created)
			data.guestCustomer == false
		}
		where:
		format << [JSON]
	}

	private String getGuestUid() {
		def randomUID = System.currentTimeMillis()
		def guestUid = "${randomUID}@test.com"
		return guestUid;
	}

	def "Guest places oder using SOP : #format"(){
		given: "a guest customer with cart ready for ordering"
		authorizeClient(restClient)
		def userGuid = getGuestUid()
		def anonymous = ['id': 'anonymous']
		def cart = createAnonymousCart(restClient, format, getBasePathWithIntegrationSite())
		addProductToCartOnline(restClient, anonymous, cart.guid, PRODUCT_POWER_SHOT_A480, 1, format, getBasePathWithIntegrationSite())
		addEmailToAnonymousCart(restClient, cart.guid, userGuid, format, getBasePathWithIntegrationSite()) //setting email address on cart makes it recognized as guest cart
		setAddressForAnonymousCart(restClient, GOOD_ADDRESS_DE, cart.guid, format , getBasePathWithIntegrationSite())
		setDeliveryModeForCart(restClient, anonymous, cart.guid, DELIVERY_STANDARD_NET, format, getBasePathWithIntegrationSite())

		def sopRequestDetails = getSopRequestDetails(restClient,anonymous,cart.guid, format, false,getBasePathWithIntegrationSite())

		when:"customer create subscription in payment provider"
		Map sopResponseParameters = createSubscriptionInPaymentProvider(sopRequestDetails.postUrl,createSopRequestParameters(sopRequestDetails.parameters.entry));

		and: "forward payment provider response to web services to create payment info"
		HttpResponseDecorator paymentInfoResponse = restClient.post(path : getBasePathWithIntegrationSite() + '/users/'+ anonymous.id +'/carts/'+cart.guid+"/payment/sop/response",
		query : sopResponseParameters,contentType : format,
		requestContentType : URLENC
		)
		assert paymentInfoResponse.status == SC_OK

		and:"place order"
		HttpResponseDecorator response = restClient.post(
				path : getBasePathWithIntegrationSite() + '/users/'+ anonymous.id +'/orders',
				query : ['cartId' : cart.guid,
					'securityCode' : '123',
					'fields' : FIELD_SET_LEVEL_FULL
				],
				contentType : format,
				requestContentType : URLENC
				)
		then: "order is properly placed"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_CREATED
			isNotEmpty(data.created)
			data.guestCustomer == true
		}
		where:
		format << [JSON]
	}

	/**
	 * Creates customer, cart and start checkout flow (set delivery address and delivery mode)
	 * @param format - format for requests
	 * @return created customer and cart
	 */
	protected createCustomerWithCartForIntegrationSite(format)
	{
		def val = createAndAuthorizeCustomerWithCart(restClient, format, getBasePathWithIntegrationSite())
		def customer = val[0]
		def cart = val[1]
		addProductToCartOnline(restClient, customer, cart.code, PRODUCT_POWER_SHOT_A480, 1, format, getBasePathWithIntegrationSite())
		def address = createAddress(restClient, customer, format, getBasePathWithIntegrationSite())
		setDeliveryAddressForCart(restClient, customer, cart.code, address.id, format, getBasePathWithIntegrationSite())
		setDeliveryModeForCart(restClient, customer, cart.code, DELIVERY_STANDARD_NET, format, getBasePathWithIntegrationSite())
		return val;
	}

	/**
	 * Send request to payment provider
	 * @param sopRequestDetails - information needed to send request to payment provider
	 * @return map with parameters returned by payment provider
	 */
	protected createSubscriptionInPaymentProvider(postUrl,bodyParams)
	{
		RESTClient paymentProviderClient = createRestClient(postUrl)
		def paymentProviderResponse = paymentProviderClient.post(body:bodyParams,contentType:TEXT,requestContentType : URLENC)
		assert paymentProviderResponse.status == SC_OK
		def parser = new XmlSlurper(false, false ,true)
		parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
		String htmlResponse = paymentProviderResponse.data.text;
		//println htmlResponse;
		NodeChild htmlData = parser.parseText(htmlResponse)
		Map sopResponseParameters = getSopResponseParametersFromHtml(htmlData);
		return sopResponseParameters;
	}

	/**
	 * This method get information needed to send SOP subscription request.
	 * @param client REST client to be used
	 * @param customer customer to whom the cart belongs
	 * @param cartID Id of cart for which
	 * @param format format to be used
	 */
	protected getSopRequestDetails(RESTClient client, customer, cartId, format, extendedMerchantCallback=false, basePathWithSite=getBasePathWithSite()) {
		HttpResponseDecorator response = client.get(path : basePathWithSite + '/users/'+ customer.id +'/carts/'+cartId+'/payment/sop/request',
		query : ['responseUrl' : RESPONSE_URL,
			'extendedMerchantCallback':extendedMerchantCallback],
		contentType : format,
		requestContentType : URLENC)

		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			println data;
		}
		return response.data
	}

	/**
	 * Creates map with parameters which need to be send to payment provider
	 * @param sopRequestDetailsParameters - list of parameters got from server
	 * @return map with parameters which need to be send to payment provide
	 */
	protected Map createSopRequestParameters(sopRequestDetailsParameters,addCartParams=true) {
		Map paramMap;
		if(addCartParams){
			paramMap = ['card_expirationYear' : DEFAULT_PAYMENT.expiryYear,
				'card_cvNumber':123,
				'card_nameOnCard': DEFAULT_PAYMENT.accountHolderName,
				'card_accountNumber': DEFAULT_PAYMENT.cardNumber,
				'card_issueNumber':'',
				'card_expirationMonth':DEFAULT_PAYMENT.expiryMonth,
				'card_cardType':'001'
			];
		}else{
			paramMap = new HashMap();
		}

		for(def requestParam : sopRequestDetailsParameters){
			paramMap.putAt(requestParam.key,requestParam.value)
		}
		return paramMap
	}

	/**
	 * Method parse html response to get parameters send by payment provider
	 * @param htmlData - payment provider response in html
	 * @return parameters send by payment provider
	 */
	protected Map getSopResponseParametersFromHtml(NodeChild htmlData) {
		//amount,billTo_city,billTo_country,billTo_email,billTo_firstName,billTo_lastName,billTo_postalCode,billTo_street1,billTo_street2,card_accountNumber,card_cardType,card_expirationMonth,card_expirationYear,card_issueNumber,card_nameOnCard,ccAuthReply_cvCode,currency,decision,decision_publicSignature,merchantID,orderPage_cancelResponseURL,orderPage_colorScheme,orderPage_declineResponseURL,orderPage_ignoreAVS,orderPage_ignoreCVN,orderPage_merchantURLPostAddress,orderPage_receiptResponseURL,orderPage_serialNumber,orderPage_signaturePublic,orderPage_timestamp,orderPage_transactionType,orderPage_version,paySubscriptionCreateReply_subscriptionID,paySubscriptionCreateReply_subscriptionIDPublicSignature,reasonCode,recurringSubscriptionInfo_amount,recurringSubscriptionInfo_automaticRenew,recurringSubscriptionInfo_frequency,recurringSubscriptionInfo_numberOfPayments,recurringSubscriptionInfo_signaturePublic,recurringSubscriptionInfo_startDate,shipTo_city,shipTo_country,shipTo_firstName,shipTo_lastName,shipTo_postalCode,shipTo_shippingMethod,shipTo_street1,shipTo_street2
		Map paramMap = new HashMap();
		def params = htmlData.depthFirst().findAll({it.name()=='input'})
		for(int i=0;i<params.size;i++)
		{
			def param = params[i]
			//println param
			paramMap.put( param.@id,  param.@value)
		}
		return paramMap;
	}

	def "Customer create payment info in extended SOP flow : #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]
		def sopRequestDetails = getSopRequestDetails(restClient,customer,cart.code, format,true, getBasePathWithIntegrationSite())

		when:"customer send create subscription request to payment provider"
		RESTClient paymentProviderClient = createRestClient(sopRequestDetails.postUrl)
		def paymentProviderResponse = paymentProviderClient.post(body:createSopRequestParameters(sopRequestDetails.parameters.entry),requestContentType : URLENC)
		assert paymentProviderResponse.status == SC_OK

		and: "check response from payment provider in occ"
		HttpResponseDecorator paymentInfoResponse = waitForPaymentProviderResponse(customer.id,cart.code,format);
		assert paymentInfoResponse.status == SC_OK

		then:"payment info should be created"
		with(paymentInfoResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status ==  SC_OK
			data.defaultPayment == false
			data.saved == true
			data.accountHolderName == DEFAULT_PAYMENT.accountHolderName
			data.cardNumber.toString().substring(12) == DEFAULT_PAYMENT.cardNumber.substring(12)
			data.cardType.code == DEFAULT_PAYMENT.cardType
			data.expiryYear == DEFAULT_PAYMENT.expiryYear
			Integer.parseInt(data.expiryMonth) == Integer.parseInt(DEFAULT_PAYMENT.expiryMonth)
			data.billingAddress.firstName == CUSTOMER_FIRST_NAME
			data.billingAddress.lastName == CUSTOMER_LAST_NAME
			data.billingAddress.line1 == CUSTOMER_ADDRESS_LINE1
			data.billingAddress.line2 == CUSTOMER_ADDRESS_LINE2
			data.billingAddress.postalCode == CUSTOMER_ADDRESS_POSTAL_CODE
			data.billingAddress.town == CUSTOMER_ADDRESS_TOWN
			data.billingAddress.country.isocode == CUSTOMER_ADDRESS_COUNTRY_ISO_CODE
		}
		where:
		format << [JSON]
	}

	HttpResponseDecorator waitForPaymentProviderResponse(customerId,cartCode,format)
	{
		HttpResponseDecorator paymentInfoResponse;
		def status = SC_ACCEPTED;
		int counter = 0;
		while(status==SC_ACCEPTED && counter < 100)
		{
			counter++;
			paymentInfoResponse = restClient.get(path : getBasePathWithIntegrationSite() + '/users/'+ customerId +'/carts/'+cartCode+"/payment/sop/response",contentType : format,
			requestContentType : URLENC)
			status = paymentInfoResponse.status;
			if(status==SC_ACCEPTED)
				sleep(500);
		}
		return paymentInfoResponse;
	}

	def "Customer doesnt fill credit cart details in extended SOP flow : #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]
		def sopRequestDetails = getSopRequestDetails(restClient,customer,cart.code, format,true, getBasePathWithIntegrationSite())

		when:"customer send incorrect request to payment provider"
		RESTClient paymentProviderClient = createRestClient(sopRequestDetails.postUrl)
		def paymentProviderResponse = paymentProviderClient.post(body:createSopRequestParameters(sopRequestDetails.parameters.entry,false),requestContentType : URLENC)
		assert paymentProviderResponse.status == SC_OK

		and: "check response from payment provider in occ"
		HttpResponseDecorator paymentInfoResponse = waitForPaymentProviderResponse(customer.id,cart.code,format);

		then:"get error information"
		with(paymentInfoResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status ==  SC_BAD_REQUEST
			data.errors.find{it.subject=='card_cvNumber' && it.reason=='missing'}
			data.errors.find{it.subject=='card_expirationMonth' && it.reason=='missing'}
			data.errors.find{it.subject=='card_cardType' && it.reason=='missing'}
			data.errors.find{it.subject=='card_expirationYear' && it.reason=='missing'}
			data.errors.find{it.subject=='card_accountNumber' && it.reason=='missing'}
		}
		where:
		format << [JSON]
	}

	def "Customer send incorrect data for payment provider in extended SOP flow : #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]
		def sopRequestDetails = getSopRequestDetails(restClient,customer,cart.code, format,true, getBasePathWithIntegrationSite())

		when:"customer send incorrect request to payment provider"
		Map sopRequestParameters = createSopRequestParameters(sopRequestDetails.parameters.entry);
		sopRequestParameters.put("billTo_country", "    ");
		RESTClient paymentProviderClient = createRestClient(sopRequestDetails.postUrl)
		def paymentProviderResponse = paymentProviderClient.post(body:sopRequestParameters,requestContentType : URLENC)
		assert paymentProviderResponse.status == SC_OK

		and: "check response from payment provider in occ"
		HttpResponseDecorator paymentInfoResponse = waitForPaymentProviderResponse(customer.id,cart.code,format);

		then:"get error information"
		with(paymentInfoResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status ==  SC_BAD_REQUEST
			data.errors.find{
				it.subject=='billTo_country' && it.reason=='invalid'
			}
		}
		where:
		format << [JSON]
	}

	def "Customer places oder using extended SOP flow : #format"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]
		def sopRequestDetails = getSopRequestDetails(restClient,customer,cart.code, format,true,getBasePathWithIntegrationSite())

		when:"customer send create subscription request to payment provider"
		RESTClient paymentProviderClient = createRestClient(sopRequestDetails.postUrl)
		def paymentProviderResponse = paymentProviderClient.post(body:createSopRequestParameters(sopRequestDetails.parameters.entry),requestContentType : URLENC)
		assert paymentProviderResponse.status == SC_OK

		and: "check response from payment provider in occ"
		HttpResponseDecorator paymentInfoResponse = waitForPaymentProviderResponse(customer.id,cart.code,format);
		assert paymentInfoResponse.status == SC_OK

		and:"place order"
		HttpResponseDecorator response = restClient.post(
				path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/orders',
				query : ['cartId' : cart.code,
					'securityCode' : '123',
					'fields' : FIELD_SET_LEVEL_FULL
				],
				contentType : format,
				requestContentType : URLENC
				)
		then: "order is properly placed"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_CREATED
			isNotEmpty(data.created)
			data.guestCustomer == false
		}
		where:
		format << [JSON]
	}

	def "Guest places oder using extended SOP flow : #format"(){
		given: "a guest customer with cart ready for ordering"
		authorizeClient(restClient)
		def userGuid = getGuestUid()
		def anonymous = ['id': 'anonymous']
		def cart = createAnonymousCart(restClient, format, getBasePathWithIntegrationSite())
		addProductToCartOnline(restClient, anonymous, cart.guid, PRODUCT_POWER_SHOT_A480, 1, format, getBasePathWithIntegrationSite())
		addEmailToAnonymousCart(restClient, cart.guid, userGuid, format, getBasePathWithIntegrationSite()) //setting email address on cart makes it recognized as guest cart
		setAddressForAnonymousCart(restClient, GOOD_ADDRESS_DE, cart.guid, format , getBasePathWithIntegrationSite())
		setDeliveryModeForCart(restClient, anonymous, cart.guid, DELIVERY_STANDARD_NET, format, getBasePathWithIntegrationSite())

		def sopRequestDetails = getSopRequestDetails(restClient,anonymous,cart.guid, format, true,getBasePathWithIntegrationSite())

		when:"customer send create subscription request to payment provider"
		RESTClient paymentProviderClient = createRestClient(sopRequestDetails.postUrl)
		def paymentProviderResponse = paymentProviderClient.post(body:createSopRequestParameters(sopRequestDetails.parameters.entry),requestContentType : URLENC)
		assert paymentProviderResponse.status == SC_OK

		and: "check response from payment provider in occ"
		HttpResponseDecorator paymentInfoResponse = waitForPaymentProviderResponse(anonymous.id,cart.guid,format);
		assert paymentInfoResponse.status == SC_OK

		and:"place order"
		HttpResponseDecorator response = restClient.post(
				path : getBasePathWithIntegrationSite() + '/users/'+ anonymous.id +'/orders',
				query : ['cartId' : cart.guid,
					'securityCode' : '123',
					'fields' : FIELD_SET_LEVEL_FULL
				],
				contentType : format,
				requestContentType : URLENC
				)
		then: "order is properly placed"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_CREATED
			isNotEmpty(data.created)
			data.guestCustomer == true
		}
		where:
		format << [JSON]
	}

	def "Customer places oder using extended SOP flow with error handling"(){
		given: "a customer with cart ready for ordering"
		def val = createCustomerWithCartForIntegrationSite(format);
		def customer = val[0]
		def cart = val[1]
		def sopRequestDetails = getSopRequestDetails(restClient,customer,cart.code, format,true, getBasePathWithIntegrationSite())

		when:"customer send incorrect request to payment provider"
		RESTClient paymentProviderClient = createRestClient(sopRequestDetails.postUrl)
		def paymentProviderResponse = paymentProviderClient.post(body:createSopRequestParameters(sopRequestDetails.parameters.entry,false),requestContentType : URLENC)
		assert paymentProviderResponse.status == SC_OK

		and: "check response from payment provider in occ and get error information"
		HttpResponseDecorator paymentInfoResponse = waitForPaymentProviderResponse(customer.id,cart.code,format);
		assert paymentInfoResponse.status == SC_BAD_REQUEST

		and:"clear payment info response in occ"
		HttpResponseDecorator response = restClient.delete(path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/carts/'+cart.code+"/payment/sop/response");
		assert response.status == SC_OK

		and:"send create subscription request again"
		paymentProviderResponse = paymentProviderClient.post(body:createSopRequestParameters(sopRequestDetails.parameters.entry),requestContentType : URLENC)
		assert paymentProviderResponse.status == SC_OK

		and: "check response from payment provider in occ"
		paymentInfoResponse = waitForPaymentProviderResponse(customer.id,cart.code,format);
		assert paymentInfoResponse.status == SC_OK

		and:"place order"
		response = restClient.post(
				path : getBasePathWithIntegrationSite() + '/users/'+ customer.id +'/orders',
				query : ['cartId' : cart.code,
					'securityCode' : '123',
					'fields' : FIELD_SET_LEVEL_FULL
				],
				contentType : format,
				requestContentType : URLENC
				)
		then: "order is properly placed"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_CREATED
			isNotEmpty(data.created)
			data.guestCustomer == false
		}

		where:
		format << [JSON]
	}
}
