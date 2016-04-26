package yscalapackage.facade

import de.hybris.platform.catalog.model.CatalogVersionModel

trait CatalogVersionSearch extends CommerceServices {

  def findCatalogVersion(catalog: String, version: String): CatalogVersionModel = {
    catalogVersionService.getCatalogVersion(catalog, version)
  }
}
