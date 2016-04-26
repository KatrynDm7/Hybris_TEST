package yscalapackage.controller

import java.lang.{Exception => JException}

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler, ResponseBody, ResponseStatus}
import yscalapackage.dto.ErrorMessageDTO

@ControllerAdvice
class ExceptionHandlingController {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  def handleItemNotFound(ex: UnknownIdentifierException): ErrorMessageDTO = {
    wrapExceptionToMessage(ex)
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  def handleAny(ex: JException): ErrorMessageDTO = {
    wrapExceptionToMessage(ex)
  }

  def wrapExceptionToMessage(ex: JException): ErrorMessageDTO = {
    val errorMessageDTO: ErrorMessageDTO = new ErrorMessageDTO()
    errorMessageDTO.message = ex.getMessage()

    errorMessageDTO
  }
}
