package org.example.druidsample

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class DruidApiService(
    private val druidHttpApiCaller: DruidHttpApiCaller,
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    fun findCityNameListWithHttpApi(query: String): Sequence<String> {
        val request = DruidHttpRequest(
            query = query,
            context = DruidHttpRequest.Context(sqlQueryId = UUID.randomUUID().toString()),
            header = false,
            typesHeader = false,
            sqlTypesHeader = false
        )

        // todo : save sqlQueryId for query monitoring

        val execute = druidHttpApiCaller.execute(request)
//        val iterator = processDruidResponse(execute).asSequence().chunked(1000).iterator()
        return processDruidResponse(execute).asSequence()

//                iterator.
//        while (iterator.hasNext()) {
//             val list = iterator.next()
//        }

//        val res = execute.toLineSequence()
//        println(execute)
//        val response = execute as List<LinkedHashMap<String, String?>>
//        return response.mapNotNull { it[RESULT_FIELD] }
    }

    fun processDruidResponse(response: Response): Iterator<String> {
        val inputStream = response.body().asInputStream()
        val bufferedReader = inputStream.bufferedReader()

        // Iterator를 반환하여 데이터를 순차적으로 가져옴
        return object : Iterator<String> {
            var nextLine: String? = null
            var hasNextCalculated = false

            override fun hasNext(): Boolean {
                if (!hasNextCalculated) {
                    hasNextCalculated = true
                    nextLine = bufferedReader.readLine() ?: return false
                    while (nextLine != null && (nextLine?.isBlank()  == true || nextLine == "(not set)" || nextLine == "null")) {
                        nextLine = bufferedReader.readLine()
                    }

                }
                return nextLine != null
            }

            override fun next(): String {
                if (!hasNext()) {
                    throw NoSuchElementException("No more lines")
                }

                hasNextCalculated = false

                while (true) {
                    val line = nextLine ?: throw NoSuchElementException("No more lines")
                    val res = objectMapper.readValue(line, UserIdResponse::class.java)

                    if (res.userId?.isBlank() == false && res.userId != "(not set)" && res.userId != "null") {
                        return res.userId
                    }

                    // 다음 줄 읽기
                    nextLine = bufferedReader.readLine() ?: throw NoSuchElementException("No more lines")
                    hasNextCalculated = true
                }
            }
        }
    }
    data class UserIdResponse(
        @JsonProperty("user_property_uid")
        val userId: String?
    )

    fun findCityNameListWithJdbcApi(query: String): List<String> {
        try {
            return jdbcTemplate.query(query) { rs, _ -> rs.getString("RESULT_FIELD") }.filterNotNull()
        } catch (e: Exception) {
            println("Database connection failed.")
            e.printStackTrace()
        }
        return emptyList()
    }
}
