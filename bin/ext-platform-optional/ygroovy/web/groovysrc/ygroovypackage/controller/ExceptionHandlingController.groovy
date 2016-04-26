package ygroovypackage.controller

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import ygroovypackage.dto.ErrorMessageDTO

@ControllerAdvice
class ExceptionHandlingController
{
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ErrorMessageDTO handleItemNotFound(UnknownIdentifierException ex)
    {
        wrapExceptionToMessage(ex)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErrorMessageDTO handleAny(Exception ex)
    {
        wrapExceptionToMessage(ex)
    }

    ErrorMessageDTO wrapExceptionToMessage(Exception ex)
    {
        ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO()
        errorMessageDTO.setMessage(ex.getMessage())

        errorMessageDTO
    }
}