/**
 *
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.export

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.XML
import static org.apache.http.HttpStatus.SC_BAD_REQUEST
import static org.apache.http.HttpStatus.SC_OK

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.AbstractSpockFlowTest

import spock.lang.Unroll
import groovyx.net.http.HttpResponseDecorator

@ManualTest
@Unroll
class ExportTest extends AbstractSpockFlowTest {

	static final NUMBER_OF_ALL_PRODUCTS = 895
    static final PAGE_SIZE = 20

	def "Trusted client exports all products paged: #format"() {
		given: "a trusted client"
		authorizeTrustedClient(restClient)

		when: "client requests an export of all products"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/export/products',
				contentType: format,
                query: ['pageSize': PAGE_SIZE],
				requestContentType: URLENC
		)

		then: "all products are returned"
		with(response) {
			if (isNotEmpty(data.errors)) println data.errors
			status == SC_OK
			isNotEmpty(data.products)
			data.products.size() == PAGE_SIZE
			data.totalProductCount == NUMBER_OF_ALL_PRODUCTS
			data.totalPageCount == (int) Math.ceil(NUMBER_OF_ALL_PRODUCTS / PAGE_SIZE)
			data.currentPage == 0
		}

		where:
		format << [XML, JSON]
	}

	def "Trusted client exports products incrementally from date in past: #format"() {
		given: "a trusted client"
		authorizeTrustedClient(restClient)

		when: "client requests an export of all products"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/export/products',
				contentType: format,
				query: [
                        'pageSize': PAGE_SIZE,
                        'timestamp': '2012-03-28T07:50:49+00:00'
                ],
				requestContentType: URLENC
		)

		then: "all products are returned"
		with(response) {
			if (isNotEmpty(data.errors)) println data.errors
			status == SC_OK
			isNotEmpty(data.products)
			data.products.size() == PAGE_SIZE
			data.totalProductCount == NUMBER_OF_ALL_PRODUCTS
			data.currentPage == 0
			data.totalPageCount == (int) Math.ceil(NUMBER_OF_ALL_PRODUCTS / PAGE_SIZE)
		}

		where:
		format << [XML, JSON]
	}

	def "Trusted client exports products incrementally from date in future: #format"() {
		given: "a trusted client"
		authorizeTrustedClient(restClient)

		when: "client requests an export of all products"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/export/products',
				contentType: format,
				query: [
                        'pageSize': PAGE_SIZE,
                        'timestamp': '2112-03-28T07:50:49+00:00'
                ],
				requestContentType: URLENC
		)

		then: "all products are returned"
		with(response) {
			if (isNotEmpty(data.errors)) println data.errors
			status == SC_OK
			!isNotEmpty(data.products)
			data.totalProductCount == 0
			data.currentPage == 0
			data.totalPageCount == 0
		}

		where:
		format << [XML, JSON]
	}

	def "Trusted client exports products from staged catalog version: #format"() {
		given: "a trusted client"
		authorizeTrustedClient(restClient)

		when: "client requests an export of all products"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/export/products',
				contentType: format,
				query: [
                        'pageSize': PAGE_SIZE,
						'catalog': 'wsTestProductCatalog',
						'version': 'Staged'
				],
				requestContentType: URLENC
		)

		then: "all products are returned"
		with(response) {
			if (isNotEmpty(data.errors)) println data.errors
			status == SC_OK
			!isNotEmpty(data.products)
			data.totalProductCount == 0
			data.currentPage == 0
			data.totalPageCount == 0
		}

		where:
		format << [XML, JSON]
	}

	def "Trusted client exports products from staged catalog version not providing catalog: #format"() {
		given: "a trusted client"
		authorizeTrustedClient(restClient)

		when: "client requests an export of all products"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/export/products',
				contentType: format,
				query: [
						'version': 'Staged'
				],
				requestContentType: URLENC
		)

		then: "all products are returned"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == 'ValidationError'
			data.errors[0].subjectType == 'parameter'
			data.errors[0].subject == 'catalog'
			data.errors[0].message
		}

		where:
		format << [XML, JSON]
	}

	def "Trusted client exports products from catalog without providing version: #format"() {
		given: "a trusted client"
		authorizeTrustedClient(restClient)

		when: "client requests an export of all products"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/export/products',
				contentType: format,
				query: [
						'catalog': 'wsTestProductCatalog'
				],
				requestContentType: URLENC
		)

		then: "all products are returned"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == 'ValidationError'
			data.errors[0].subjectType == 'parameter'
			data.errors[0].subject == 'version'
			data.errors[0].message
		}

		where:
		format << [XML, JSON]
	}


	def "Trusted client exports products from non-existing catalog: #format"() {
		given: "a trusted client"
		authorizeTrustedClient(restClient)

		when: "client requests an export of all products"
		HttpResponseDecorator response = restClient.get(
				path: getBasePathWithSite() + '/export/products',
				contentType: format,
				query: [
						'catalog': 'notExisting',
						'version': 'Online'
				],
				requestContentType: URLENC
		)

		then: "all products are returned"
		with(response) {
			status == SC_BAD_REQUEST
			data.errors[0].type == 'UnknownIdentifierError'
		}

		where:
		format << [XML, JSON]
	}
}
