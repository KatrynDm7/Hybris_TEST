package yscalapackage.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, MediaType}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import yscalapackage.dto.CategoryDTO
import yscalapackage.facade.CategoryFacade

import scala.beans.BeanProperty

@Controller
@RequestMapping(value = Array("categories"))
class CategoryController {

  @BeanProperty
  @Autowired
  var categoryFacade: CategoryFacade = _

  /**
   * Creates new category.
   * Request URL: http://server:port/yscala/categories
   * Method: POST
   * JSON:
   * {
   *  "code": "CateogyCode",
   *  "name": "Category Name",
   *  "catalog": "Catalog ID",
   *  "version": "Catalog Version"
   * }
   */
  @RequestMapping(method = Array(RequestMethod.POST), consumes = Array(MediaType.APPLICATION_JSON_VALUE))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.CREATED)
  def create(@RequestBody categoryDTO: CategoryDTO): Unit = categoryFacade.createCategory(categoryDTO)

  /**
   * Removes existing category.
   * Request URL: http://server:port/yscala/catalogID/catalogVersion/categoryCode
   * Method: DELETE
   */
  @RequestMapping(value = Array("{catalog}/{version}/{categoryCode}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  def delete(@PathVariable catalog: String, @PathVariable version: String, @PathVariable categoryCode: String): Unit = {
    categoryFacade.deleteCategory(catalog, version, categoryCode)
  }
}
