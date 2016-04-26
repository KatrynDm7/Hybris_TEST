package ygroovypackage

import static org.fest.assertions.Assertions.assertThat

import javax.annotation.Resource

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.user.TitleModel
import de.hybris.platform.servicelayer.ServicelayerBaseTest
import de.hybris.platform.servicelayer.model.ModelService

import org.junit.After
import org.junit.Test

@IntegrationTest
class ExampleXmlConfiguredGroovyTitleServiceTest extends ServicelayerBaseTest {

  @Resource(name = "xmlConfiguredGroovyTitleService")
  private ExampleXmlConfiguredGroovyTitleService titleService;
  @Resource
  private ModelService modelService;

  @Test
  void shouldCreateNewTitleWithProvidedCodeAndName()
  {
    // given
      String code = "myCode"
      String name = "myName"

    // when
    TitleModel title = titleService.createAndSaveTitle(code, name)

    // then
    assertThat(modelService.isNew(title)).isFalse()
    assertThat(title.getCode()).isEqualTo(code)
    assertThat(title.getName()).isEqualTo(name)
  }

  @Test
  void shouldFindAlreadyCreatedTitle()
  {
    // given
      String code = "myCode"
      String name = "myName"
    titleService.createAndSaveTitle(code, name)

    // when
    TitleModel title = titleService.findTitleByCode(code)

    // then
    assertThat(title!=null).isTrue()
    assertThat(title.getCode()).isEqualTo(code)
    assertThat(title.getName()).isEqualTo(name)
  }

  @After
  void tearDown()
  {
    TitleModel title = titleService.findTitleByCode("myCode")
    if (title!=null) modelService.remove(title)
  }

}
