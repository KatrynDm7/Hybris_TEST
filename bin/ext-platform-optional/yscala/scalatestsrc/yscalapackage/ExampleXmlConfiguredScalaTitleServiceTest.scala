package yscalapackage

import javax.annotation.Resource

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.user.TitleModel
import de.hybris.platform.servicelayer.ServicelayerBaseTest
import de.hybris.platform.servicelayer.model.ModelService
import org.fest.assertions.Assertions._
import org.junit.{After, Test}

@IntegrationTest
class ExampleXmlConfiguredScalaTitleServiceTest extends ServicelayerBaseTest {

  @Resource(name = "xmlConfiguredScalaTitleService")
  var titleService: ExampleXmlConfiguredScalaTitleService = _
  @Resource
  var modelService: ModelService = _

  @Test
  def shouldCreateNewTitleWithProvidedCodeAndName(): Unit = {
    // given
    val code: String = "myCode"
    val name: String = "myName"

    // when
    val title: TitleModel = titleService.createAndSaveTitle(code, name)

    // then
    assertThat(modelService.isNew(title)).isFalse()
    assertThat(title.getCode).isEqualTo(code)
    assertThat(title.getName).isEqualTo(name)
  }

  @Test
  def shouldFindAlreadyCreatedTitle(): Unit = {
    // given
    val code: String = "myCode"
    val name: String = "myName"
    titleService.createAndSaveTitle(code, name)

    // when
    val title: Option[TitleModel] = titleService.findTitleByCode(code)

    // then
    assertThat(title.isDefined).isTrue()
    assertThat(title.get.getCode).isEqualTo(code)
    assertThat(title.get.getName).isEqualTo(name)
  }

  @After
  def tearDown(): Unit = {
    val title: Option[TitleModel] = titleService.findTitleByCode("myCode")
    if (title.isDefined) modelService.remove(title.get)
  }

}
