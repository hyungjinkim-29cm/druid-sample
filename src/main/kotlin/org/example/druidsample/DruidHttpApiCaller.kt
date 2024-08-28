package org.example.druidsample

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "druidClient", url = "http://localhost:8888", configuration = [FeignClientConfig::class])
interface DruidHttpApiCaller {
    @PostMapping("/druid/v2/sql")
    fun execute(@RequestBody request: DruidHttpRequest): Any
}

data class DruidHttpRequest(
    val query: String,
    val context: Context,
    val header: Boolean,
    val typesHeader: Boolean,
    val sqlTypesHeader: Boolean
) {
    data class Context(
        val sqlQueryId: String
    )
}
