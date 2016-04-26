package ygroovypackage

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.servicelayer.ServicelayerBaseTest
import javax.annotation.Resource
import org.fest.assertions.Assertions
import org.fest.assertions.GenericAssert

import org.junit.Test

@IntegrationTest
class ExampleAnnotationConfiguredGroovyMimeServiceTest
        extends ServicelayerBaseTest
{
    @Resource(name="exampleAnnotationConfiguredGroovyMimeService")
    private ExampleAnnotationConfiguredGroovyMimeService service

    void setService(ExampleAnnotationConfiguredGroovyMimeService service)
    {
        this.service = service
    }

    @Test
    void jpgFileExtensionShouldHaveImageJpegMime()
    {
        String fileExt = "jpg"


        String mime = service.getMimeForFileExtension(fileExt)


        ((GenericAssert)Assertions.assertThat(mime).isNotNull()).isEqualTo("image/jpeg")
    }
}