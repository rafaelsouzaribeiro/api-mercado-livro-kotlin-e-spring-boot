package mercado_livro.expection

import mercado_livro.controller.response.ErrorResponse
import mercado_livro.controller.response.FieldErrorResponse
import mercado_livro.enums.Errors
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(NotFoundException::class)
    fun handlerNotFoundExpection(ex:NotFoundException,request:WebRequest):ResponseEntity<ErrorResponse>{
        val erro = ErrorResponse(
            httpCode = HttpStatus.NOT_FOUND.value(),
            message = ex.message,
            internalCode = ex.errorCode,
            erros = null
        )

        return ResponseEntity(erro, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handlerAccessDeniedException(ex:AccessDeniedException,request:WebRequest):ResponseEntity<ErrorResponse>{
        val erro = ErrorResponse(
            httpCode = HttpStatus.FORBIDDEN.value(),
            message =Errors.MLOOO.message,
            internalCode = Errors.MLOOO.code,
            erros = null
        )

        return ResponseEntity(erro, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(BadResquetExpection::class)
    fun handlerBadRequestException(ex:BadResquetExpection,request:WebRequest):ResponseEntity<ErrorResponse>{
        val erro = ErrorResponse(
            httpCode = HttpStatus.BAD_REQUEST.value(),
            message = ex.message,
            internalCode = ex.errorCode,
            erros = null
        )

        return ResponseEntity(erro, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleArgumentTypeMismatch(
        ex: MethodArgumentTypeMismatchException
    ): ResponseEntity<ErrorResponse> {
        val valorInvalido = ex.value?.toString() ?: ""
        val error = ErrorResponse(
            httpCode = HttpStatus.BAD_REQUEST.value(),
            message = Errors.ML301.message.format(valorInvalido),
            internalCode = Errors.ML301.code,
            erros = null
        )

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex:MethodArgumentNotValidException,request:WebRequest):ResponseEntity<ErrorResponse>{
        val erro = ErrorResponse(
            httpCode = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            message = Errors.ML001.message,
            internalCode = Errors.ML001.code,
            erros = ex.bindingResult.fieldErrors.map {
                FieldErrorResponse(it.defaultMessage?:"invalid",it.field)
            }
        )

        return ResponseEntity(erro, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex:HttpMessageNotReadableException,request:WebRequest):ResponseEntity<ErrorResponse>{
        val erro = ErrorResponse(
            httpCode = HttpStatus.BAD_REQUEST.value(),
            message = ex.message?:Errors.ML401.message,
            internalCode = Errors.ML401.code,
            erros = null
        )

        return ResponseEntity(erro, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleHttpMessageNotReadableException(ex:DataIntegrityViolationException,request:WebRequest):ResponseEntity<ErrorResponse>{
        val erro = ErrorResponse(
            httpCode = HttpStatus.BAD_REQUEST.value(),
            message = Errors.ML002.message,
            internalCode = Errors.ML002.code,
            erros = null
        )

        return ResponseEntity(erro, HttpStatus.BAD_REQUEST)
    }
}