package yscalapackage

import javax.annotation.Resource

import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.catalog.model.{CatalogModel, CatalogVersionModel}
import de.hybris.platform.servicelayer.model.ModelService

trait CommerceFixtures {

  @Resource
  var catalogVersionService: CatalogVersionService = _
  @Resource
  var modelService: ModelService = _

  def createCatalogVersion(catalogId: String, version: String): CatalogVersionModel = {
    val catalog: CatalogModel = modelService.create(classOf[CatalogModel])
    catalog.setId(catalogId)
    modelService.save(catalog)

    val ctgVersion: CatalogVersionModel = modelService.create(classOf[CatalogVersionModel])
    ctgVersion.setCatalog(catalog)
    ctgVersion.setVersion(version)
    ctgVersion.setActive(true)
    modelService.save(ctgVersion)

    ctgVersion
  }
}
