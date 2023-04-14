package lab.maxb.dark_api.application.rest.exceptions

import lab.maxb.dark_api.application.response.ResponseError
import lab.maxb.dark_api.domain.exceptions.AccessDeniedException
import lab.maxb.dark_api.domain.exceptions.DomainException
import lab.maxb.dark_api.domain.exceptions.NotFoundException
import lab.maxb.dark_api.domain.exceptions.ValidationError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(DomainException::class)
    fun handleDomainException(exception: DomainException): ResponseEntity<ResponseError> {
        val status = when (exception) {
            is AccessDeniedException -> HttpStatus.FORBIDDEN
            is NotFoundException -> HttpStatus.NOT_FOUND
            else -> HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).body(ResponseError(exception.message))
    }

    @ExceptionHandler(ValidationError::class)
    fun handleValidationError(error: ValidationError)
        = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResponseError(
            error.message, error.errors
        ))
}