package ygroovypackage.facade

import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.category.CategoryService
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.product.ProductService
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ygroovypackage.dto.CategoryDTO

@Service
class CategoryFacade
{
    @Autowired
    private FlexibleSearchService flexibleSearchService
    @Autowired
    private ModelService modelService
    @Autowired
    private ProductService productService
    @Autowired
    private CategoryService categoryService
    @Autowired
    private CatalogVersionService catalogVersionService

    void createCategory(CategoryDTO categoryDTO)
    {
        CatalogVersionModel ctgVersion = findCatalogVersion(categoryDTO.getCatalog(), categoryDTO.getVersion())

        CategoryModel category = (CategoryModel)modelService.create(CategoryModel.class)
        category.setCode(categoryDTO.getCode())
        category.setName(categoryDTO.getName())
        category.setCatalogVersion(ctgVersion)

        modelService.save(category)
    }

    void deleteCategory(String catalog, String version, String code)
    {
        CatalogVersionModel ctgVersion = findCatalogVersion(catalog, version)
        CategoryModel category = findCategory(code, ctgVersion)

        modelService.remove(category)
    }

    CatalogVersionModel findCatalogVersion(String catalog, String version){
        catalogVersionService.getCatalogVersion(catalog, version)
    }

    CategoryModel findCategory(String code, CatalogVersionModel catalogVersion)
    {
        getCategoryService().getCategoryForCode(catalogVersion, code);
    }

    FlexibleSearchService getFlexibleSearchService()
    {
        flexibleSearchService
    }

    void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService
    }

    ModelService getModelService()
    {
        modelService
    }

    void setModelService(ModelService modelService)
    {
        this.modelService = modelService
    }

    ProductService getProductService()
    {
        productService
    }

    void setProductService(ProductService productService)
    {
        this.productService = productService
    }

    CategoryService getCategoryService()
    {
        categoryService
    }

    void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService
    }

    CatalogVersionService getCatalogVersionService()
    {
        catalogVersionService
    }

    void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService
    }
}
