package org.example.druidsample

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class CommonControllerAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = [BadRequestException::class, NotFoundException::class, ServerException::class, GenericFeignException::class])
    fun onException(e: Exception): DruidSampleApiController.Response<*> {
        return DruidSampleApiController.Response(
            result = "FAIL",
            data = null,
            errorCode = e.message
        )
    }
}
