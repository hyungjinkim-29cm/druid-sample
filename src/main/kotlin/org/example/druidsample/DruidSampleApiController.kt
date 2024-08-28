package org.example.druidsample

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DruidSampleApiController(
    val druidApiFacade: DruidApiFacade
) {
    @PostMapping("/http")
    fun executeQueryWithHttp(@RequestBody request: Request): Response<List<String>> {
        val command = DruidApiCommand(request.queryTemplateId, request.parameters)
        val result = druidApiFacade.getCityNameListWithHttp(command)
        return Response(
            result = "SUCCESS",
            data = result,
            errorCode = null
        )
    }

    @PostMapping("/jdbc")
    fun executeQueryWithJdbc(@RequestBody request: Request): Response<List<String>> {
        val command = DruidApiCommand(request.queryTemplateId, request.parameters)
        val result = druidApiFacade.getCityNameListWithJdbc(command)
        return Response(
            result = "SUCCESS",
            data = result,
            errorCode = null
        )
    }

    data class Request(
        val queryTemplateId: String,
        val parameters: Map<String, Any>
    )

    data class Response<T>(
        val result: String,
        val data: T?,
        val errorCode: String?
    )
}
