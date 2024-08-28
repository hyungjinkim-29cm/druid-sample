package org.example.druidsample

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class DruidApiService(
    private val druidHttpApiCaller: DruidHttpApiCaller,
    private val jdbcTemplate: JdbcTemplate,
) {
    companion object {
        const val RESULT_FIELD = "cityName"
    }

    fun findCityNameListWithHttpApi(query: String): List<String> {
        val request = DruidHttpRequest(
            query = query,
            context = DruidHttpRequest.Context(sqlQueryId = UUID.randomUUID().toString()),
            header = false,
            typesHeader = false,
            sqlTypesHeader = false
        )

        // todo : save sqlQueryId for query monitoring

        val execute = druidHttpApiCaller.execute(request)
        val response = execute as List<LinkedHashMap<String, String?>>
        return response.mapNotNull { it[RESULT_FIELD] }
    }

    fun findCityNameListWithJdbcApi(query: String): List<String> {
        try {
            return jdbcTemplate.query(query) { rs, _ -> rs.getString(RESULT_FIELD) }.filterNotNull()
        } catch (e: Exception) {
            println("Database connection failed.")
            e.printStackTrace()
        }
        return emptyList()
    }
}
