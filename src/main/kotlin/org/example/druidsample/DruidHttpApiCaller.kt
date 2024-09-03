package org.example.druidsample

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import feign.Response

@FeignClient(name = "druidClient", url = "http://10.89.57.187:8888", configuration = [FeignClientConfig::class])
interface DruidHttpApiCaller {
    @PostMapping("/druid/v2/sql")
    fun execute(@RequestBody request: DruidHttpRequest): Response
}

data class DruidHttpRequest(
    val query: String,
    val context: Context,
    val header: Boolean,
    val typesHeader: Boolean,
    val sqlTypesHeader: Boolean,
    val resultFormat: String = "objectLines"
) {
    data class Context(
        val sqlQueryId: String
    )
}
