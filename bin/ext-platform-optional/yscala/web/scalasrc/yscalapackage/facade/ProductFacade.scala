package yscalapackage.facade

import java.util

import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.servicelayer.search.{FlexibleSearchQuery, SearchResult}
import org.springframework.stereotype.Service
import yscalapackage.dto.{ProductDTO, ProductsDTO}

/**
 * Sample facade for simple Product manipulation.
 */
@Service
class ProductFacade extends CatalogVersionSearch with CategorySearch with CoreServices {

  import scala.collection.JavaConversions._

  /**
   * Returns all Products DTOs for give CatalogVersion.
   */
  def getProductsForCatalogVersion(catalog: String, version: String): ProductsDTO = {
    val catalogVersion: CatalogVersionModel = findCatalogVersion(catalog, version)

    val query = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {CatalogVersion}=?catalogVersion ORDER BY {code}")
    query.addQueryParameter("catalogVersion", catalogVersion)

    val searchResult: SearchResult[ProductModel] = flexibleSearchService.search(query)
    val productsDTO: ProductsDTO = new ProductsDTO()
    productsDTO.products = searchResult.getResult.map(convertProdtucToDTO)
    return productsDTO
  }

  /**
   * Returns Product DTO.
   *
   */
  def getProduct(catalog: String, version: String, code: String): ProductDTO = {
    val catalogVersion: CatalogVersionModel = findCatalogVersion(catalog, version)
    val product: ProductModel = productService.getProductForCode(catalogVersion, code)

    convertProdtucToDTO(product)
  }

  /**
   * Deletes product stored in CatalogVersion identified by code.
   *
   */
  def deleteProduct(catalog: String, version: String, code: String): Unit = {
    val catalogVersion: CatalogVersionModel = findCatalogVersion(catalog, version)
    val product: ProductModel = productService.getProductForCode(catalogVersion, code)

    modelService.remove(product)
  }

  /**
   * Updates Product with data provided by ProductDTO object.
   *
   */
  def updateProduct(catalog: String, version: String, code: String, productDTO: ProductDTO): Unit = {
    val catalogVersion: CatalogVersionModel = findCatalogVersion(catalog, version)
    val product: ProductModel = productService.getProductForCode(catalogVersion, code)

    if (productDTO.eanOption().isDefined) product.setEan(productDTO.eanOption().get)
    if (productDTO.nameOption().isDefined) product.setName(productDTO.nameOption().get)

    applyCategories(productDTO, catalogVersion, product)

    modelService.save(product)
  }

  /**
   * Creates Product with data provided by ProductDTO object.
   */
  def createProduct(productDTO: ProductDTO): ProductDTO = {
    val ctgVersion: CatalogVersionModel = findCatalogVersion(productDTO.catalog, productDTO.version)

    val product: ProductModel = modelService.create(classOf[ProductModel])
    product.setCatalogVersion(ctgVersion)
    product.setCode(productDTO.code)
    if (productDTO.eanOption().isDefined) product.setEan(productDTO.eanOption().get)
    if (productDTO.nameOption().isDefined) product.setName(productDTO.nameOption().get)
    modelService.save(product)

    applyCategories(productDTO, ctgVersion, product)

    convertProdtucToDTO(product)
  }

  private def applyCategories(productDTO: ProductDTO, ctgVersion: CatalogVersionModel, product: ProductModel) {
    productDTO.categories.foreach(categoryCode => {
      val category: CategoryModel = findCategory(categoryCode, ctgVersion)
      category.setProducts(List.concat(category.getProducts, List(product)))
      modelService.save(category)
    })
  }

  private def convertProdtucToDTO(product: ProductModel): ProductDTO = {
    val dto = new ProductDTO()
    dto.code = product.getCode
    dto.name = product.getName
    dto.ean = product.getEan
    dto.catalog = product.getCatalogVersion.getCatalog.getId
    dto.version = product.getCatalogVersion.getVersion
    dto.categories = new util.ArrayList(product.getSupercategories.map(ctg => ctg.getCode))

    dto
  }

}
