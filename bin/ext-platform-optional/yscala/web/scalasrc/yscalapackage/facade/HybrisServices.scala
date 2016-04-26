package yscalapackage.facade

import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.category.CategoryService
import de.hybris.platform.product.ProductService
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.springframework.beans.factory.annotation.Autowired

import scala.beans.BeanProperty

trait CoreServices {
  @BeanProperty
  @Autowired
  var flexibleSearchService: FlexibleSearchService = _

  @BeanProperty
  @Autowired
  var modelService: ModelService = _
}

trait CommerceServices {
  @BeanProperty
  @Autowired
  var productService: ProductService = _

  @BeanProperty
  @Autowired
  var categoryService: CategoryService = _

  @BeanProperty
  @Autowired
  var catalogVersionService: CatalogVersionService = _
}
