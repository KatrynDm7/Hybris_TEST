package yscalapackage.controller

import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.product.ProductService
import de.hybris.platform.servicelayer.model.ModelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, MediaType}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import yscalapackage.dto.{ProductDTO, ProductsDTO}
import yscalapackage.facade.ProductFacade

import scala.beans.BeanProperty

@Controller
@RequestMapping(value = Array("products"))
class ProductController {

  @BeanProperty
  @Autowired
  var productFacade: ProductFacade = _

  @BeanProperty
  @Autowired
  var modelService: ModelService = _
  @BeanProperty
  @Autowired
  var catalogVersionService: CatalogVersionService = _
  @BeanProperty
  @Autowired
  var productService: ProductService = _

  /**
   * Creates new Product.
   * Request URL: http://server:port/yscala/products
   * Method: POST
   * JSON:
   * {
   * "code": "product_code",
   * "name": "product name",
   * "ean": "product ean",
   * "catalog": "catalog_code",
   * "version": "catalog_version_code",
   * "categories": ["category_code1", "category_code2", ...]
   * }
   */
  @RequestMapping(method = Array(RequestMethod.POST), consumes = Array(MediaType.APPLICATION_JSON_VALUE))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.CREATED)
  def create(@RequestBody productDTO: ProductDTO): Unit = productFacade.createProduct(productDTO)


  /**
   * Updates existing Product. Consumes following json:
   * Request URL: http://server:port/yscala/products/catalogID/catalogVersion/productCode
   * Method: PUT
   * <pre>
   * {
   * "name": "new product name",
   * "ean": "new product ean",
   * "categories": ["category_code1", "category_code2", ...]
   * }
   * </pre>
   */
  @RequestMapping(value = Array("{catalog}/{version}/{productCode}"), method = Array(RequestMethod.PUT), consumes = Array(MediaType.APPLICATION_JSON_VALUE))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  def update(@PathVariable catalog: String, @PathVariable version: String, @PathVariable productCode: String, @RequestBody productDTO: ProductDTO): Unit = {
    productFacade.updateProduct(catalog, version, productCode, productDTO)
  }

  /**
   * Removes existing Product.
   * Request URL: http://server:port/yscala/products/catalogID/catalogVersion/productCode
   * Method: DELETE
   */
  @RequestMapping(value = Array("{catalog}/{version}/{productCode}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  def delete(@PathVariable catalog: String, @PathVariable version: String, @PathVariable productCode: String): Unit = {
    productFacade.deleteProduct(catalog, version, productCode)
  }

  /**
   * Shows existing product.
   * Request URL: http://server:port/yscala/products/catalogID/catalogVersion/productCode
   * Method: GET
   */
  @RequestMapping(value = Array("{catalog}/{version}/{productCode}"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  def show(@PathVariable catalog: String, @PathVariable version: String, @PathVariable productCode: String): ProductDTO = {
    productFacade.getProduct(catalog, version, productCode)
  }

  /**
   * Shows all products from CatalogVersion. This method is just for demo purposes. In real life scenario getting
   * all Products without paging from CatalogVersion is not an good idea due to performance reasons.
   * Request URL: http://server:port/yscala/products/catalogID/catalogVersion
   */
  @RequestMapping(value = Array("{catalog}/{version}"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  def all(@PathVariable catalog: String, @PathVariable version: String): ProductsDTO = {
    productFacade.getProductsForCatalogVersion(catalog, version)
  }

}
