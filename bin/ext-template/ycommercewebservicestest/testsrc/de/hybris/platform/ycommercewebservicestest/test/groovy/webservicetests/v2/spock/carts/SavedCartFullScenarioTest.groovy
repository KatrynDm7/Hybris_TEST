/**
 *
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.carts
import static groovyx.net.http.ContentType.*
import static org.apache.http.HttpStatus.*

import de.hybris.bootstrap.annotations.ManualTest

import spock.lang.Unroll

/**
 * Test cases for the save cart functionality
 */
@Unroll
@ManualTest
class SavedCartFullScenarioTest extends AbstractCartTest {

	def "Registered and authorized customer saves a cart and retrieve it for update : #format"(){
		given: "a registered customer with one cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)

		when: "authorized customer wants to save a cart and retrieve it for update"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		def retrievedCartResponse = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/savedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		def saveCartName = 'new name'
		def saveCartDescription = 'new description'
		def updateCartResponse = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL, 'saveCartName' : saveCartName, 'saveCartDescription' : saveCartDescription],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he saves the cart and retrieves the saved cart"
		with(updateCartResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code == cart.code
			data.savedCartData.name == saveCartName
			data.savedCartData.description == saveCartDescription
			data.savedCartData.savedBy != null
			data.savedCartData.saveTime != null
			data.savedCartData.expirationTime != null
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer saves multiple carts and then flags one of them for deletion: #format"(){
		given: "a registered customer with two carts"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def cart2 = createCart( restClient, customer, format)

		when: "authorized customer wants to save multiple carts cart and then flags one of them for deletion"
		def cart1Response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		def cart2Response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart2.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		def cartsListResponse = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts',
				query : ['savedCartsOnly' : true, 'fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		def flagForDeletionResponse = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/flagForDeletion',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)


		def cartsListResponse2 = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts',
				query : ['savedCartsOnly' : true, 'fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he flags a cart from the 2 carts for deletion and only 1 saved cart remains"
		with(flagForDeletionResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code == cart.code
			isEmpty(data.savedCartData.name)
			isEmpty(data.savedCartData.description)
			isEmpty(data.savedCartData.savedBy)
			isEmpty(data.savedCartData.saveTime)
			isEmpty(data.savedCartData.expirationTime)
		}
		with(cartsListResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.carts)
			data.carts.size() == 2
		}
		with(cartsListResponse2){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.carts)
			data.carts.size() == 1
			def resultCart = data.carts[0]
			resultCart.code == cart2Response.data.savedCartData.code
			resultCart.name == cart2Response.data.savedCartData.name
			resultCart.description == cart2Response.data.savedCartData.description
			resultCart.savedBy == cart2Response.data.savedCartData.savedBy
			resultCart.saveTime == cart2Response.data.savedCartData.saveTime
			resultCart.expirationTime == cart2Response.data.savedCartData.expirationTime
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer saves a cart, update it and then restore it: #format"(){
		given: "a registered customer with one cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)


		when: "authorized customer wants to save a cart, update it and then restore it"
		def cartResponse = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		def saveCartName = 'new name'
		def saveCartDescription = 'new description'
		def updateCartResponse = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL, 'saveCartName' : saveCartName, 'saveCartDescription' : saveCartDescription],
				contentType: format,
				requestContentType: URLENC
				)

		def restorationResponse = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/restoresavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he updates the cart and restore it"
		with(updateCartResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			data.savedCartData.code == cart.code
			data.savedCartData.name == saveCartName
			data.savedCartData.description == saveCartDescription
			data.savedCartData.savedBy != null
			data.savedCartData.saveTime != null
			data.savedCartData.expirationTime != null
		}

		with(restorationResponse){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			data.savedCartData.code == cart.code
			data.savedCartData.name == saveCartName
			data.savedCartData.description == saveCartDescription
			data.savedCartData.savedBy != null
			data.savedCartData.saveTime != null
			data.savedCartData.expirationTime != null
		}

		where:
		format <<[XML, JSON]
	}
}

