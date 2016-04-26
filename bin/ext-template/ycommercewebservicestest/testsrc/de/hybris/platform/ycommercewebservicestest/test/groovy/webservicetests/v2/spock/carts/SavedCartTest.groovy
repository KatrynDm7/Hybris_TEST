/**
 *
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.carts
import static groovyx.net.http.ContentType.*
import static org.apache.http.HttpStatus.*

import de.hybris.bootstrap.annotations.ManualTest

import spock.lang.Unroll
import groovyx.net.http.HttpResponseDecorator

/**
 * Test cases for the save cart functionality
 */
@Unroll
@ManualTest
class SavedCartTest extends AbstractCartTest {

	def "Registered and authorized customer saves a cart : #format"(){
		given: "a registered customer with one cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)

		when: "authorized customer wants to save a cart"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he saves the cart and retrieves the saved cart"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code == cart.code
			data.savedCartData.name == cart.code
			data.savedCartData.description == "-"
			data.savedCartData.savedBy != null
			data.savedCartData.saveTime != null
			data.savedCartData.expirationTime != null
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer saves a cart with full details : #format"(){
		given: "a registered customer with one cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def saveCartName = "saveCartName"
		def saveCartDescription = "saveCartDescription"

		when: "authorized customer wants to save a cart"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL,
					'saveCartName': saveCartName,
					'saveCartDescription':saveCartDescription],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he saves the cart and retrieves the saved cart"
		with(response){
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

	def "Registered and authorized customer saves a cart with wrong id : #format"(){
		given: "a registered customer with one cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def saveCartName = "saveCartName"
		def saveCartDescription = "saveCartDescription"

		when: "authorized customer wants to save a cart"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/12345678/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL,
					'saveCartName': saveCartName,
					'saveCartDescription':saveCartDescription],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he saves the cart and retrieves the saved cart"
		with(response){
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].type == 'CartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer updates a cart : #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to update the cart setting new name and description"
		def saveCartName = 'new name'
		def saveCartDescription = 'new description'
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL, 'saveCartName' : saveCartName, 'saveCartDescription' : saveCartDescription],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he updates the cart and retrieves it"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code == cart.code
			data.savedCartData.name == saveCartName
			data.savedCartData.description == saveCartDescription
			data.savedCartData.savedBy == savedCart.savedCartData.savedBy
			data.savedCartData.saveTime != null
			data.savedCartData.expirationTime != null
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer updates a cart not changing its name and description: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)
		sleep 2000

		when: "authorized customer wants to update the cart not specifying new name and description"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he updates the cart and retrieves it"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code == cart.code
			data.savedCartData.name == savedCart.savedCartData.name
			data.savedCartData.description == savedCart.savedCartData.description
			data.savedCartData.savedBy == savedCart.savedCartData.savedBy
			if (format == XML){
				data.savedCartData.saveTime.text() > savedCart.savedCartData.saveTime.text()
				data.savedCartData.expirationTime.text() > savedCart.savedCartData.expirationTime.text()
			}
			if (format == JSON){
				data.savedCartData.saveTime > savedCart.savedCartData.saveTime
				data.savedCartData.expirationTime > savedCart.savedCartData.expirationTime
			}
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer updates a cart by wrong id: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to update the cart not specifying new name and description"
		def invalidCartId = "invalid_cart_id"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+ invalidCartId + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he gets Bad Request error in response"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_BAD_REQUEST
			isEmpty(data.savedCartData)
			data.errors[0].subject == invalidCartId
			data.errors[0].type == 'CartError'
			data.errors[0].reason == 'notFound'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer updates cart name only : #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to update the cart setting new name"
		def saveCartName = 'new name'
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL, 'saveCartName' : saveCartName],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he updates the cart and retrieves it"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code == cart.code
			data.savedCartData.name == saveCartName
			data.savedCartData.description == "-"
			data.savedCartData.savedBy == savedCart.savedCartData.savedBy
			data.savedCartData.saveTime != null
			data.savedCartData.expirationTime != null
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer updates cart description only : #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to update the cart setting new description"
		def saveCartDescription = 'new description'
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL, 'saveCartDescription' : saveCartDescription],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he updates the cart and retrieves it"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code == cart.code
			data.savedCartData.name == cart.code
			data.savedCartData.description == saveCartDescription
			data.savedCartData.savedBy == savedCart.savedCartData.savedBy
			data.savedCartData.saveTime != null
			data.savedCartData.expirationTime != null
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer updates nothing on cart: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)

		when: "authorized customer wants to save cart and then update the cart without setting anything new"
		def saveCartDescription = 'new description'
		def saveCartName = 'new name'
		def saveResponse = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL, 'saveCartDescription' : saveCartDescription, 'saveCartName' : saveCartName],
				contentType: format,
				requestContentType: URLENC
				)

		def updateResponse = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he updates the cart and retrieves it with unmodified values"
		with(updateResponse){
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

	def "Anonymous user saves a cart with full details providing guid: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to save anonymous carts"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.guid+'/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CommerceSaveCartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous user saves a cart with full details providing code: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to save anonymous carts"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.code+'/save',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer saves a cart and then restores it : #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)
		def cart2 = createCart( restClient, customer, format) //just for loading another cart into session cart

		when: "authorized customer wants to restore the saved cart"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/restoresavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		def sessionCart = getCart(restClient, customer, 'current', format)

		then: "he restores the cart and the cart becomes a session one"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			response.data.savedCartData.code == sessionCart.code
			response.data.savedCartData.code == cart.code
			response.data.savedCartData.name == cart.code
			response.data.savedCartData.description == "-"
			response.data.savedCartData.savedBy != null
			response.data.savedCartData.saveTime != null
			response.data.savedCartData.expirationTime != null
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer saves a cart and tries to restores invalid cart: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)
		def cart2 = createCart( restClient, customer, format) //just for loading another cart into session cart

		when: "authorized customer wants to restore the saved cart"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/12345678/restoresavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		def sessionCart = getCart(restClient, customer, 'current', format)

		then: "he gets cart not found exception"
		with(response){
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].type == 'CartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous user restores a cart providing guid: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to save anonymous carts"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.guid+'/restoresavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CommerceSaveCartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous user restores a cart providing code: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to restore anonymous carts"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.code+'/restoresavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer saves a cart and then flags it for deletion: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to flag the cart for deletion"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/flagForDeletion',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he retrieves the saved cart with empty specific attributes"
		with(response){
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

		where:
		format <<[XML, JSON]
	}

	def "Customer flags another customer's cart for deletion: #format"(){
		given: "a customer with cart"
		authorizeTrustedClient(restClient)
		def customer1 = registerCustomer(restClient)
		def cart = createCart(restClient, customer1, format)
		def savedCart = saveCart(restClient, customer1, cart, format)

		and: "another, authorized customer"
		def customer2 = registerCustomer(restClient)
		authorizeCustomer(restClient, customer2)

		when: "authorized customer attempts to flag other customer's cart for deletion"
		HttpResponseDecorator response = restClient.post(
				path : getBasePathWithSite() + '/users/'+ customer2.id +'/carts/'+savedCart.code + '/flagForDeletion',
				contentType: format,
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC
				)

		then: "this attempt fails"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CartError'
		}

		where:
		format << [XML, JSON]
	}

	def "Registered and authorized customer saves a cart and then tries to flag cart for deletion by wrong id : #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to flag cart for deletion by wrong identifier"
		def invalidCartId = "invalid_cart_id"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+invalidCartId + '/flagForDeletion',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he gets Bad Request error in response"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_BAD_REQUEST
			isEmpty(data.savedCartData)
			data.errors[0].subject == invalidCartId
			data.errors[0].type == 'CartError'
			data.errors[0].reason == 'notFound'
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous user flags a cart for deletion providing guid: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to flag anonymous cart for deletion"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.guid+'/flagForDeletion',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CommerceSaveCartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous user flags a cart for deletion providing code: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to flag anonymous cart for deletion"
		def response = restClient.patch(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.code+'/flagForDeletion',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer gets a saved cart : #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to get a saved cart"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+cart.code + '/savedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he retrieves the saved cart"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code == savedCart.savedCartData.code
			data.savedCartData.name == savedCart.savedCartData.name
			data.savedCartData.description == savedCart.savedCartData.description
			data.savedCartData.savedBy == savedCart.savedCartData.savedBy
			data.savedCartData.saveTime == savedCart.savedCartData.saveTime
			data.savedCartData.expirationTime == savedCart.savedCartData.expirationTime
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer trying to get a saved cart by invalid id: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to get a saved cart by invalid id"
		def invalidCartId = 'invalid_cart_id'
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+ invalidCartId + '/savedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he gets error in response"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_BAD_REQUEST
			isEmpty(data.savedCartData)
			data.errors[0].subject == invalidCartId
			data.errors[0].type == 'CartError'
			data.errors[0].reason == 'notFound'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer trying to get a saved cart providing no cart id: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to get a saved cart providing no cart id"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/savedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he gets error in response"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_BAD_REQUEST
			isEmpty(data.savedCartData)
			data.errors[0].subject == 'savedcart'
			data.errors[0].type == 'CartError'
			data.errors[0].reason == 'notFound'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer trying to get a saved cart providing invalid user id: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to get a saved cart providing no cart id"
		def invalidUserId = 'invalid_user_id'
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ invalidUserId +'/carts/savedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he gets error in response"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_FORBIDDEN
			isEmpty(data.savedCartData)
			data.errors[0].type == 'ForbiddenError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer trying to get existing cart but not saved one: #format"(){
		given: "a registered customer with one cart created"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)

		when: "authorized customer wants to get the cart by it id"
		def invalidCartId = 'invalid_cart_id'
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+ cart.id + '/savedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he gets error in response"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_BAD_REQUEST
			isEmpty(data.savedCartData)
			data.errors[0].subjectType == 'cart'
			data.errors[0].type == 'CartError'
			data.errors[0].reason == 'notFound'
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous user gets a cart by guid: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to save anonymous carts"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.guid+'/savedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CommerceSaveCartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous user gets a cart by code: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to restore anonymous carts"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.code+'/savedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer gets list of saved carts with only 1 saved cart : #format"(){
		given: "a registered customer with one saved cart and one not saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)
		def cart2 = createCart(restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to get a list of all the saved carts"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts',
				query : ['savedCartsOnly' : true, 'fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he retrieves only the saved cart in the list"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.carts)
			data.carts.size() == 1
			def resultCart = data.carts[0]
			resultCart.code == savedCart.savedCartData.code
			resultCart.name == savedCart.savedCartData.name
			resultCart.description == savedCart.savedCartData.description
			resultCart.savedBy == savedCart.savedCartData.savedBy
			resultCart.saveTime == savedCart.savedCartData.saveTime
			resultCart.expirationTime == savedCart.savedCartData.expirationTime
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer gets list of saved carts : #format"(){
		given: "a registered customer with two saved carts"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)
		def cart2 = createCart(restClient, customer, format)
		def savedCart2 = saveCart(restClient, customer, cart2, format)

		when: "authorized customer wants to get a list of all the saved carts"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts',
				query : ['savedCartsOnly' : true, 'fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he retrieves all saved carts"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.carts)
			data.carts.size() == 2
			for (def resultCart : response.data.carts){
				resultCart.code == savedCart.savedCartData.code || resultCart.code == savedCart2.savedCartData.code
				resultCart.name == savedCart.savedCartData.name || resultCart.name == savedCart2.savedCartData.name
				resultCart.description == savedCart.savedCartData.description || resultCart.description == savedCart2.savedCartData.description
				resultCart.savedBy == savedCart.savedCartData.savedBy || resultCart.savedBy == savedCart2.savedCartData.savedBy
				resultCart.saveTime == savedCart.savedCartData.saveTime || resultCart.saveTime == savedCart2.savedCartData.saveTime
				resultCart.expirationTime == savedCart.savedCartData.expirationTime || resultCart.expirationTime == savedCart2.savedCartData.expirationTime
			}
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer gets first page of list of saved carts : #format"(){
		given: "a registered customer with 4 saved carts"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)
		def cart2 = createCart(restClient, customer, format)
		def savedCart2 = saveCart(restClient, customer, cart2, format)
		def cart3 = createCart(restClient, customer, format)
		def savedCart3 = saveCart(restClient, customer, cart3, format)
		def cart4 = createCart(restClient, customer, format)
		def savedCart4 = saveCart(restClient, customer, cart4, format)

		when: "authorized customer wants to get the first page of the list of saved carts"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts',
				query : ['savedCartsOnly' : true, 'currentPage' : 0, 'pageSize' : 2, 'fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he retrieves 2 saved carts"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.carts)
			data.carts.size() == 2
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer gets second page of list of saved carts : #format"(){
		given: "a registered customer with 4 saved carts"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)
		def cart2 = createCart(restClient, customer, format)
		def savedCart2 = saveCart(restClient, customer, cart2, format)
		def cart3 = createCart(restClient, customer, format)
		def savedCart3 = saveCart(restClient, customer, cart3, format)
		def cart4 = createCart(restClient, customer, format)
		def savedCart4 = saveCart(restClient, customer, cart4, format)

		when: "authorized customer wants to get the second page of the list of saved carts"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts',
				query : ['savedCartsOnly' : true, 'currentPage' : 1, 'pageSize' : 2, 'fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he retrieves 2 saved carts"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.carts)
			data.carts.size() == 2
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer has no saved carts but trying to get a list of saved carts : #format"(){
		given: "a registered customer with no saved cart and one not saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart(restClient, customer, format)

		when: "authorized customer wants to get a list of all the saved carts"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts',
				query : ['savedCartsOnly' : true, 'fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "he retrieves empty list"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isEmpty(data.carts)
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous customer trying to get a list of saved carts : #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)

		when: "anonymous customer (not logged in) wants to retrieve anonymous saved carts"
		def response = restClient.get(
				path : getBasePathWithSite() + '/users/anonymous/carts',
				query : ['savedCartsOnly' : true, 'fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)
		then: "his access is denied"
		with(response){
			status == SC_UNAUTHORIZED
			data.errors[0].type == 'AccessDeniedError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer saves a cart and clones it after: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		when: "authorized customer wants to clone a cart"
		def response = restClient.post(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+savedCart.savedCartData.code + '/clonesavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he gets the cart cloned"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code != savedCart.savedCartData.code
			data.savedCartData.name != savedCart.savedCartData.name
			data.savedCartData.description == savedCart.savedCartData.description
			data.savedCartData.savedBy == savedCart.savedCartData.savedBy
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer saves a cart and clones it with new name and description: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)
		def savedCart = saveCart(restClient, customer, cart, format)

		def name = "clonedSaveCartName"
		def description = "clonedSaveCartDescription"

		when: "authorized customer wants to clone a cart"
		def response = restClient.post(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+savedCart.savedCartData.code + '/clonesavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL,
					'name': name,
					'description':description],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he gets the cart cloned"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_OK
			isNotEmpty(data.savedCartData)
			data.savedCartData.code != savedCart.savedCartData.code
			data.savedCartData.name == name
			data.savedCartData.description == description
			data.savedCartData.savedBy == savedCart.savedCartData.savedBy
		}

		where:
		format <<[XML, JSON]
	}

	def "Registered and authorized customer clones a cart by wrong id: #format"(){
		given: "a registered customer with one saved cart"
		authorizeTrustedClient(restClient)
		def customer = registerCustomer(restClient)
		authorizeCustomer(restClient, customer)
		def cart = createCart( restClient, customer, format)

		when: "authorized customer wants to update the cart not specifying new name and description"
		def invalidCartId = "invalid_cart_id"
		def response = restClient.post(
				path : getBasePathWithSite() + '/users/'+ customer.id +'/carts/'+ invalidCartId + '/clonesavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "he gets Bad Request error in response"
		with(response){
			if(isNotEmpty(data)&&isNotEmpty(data.errors))println(data)
			status == SC_BAD_REQUEST
			isEmpty(data.savedCartData)
			data.errors[0].subject == invalidCartId
			data.errors[0].type == 'CartError'
			data.errors[0].reason == 'notFound'
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous user clones a cart by providing guid: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to save anonymous carts"
		def response = restClient.post(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.guid+'/clonesavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CommerceSaveCartError'
		}

		where:
		format <<[XML, JSON]
	}

	def "Anonymous user clones a cart by providing code: #format"(){
		given: "a trusted client"
		authorizeTrustedClient(restClient)
		def cart = createAnonymousCart(restClient, format)

		when: "anonymous (not logged in) user wants to save anonymous carts"
		def response = restClient.post(
				path : getBasePathWithSite() + '/users/anonymous/carts/'+cart.code+'/clonesavedcart',
				query : ['fields' : FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
				)

		then: "his access id denied but cart not found will be thrown for security reasons"
		with(response){
			status == SC_BAD_REQUEST
			data.errors[0].type == 'CartError'
		}

		where:
		format <<[XML, JSON]
	}
}

