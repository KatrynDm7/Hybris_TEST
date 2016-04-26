package ygroovypackage

import de.hybris.platform.media.services.MimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * This is an example how to do Spring Dependency Injection in Groovy service using annotations.
 */
@Service
class ExampleAnnotationConfiguredGroovyMimeService
{
    @Autowired
    private MimeService mimeService

    String getMimeForFileExtension(String fileExt)
    {
        mimeService.getMimeFromFileExtension(fileExt)
    }

    void setMimeService(MimeService mimeService)
    {
        this.mimeService = mimeService
    }
}