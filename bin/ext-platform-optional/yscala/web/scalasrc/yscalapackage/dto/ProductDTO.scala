package yscalapackage.dto

import java.util

import scala.beans.BeanProperty

class ProductDTO {

  @BeanProperty
  var catalog: String = _
  @BeanProperty
  var version: String = _
  @BeanProperty
  var categories: util.List[String] = util.Collections.emptyList()
  @BeanProperty
  var code: String = _
  @BeanProperty
  var name: String = _
  @BeanProperty
  var ean: String = _

  def catalogOption() = Option(catalog)

  def versionOption() = Option(version)

  def codeOption() = Option(code)

  def nameOption() = Option(name)

  def eanOption() = Option(ean)
}
