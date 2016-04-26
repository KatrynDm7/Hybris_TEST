package ygroovypackage

import de.hybris.platform.core.model.user.TitleModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import org.springframework.beans.factory.annotation.Autowired

/**
 * This is an example how to do Spring Dependency Injection in Groovy service using XML configuration.
 */
class ExampleXmlConfiguredGroovyTitleService {
    @Autowired
    private FlexibleSearchService flexibleSearchService
    @Autowired
    private ModelService modelService

    TitleModel createAndSaveTitle(String code, String name) {
        Objects.requireNonNull(code, "code must not be null")
        Objects.requireNonNull(name, "name must not be null")

        TitleModel title = (TitleModel) modelService.create(TitleModel.class)
        title.setCode(code)
        title.setName(name)
        modelService.save(title)

        title
    }

    TitleModel findTitleByCode(String code) {
        Objects.requireNonNull(code, "code must not be null")

        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {Title} WHERE {code}=?code")
        query.addQueryParameter("code", code)

        SearchResult searchResult = flexibleSearchService.search(query)
        if (searchResult.getCount() == 1) {
            return searchResult.getResult().get(0)
        }
        return null
    }

    void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService
    }

    void setModelService(ModelService modelService) {
        this.modelService = modelService
    }
}
