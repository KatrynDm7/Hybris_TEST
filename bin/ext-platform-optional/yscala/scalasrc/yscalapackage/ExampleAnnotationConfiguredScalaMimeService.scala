package yscalapackage

import de.hybris.platform.media.services.MimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.beans.BeanProperty

/**
 * This is an example how to do Spring Dependency Injection in Scala service using annotations.
 */
@Service
class ExampleAnnotationConfiguredScalaMimeService {

  @BeanProperty
  @Autowired
  var mimeService: MimeService = _

  def getMimeForFileExtension(fileExt: String): String = {
    mimeService.getMimeFromFileExtension(fileExt)
  }
}
