package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.addons.v2.spock

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.carts.AbstractCartTest
import spock.lang.Unroll

import static groovyx.net.http.ContentType.*
import static org.apache.http.HttpStatus.SC_OK

/**
 *  Tests for extended cart controller from acceleratorwebservicesaddon
 */
@Unroll
@ManualTest
class ExtendedCartV2Tests extends AbstractCartTest {
	static final PASSWORD = "test"
	static final STORE_NAME = "WS-Shinbashi"
	static final ANOTHER_STORE_NAME = "WS-Tokio Hotel Metropolitan Tokyo"

	def "Customer gets a consolidated pickup locations: #format"() {
		given: "a registerd and logged in customer with cart"
		def val = createAndAuthorizeCustomerWithCart(restClient, format)
		def customer = val[0]
		def cart = val[1]

		when: "customer adds an item which is out of stock in the online store"
		restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entries',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: [
						'code'       : '2006139',
						'qty'        : 1,
						'pickupStore': STORE_NAME
				],
				contentType: format,
				requestContentType: URLENC
		)

		and: "he adds a pickup item to the cart"
		restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entries',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: [
						'code'       : '1934793',
						'qty'        : 2,
						'pickupStore': ANOTHER_STORE_NAME
				],
				contentType: format,
				requestContentType: URLENC
		)

		and: "he tries to get a consolidated options"
		def response = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/consolidate',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
		)

		then: "he should be able see two consolidated options"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.pointOfServices.size() == 2
			data.pointOfServices[0].name == STORE_NAME
			data.pointOfServices[1].name == ANOTHER_STORE_NAME
		}

		where:
		format << [JSON, XML]
	}

	def "Customer consolidates pickup locations: #format"() {
		given: "a registerd and logged in customer with cart"
		def val = createAndAuthorizeCustomerWithCart(restClient, format)
		def customer = val[0]
		def cart = val[1]

		when: "customer adds an item which is out of stock in the online store"
		restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entries',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: [
						'code'       : '2006139',
						'qty'        : 1,
						'pickupStore': STORE_NAME
				],
				contentType: format,
				requestContentType: URLENC
		)

		and: "he adds a pickup item to the cart"
		restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/entries',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				body: [
						'code'       : '1934793',
						'qty'        : 2,
						'pickupStore': ANOTHER_STORE_NAME
				],
				contentType: format,
				requestContentType: URLENC
		)

		and: "he tries to consolidate locations"
		with(restClient.post(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code + '/consolidate',
				query: [
						'fields'   : FIELD_SET_LEVEL_FULL,
						'storeName': STORE_NAME
				],
				contentType: format,
				requestContentType: URLENC
		)) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
		}

		and: "gets a cart"
		def response = restClient.get(
				path: getBasePathWithSite() + '/users/' + customer.id + '/carts/' + cart.code,
				contentType: format,
				query: [
						'fields': FIELD_SET_LEVEL_FULL],
				requestContentType: URLENC
		)

		then: "the cart is modified and locations are consolidated"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			data.totalItems == 2
			data.totalUnitCount == 3
			data.entries[0].product.availableForPickup == true
			data.entries[0].deliveryPointOfService.name == STORE_NAME
			data.entries[1].product.availableForPickup == true
			data.entries[1].deliveryPointOfService.name == STORE_NAME
			data.pickupOrderGroups[0].entries[0].product.availableForPickup == true
			data.pickupOrderGroups[0].entries[0].deliveryPointOfService.name == STORE_NAME
			data.pickupOrderGroups[0].entries[1].product.availableForPickup == true
			data.pickupOrderGroups[0].entries[1].deliveryPointOfService.name == STORE_NAME
			data.pickupOrderGroups[0].deliveryPointOfService.name == STORE_NAME
		}

		where:
		format << [JSON, XML]
	}
}
