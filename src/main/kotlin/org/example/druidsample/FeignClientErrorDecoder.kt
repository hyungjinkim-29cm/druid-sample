package org.example.druidsample

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder

class FeignClientErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        val status = response.status()
        val responseBody = response.body().asInputStream()
        val errorResponse = objectMapper.readValue(responseBody, ErrorResponse::class.java)

        return when (status) {
            400 -> BadRequestException(errorResponse)
            404 -> NotFoundException(errorResponse)
            500 -> ServerException(errorResponse)
            else -> GenericFeignException(errorResponse)
        }
    }

    private val objectMapper = ObjectMapper()

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ErrorResponse(
        val error: String? = null,
        val errorCode: String? = null,
        val errorMessage: String? = null,
    )
}

class BadRequestException(errorResponse: FeignClientErrorDecoder.ErrorResponse) :
    RuntimeException(errorResponse.errorCode)

class NotFoundException(errorResponse: FeignClientErrorDecoder.ErrorResponse) :
    RuntimeException(errorResponse.errorCode)

class ServerException(errorResponse: FeignClientErrorDecoder.ErrorResponse) :
    RuntimeException(errorResponse.errorCode)

class GenericFeignException(errorResponse: FeignClientErrorDecoder.ErrorResponse) :
    RuntimeException(errorResponse.errorCode)
