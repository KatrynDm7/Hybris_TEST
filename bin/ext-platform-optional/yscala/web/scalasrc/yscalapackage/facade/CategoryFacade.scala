package yscalapackage.facade

import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.category.model.CategoryModel
import org.springframework.stereotype.Service
import yscalapackage.dto.CategoryDTO

@Service
class CategoryFacade extends CatalogVersionSearch with CategorySearch with CoreServices {


  def createCategory(categoryDTO: CategoryDTO): Unit = {
    val ctgVersion: CatalogVersionModel = findCatalogVersion(categoryDTO.catalog, categoryDTO.version)

    val category: CategoryModel = modelService.create(classOf[CategoryModel])
    category.setCode(categoryDTO.code)
    category.setName(categoryDTO.name)
    category.setCatalogVersion(ctgVersion)

    modelService.save(category)
  }

  def deleteCategory(catalog: String, version: String, code: String): Unit = {
    val ctgVersion: CatalogVersionModel = findCatalogVersion(catalog, version)
    val category: CategoryModel = findCategory(code, ctgVersion)

    modelService.remove(category)
  }

}
