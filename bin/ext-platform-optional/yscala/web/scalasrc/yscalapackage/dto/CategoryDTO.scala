package yscalapackage.dto

import scala.beans.BeanProperty

class CategoryDTO {
   @BeanProperty
   var catalog: String = _
   @BeanProperty
   var version: String = _
   @BeanProperty
   var code: String = _
   @BeanProperty
   var name: String = _
 }
