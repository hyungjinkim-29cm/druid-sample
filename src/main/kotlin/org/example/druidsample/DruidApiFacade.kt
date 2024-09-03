package org.example.druidsample

import org.springframework.stereotype.Service

@Service
class DruidApiFacade(
    val druidQueryTemplateService: DruidQueryTemplateService,
    val druidQueryParameterBindingService: DruidQueryParameterBindingService,
    val druidApiService: DruidApiService
) {
    fun getCityNameListWithHttp(command: DruidApiCommand): List<String> {
        val queryTemplate = druidQueryTemplateService.getQueryTemplateById(command.queryTemplateId)
//        val query = druidQueryParameterBindingService.makeQuery(queryTemplate, command.parameters)
        val query = """
            SELECT *
            FROM "poc_v1_datasource_2024_08_29"
            WHERE "user_property_gender" = 'M' 
            AND "user_property_member_level" = '3'
        """.trimIndent()
//        return druidApiService.findCityNameListWithHttpApi(query)
        return emptyList()
    }

    fun getCityNameListWithJdbc(command: DruidApiCommand): List<String> {
        val queryTemplate = druidQueryTemplateService.getQueryTemplateById(command.queryTemplateId)
        val query = druidQueryParameterBindingService.makeQuery(queryTemplate, command.parameters)
        return druidApiService.findCityNameListWithJdbcApi(query)
    }

    fun test1(): Sequence<String> {
        val query = """
            SELECT DISTINCT "user_property_uid"
            FROM "poc_v1_datasource_2024_08_29"
            WHERE "user_property_gender" = 'F' 
            AND "user_property_member_level" = '4'
            AND "user_property_year_of_birth" BETWEEN '1994' AND '2004'
            AND "user_property_total_order_amount" >= 1000000
        """.trimIndent()
        return druidApiService.findCityNameListWithHttpApi(query)
    }

    fun test2(): Sequence<String> {
        val query = """
            SELECT DISTINCT "user_property_uid"
            FROM "poc_v1_datasource_2024_08_29"
            WHERE user_property_total_order_count >= 4
            AND user_property_gender = 'M'
        """.trimIndent()
        return druidApiService.findCityNameListWithHttpApi(query)
    }

    fun test3(): Sequence<String> {
        val query = """
            SELECT DISTINCT "user_property_uid"
            FROM "poc_v1_datasource_2024_08_29"
            WHERE user_property_user_bucket = 500
            AND user_property_total_order_count = 0
        """.trimIndent()
        return druidApiService.findCityNameListWithHttpApi(query)
    }
}
