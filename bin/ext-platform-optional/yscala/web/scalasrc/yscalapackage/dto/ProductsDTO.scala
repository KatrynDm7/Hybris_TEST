package yscalapackage.dto

import scala.beans.BeanProperty

class ProductsDTO {

   @BeanProperty
   var products: java.lang.Iterable[ProductDTO] = _
 }
