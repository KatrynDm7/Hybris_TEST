package yscalapackage

import javax.annotation.Resource

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import org.fest.assertions.Assertions.assertThat
import org.junit.{Before, Test}
import yscalapackage.dto.CategoryDTO
import yscalapackage.facade.CategoryFacade

@IntegrationTest
class CategoryFacadeTest extends ServicelayerTransactionalBaseTest with CommerceFixtures {

   @Resource
   var categoryFacade: CategoryFacade = _
   var catalogVersion: CatalogVersionModel = _

   @Before
   def setUp(): Unit = {
     catalogVersion = createCatalogVersion("Test", "Staged")
   }

   @Test
   def shouldCreateCategory(): Unit = {
     // given
     val categoryDTO: CategoryDTO = prepareCategoryDTO("MyCategory")

     // when
     categoryFacade.createCategory(categoryDTO)
     val category: Option[CategoryModel] = tryFindCategoryForCode("MyCategory")

     // then
     assertThat(category.isDefined).isTrue()

   }

   @Test
   def shouldDeleteExistingCategory(): Unit = {
     // given
     val categoryDTO: CategoryDTO = prepareCategoryDTO("MyCategory")
     categoryFacade.createCategory(categoryDTO)

     // when
     categoryFacade.deleteCategory("Test", "Staged", "MyCategory")
     val category: Option[CategoryModel] = tryFindCategoryForCode("MyCategory")

     // then
     assertThat(category.isDefined).isFalse()
   }

   private def prepareCategoryDTO(code: String): CategoryDTO = {
     val categoryDTO: CategoryDTO = new CategoryDTO()
     categoryDTO.catalog = "Test"
     categoryDTO.version = "Staged"
     categoryDTO.code = code

     categoryDTO
   }

   private def tryFindCategoryForCode(code: String): Option[CategoryModel] = {
     try {
       Some(categoryFacade.getCategoryService.getCategoryForCode(catalogVersion, "MyCategory"))
     } catch {
       case e: UnknownIdentifierException => None
     }
   }
 }
