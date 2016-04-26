package ygroovypackage.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import ygroovypackage.dto.CategoryDTO
import ygroovypackage.facade.CategoryFacade

@Controller
@RequestMapping(value="categories")
class CategoryController
{
    @Autowired
    private CategoryFacade categoryFacade

    CategoryFacade getCategoryFacade()
    {
        categoryFacade
    }

    @RequestMapping(method=org.springframework.web.bind.annotation.RequestMethod.POST, consumes="application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestBody CategoryDTO categoryDTO)
    {
        getCategoryFacade().createCategory(categoryDTO)
    }

    @RequestMapping(value="catalog}/{version}/{categoryCode}", method=org.springframework.web.bind.annotation.RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    void delete(@PathVariable String catalog, @PathVariable String version, @PathVariable String categoryCode)
    {
        getCategoryFacade().deleteCategory(catalog, version, categoryCode)
    }
}