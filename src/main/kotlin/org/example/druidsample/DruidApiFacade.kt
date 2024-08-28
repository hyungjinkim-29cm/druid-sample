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
        val query = druidQueryParameterBindingService.makeQuery(queryTemplate, command.parameters)
        return druidApiService.findCityNameListWithHttpApi(query)
    }

    fun getCityNameListWithJdbc(command: DruidApiCommand): List<String> {
        val queryTemplate = druidQueryTemplateService.getQueryTemplateById(command.queryTemplateId)
        val query = druidQueryParameterBindingService.makeQuery(queryTemplate, command.parameters)
        return druidApiService.findCityNameListWithJdbcApi(query)
    }
}
