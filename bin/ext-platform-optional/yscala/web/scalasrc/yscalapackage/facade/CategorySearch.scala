package yscalapackage.facade

import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.category.model.CategoryModel

trait CategorySearch extends CommerceServices {


  def findCategory(code: String, catalogVersion: CatalogVersionModel): CategoryModel = {
    categoryService.getCategoryForCode(catalogVersion, code)
  }

}
