package yscalapackage

import javax.annotation.Resource

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import org.fest.assertions.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.{Before, Test}
import yscalapackage.dto.{ProductDTO, ProductsDTO}
import yscalapackage.facade.ProductFacade

@IntegrationTest
class ProductFacadeTest extends ServicelayerTransactionalBaseTest with CommerceFixtures {

  @Resource
  var productFacade: ProductFacade = _

  @Before
  def setUp(): Unit = {
    createCatalogVersion("Test", "Staged")
  }

  @Test
  def shouldFindAllProductsFromCatalogVersion(): Unit = {
    // given
    productFacade.createProduct(prepareProductDTO("MyProduct1"))
    productFacade.createProduct(prepareProductDTO("MyProduct2"))
    productFacade.createProduct(prepareProductDTO("MyProduct3"))

    // when
    val productsDTO: ProductsDTO = productFacade.getProductsForCatalogVersion("Test", "Staged")

    // then
    assertThat(productsDTO.getProducts).hasSize(3)
  }

  @Test
  def shouldDeleteExistingProduct(): Unit = {
    // given
    productFacade.createProduct(prepareProductDTO("MyProduct"))

    // when
    productFacade.deleteProduct("Test", "Staged", "MyProduct")

    // then
    try {
      productFacade.getProduct("Test", "Staged", "MyProduct")
    } catch {
      case e: UnknownIdentifierException => // OK
      case e: Exception => fail("Should throw Unknown Identifier Exception")
    }
  }

  @Test
  def shouldCreateAndFindProductInCatalogVersion(): Unit = {
    // given
    val dto: ProductDTO = prepareProductDTO("MyProduct")

    // when
    productFacade.createProduct(dto)
    val foundProduct: ProductDTO = productFacade.getProduct("Test", "Staged", "MyProduct")

    // then
    assertThat(foundProduct.getCode).isEqualTo("MyProduct")
    assertThat(foundProduct.getName).isEqualTo("MyName")
    assertThat(foundProduct.getEan).isEqualTo("MyEan")
    assertThat(foundProduct.getCatalog).isEqualTo("Test")
    assertThat(foundProduct.getVersion).isEqualTo("Staged")
  }

  @Test
  def shouldUpdateExistingProduct(): Unit = {
    // given
    val dto: ProductDTO = prepareProductDTO("MyProduct")
    productFacade.createProduct(dto)
    val updated: ProductDTO = new ProductDTO()
    updated.setEan("NewEAN")
    updated.setName("NewName")

    // when
    productFacade.updateProduct("Test", "Staged", "MyProduct", updated)
    val updatedDTO: ProductDTO = productFacade.getProduct("Test", "Staged", "MyProduct")

    // then
    assertThat(updatedDTO.getCode).isEqualTo("MyProduct")
    assertThat(updatedDTO.getEan).isEqualTo("NewEAN")
    assertThat(updatedDTO.getName).isEqualTo("NewName")
  }

  def prepareProductDTO(code: String): ProductDTO = {
    val dto: ProductDTO = new ProductDTO()
    dto.code = code
    dto.name = "MyName"
    dto.ean = "MyEan"
    dto.catalog = "Test"
    dto.version = "Staged"

    dto
  }

}
