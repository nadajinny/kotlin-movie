package api

import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import util.ErrorMessage

@RestControllerAdvice
class ApiExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ErrorResponse =
        ErrorResponse(exception.message ?: ErrorMessage.INVALID_INPUT)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(): ErrorResponse = ErrorResponse(ErrorMessage.INVALID_INPUT)

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SeatAlreadyReservedException::class)
    fun handleSeatAlreadyReservedException(exception: SeatAlreadyReservedException): ErrorResponse = ErrorResponse(exception.message)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ScreeningNotFoundException::class)
    fun handleScreeningNotFoundException(exception: ScreeningNotFoundException): ErrorResponse = ErrorResponse(exception.message)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiBadRequestException::class)
    fun handleApiBadRequestException(exception: ApiBadRequestException): ErrorResponse = ErrorResponse(exception.message)
}

class SeatAlreadyReservedException(
    override val message: String,
) : RuntimeException(message)

class ScreeningNotFoundException(
    override val message: String,
) : RuntimeException(message)

class ApiBadRequestException(
    override val message: String,
) : RuntimeException(message)
