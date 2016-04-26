package yscalapackage

import javax.annotation.Resource

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.servicelayer.ServicelayerBaseTest
import org.fest.assertions.Assertions._
import org.junit.Test

@IntegrationTest
class ExampleAnnotationConfiguredScalaMimeServiceTest extends ServicelayerBaseTest {

  @Resource(name = "exampleAnnotationConfiguredScalaMimeService")
  var service: ExampleAnnotationConfiguredScalaMimeService = _

  @Test
  def jpgFileExtensionShouldHaveImageJpegMime(): Unit = {
    // given
    val fileExt: String = "jpg"

    // when
    val mime: String = service.getMimeForFileExtension(fileExt)

    // then
    assertThat(mime).isNotNull.isEqualTo("image/jpeg")
  }
}
