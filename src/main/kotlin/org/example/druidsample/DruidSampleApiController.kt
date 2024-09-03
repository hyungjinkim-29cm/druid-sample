package org.example.druidsample

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

@RestController
class DruidSampleApiController(
    val druidApiFacade: DruidApiFacade,
    val objectMapper: ObjectMapper
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


    //    맴버 등급이 4인 20대 여성 유저 중 총 구매액(total_order_amount)이 100만원이상인 유저
    @PostMapping("/test1")
    fun test1(): StreamingResponseBody {
        val stringSequence = druidApiFacade.test1()
        return toStreamingResponseBody(stringSequence)
    }

    private fun toStreamingResponseBody(stringSequence: Sequence<String>, batchSize: Int = 1000): StreamingResponseBody {
        return StreamingResponseBody { outputStream ->
            val buffer = StringBuilder()

            stringSequence.chunked(batchSize).forEach { chunk ->
                chunk.forEach { str ->
                    buffer.append(str).append("\n")
                }

                // 청크 단위로 데이터를 전송
                outputStream.write(buffer.toString().toByteArray())
                outputStream.flush()
                buffer.clear()
            }

            // 남아있는 데이터가 있을 경우 전송
            if (buffer.isNotEmpty()) {
                throw RuntimeException("logic error")

            }
        }
    }

    //        return StreamingResponseBody { outputStream ->
//            val buffer = StringBuilder()
//
//            stringSequence.forEachIndexed { index, str ->
//                buffer.append(str).append("\n")
//
//                // 일정 개수의 데이터를 모아 한 번에 전송
//                if ((index + 1) % batchSize == 0) {
//                    val response = Response(
//                        result = "SUCCESS",
//                        data = buffer,
//                        errorCode = null
//                    )
//                    outputStream.write(objectMapper.writeValueAsBytes(response))
//                    outputStream.flush()
//                    buffer.clear()
//                }
//            }
//
//            // 남아있는 데이터 전송
//            if (buffer.isNotEmpty()) {
//                val response = Response(
//                    result = "SUCCESS",
//                    data = buffer,
//                    errorCode = null
//                )
//                outputStream.write(objectMapper.writeValueAsBytes(response))
//                outputStream.flush()
//            }
//        }

    @PostMapping("/test2")
    fun test2(): StreamingResponseBody {
        val stringSequence = druidApiFacade.test2()
        return toStreamingResponseBody(stringSequence)
    }

    @PostMapping("/test3")
    fun test3(): StreamingResponseBody {
        val stringSequence = druidApiFacade.test3()
        return toStreamingResponseBody(stringSequence)
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
