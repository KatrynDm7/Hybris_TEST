package ygroovypackage.controller

import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.product.ProductService
import de.hybris.platform.servicelayer.model.ModelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import ygroovypackage.dto.ProductDTO
import ygroovypackage.dto.ProductsDTO
import ygroovypackage.facade.ProductFacade

@Controller
@RequestMapping(value="products")
class ProductController
{
    @Autowired
    private ProductFacade productFacade
    @Autowired
    private ModelService modelService
    @Autowired
    private CatalogVersionService catalogVersionService
    @Autowired
    private ProductService productService

    @RequestMapping(method=org.springframework.web.bind.annotation.RequestMethod.POST, consumes="application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestBody ProductDTO productDTO)
    {
        productFacade.createProduct(productDTO)
    }

    @RequestMapping(value="{catalog}/{version}/{productCode}", method=org.springframework.web.bind.annotation.RequestMethod.PUT, consumes="application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    void update(@PathVariable String catalog, @PathVariable String version, @PathVariable String productCode, @RequestBody ProductDTO productDTO)
    {
        productFacade.updateProduct(catalog, version, productCode, productDTO)
    }

    @RequestMapping(value="{catalog}/{version}/{productCode}", method=org.springframework.web.bind.annotation.RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    void delete(@PathVariable String catalog, @PathVariable String version, @PathVariable String productCode)
    {
        productFacade.deleteProduct(catalog, version, productCode)
    }

    @RequestMapping(value="{catalog}/{version}/{productCode}", method=org.springframework.web.bind.annotation.RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ProductDTO show(@PathVariable String catalog, @PathVariable String version, @PathVariable String productCode)
    {
        productFacade.getProduct(catalog, version, productCode)
    }

    @RequestMapping(value="{catalog}/{version}", method=org.springframework.web.bind.annotation.RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ProductsDTO all(@PathVariable String catalog, @PathVariable String version)
    {
        productFacade.getProductsForCatalogVersion(catalog, version)
    }
}