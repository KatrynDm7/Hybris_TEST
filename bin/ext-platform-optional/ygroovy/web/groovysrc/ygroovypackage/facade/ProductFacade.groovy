package ygroovypackage.facade

import de.hybris.platform.catalog.CatalogVersionService
import de.hybris.platform.category.CategoryService
import de.hybris.platform.product.ProductService
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ygroovypackage.dto.ProductsDTO
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.core.model.product.ProductModel
import ygroovypackage.dto.ProductDTO
import de.hybris.platform.category.model.CategoryModel

@Service
class ProductFacade {

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

   ProductsDTO getProductsForCatalogVersion(String catalog, String version) {
        CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalog, version)

       FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {CatalogVersion}=?catalogVersion ORDER BY {code}")
        query.addQueryParameter("catalogVersion", catalogVersion)

        SearchResult searchResult = flexibleSearchService.search(query)
        ProductsDTO productsDTO = new ProductsDTO()
        List<ProductDTO> productDTOList = new ArrayList<>()
        searchResult.getResult().each {productDTOList.add(convertProdtucToDTO($it))}
        productsDTO.setProducts(productDTOList)
        productsDTO
    }

    ProductDTO getProduct(String catalog, String version, String code) {
        CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalog, version)
        ProductModel product = productService.getProductForCode(catalogVersion, code)

        convertProdtucToDTO(product)
    }

    void deleteProduct(String catalog, String version, String code) {
        CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalog, version)
        ProductModel product = productService.getProductForCode(catalogVersion, code)

        modelService.remove(product)
    }

    void updateProduct(String catalog, String version, String code, ProductDTO productDTO) {
        CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalog, version)
        ProductModel product = productService.getProductForCode(catalogVersion, code)
        if (productDTO.getEan()!=null) {
            product.setEan(productDTO.getEan())
        }
        if (productDTO.getName()!=null) {
            product.setName(productDTO.getName())
        }
        applyCategories(productDTO, catalogVersion, product)

        modelService.save(product)
    }

    ProductDTO createProduct(ProductDTO productDTO) {
        CatalogVersionModel ctgVersion = catalogVersionService.getCatalogVersion(productDTO.getCatalog(), productDTO.getVersion())

        ProductModel product = (ProductModel) modelService.create(ProductModel.class)
        product.setCatalogVersion(ctgVersion)
        product.setCode(productDTO.getCode())
        if (productDTO.getEan()!=null) {
            product.setEan(productDTO.getEan())
        }
        if (productDTO.getName()!=null) {
            product.setName(productDTO.getName())
        }
        modelService.save(product)

        applyCategories(productDTO, ctgVersion, product)

        convertProdtucToDTO(product)
    }

    private void applyCategories(ProductDTO productDTO, CatalogVersionModel ctgVersion, ProductModel product) {
        for(String category:productDTO.getCategories()){
            CategoryModel categoryModel = categoryService.getCategoryForCode(ctgVersion, category);
            categoryModel.setProducts(categoryModel.getProducts())
            categoryModel.getProducts().add(product)
            modelService.save(categoryModel)
        }

    }

    ProductDTO convertProdtucToDTO(ProductModel product) {
        ProductDTO dto = new ProductDTO()
        dto.setCode(product.getCode())
        dto.setName(product.getName())
        dto.setEan(product.getEan())
        dto.setCatalog(product.getCatalogVersion().getCatalog().getId())
        dto.setVersion(product.getCatalogVersion().getVersion())
        ArrayList<String> categoryList = new ArrayList<>();
        product.getSupercategories().each {categoryList.add($it.getCode())}
        dto.setCategories(categoryList)
        dto
    }
}