package yscalapackage

import java.util.Objects

import de.hybris.platform.core.model.user.TitleModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.{FlexibleSearchQuery, FlexibleSearchService, SearchResult}
import org.springframework.beans.factory.annotation.Required

import scala.beans.BeanProperty

/**
 * This is an example how to do Spring Dependency Injection in Scala service using XML configuration.
 */
class ExampleXmlConfiguredScalaTitleService {

  @Required
  @BeanProperty
  var flexibleSearchService: FlexibleSearchService = _

  @Required
  @BeanProperty
  var modelService: ModelService = _

  def createAndSaveTitle(code: String, name: String): TitleModel = {
    Objects.requireNonNull(code, "code must not be null")
    Objects.requireNonNull(name, "name must not be null")

    val title: TitleModel = modelService.create(classOf[TitleModel])
    title.setCode(code)
    title.setName(name)
    modelService.save(title)

    title
  }

  def findTitleByCode(code: String): Option[TitleModel] = {
    Objects.requireNonNull(code, "code must not be null")

    val query: FlexibleSearchQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Title} WHERE {code}=?code")
    query.addQueryParameter("code", code)

    val searchResult: SearchResult[TitleModel] = flexibleSearchService.search(query)

    if (searchResult.getCount == 1) {
      return Some(searchResult.getResult.get(0))
    }

    None
  }

}
