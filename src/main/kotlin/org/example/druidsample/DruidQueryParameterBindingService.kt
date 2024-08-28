package org.example.druidsample

import org.springframework.stereotype.Service

@Service
class DruidQueryParameterBindingService {
    fun makeQuery(queryTemplate: DruidQueryTemplateService.QueryTemplate, parameters: Map<String, Any>): String {
        var query = queryTemplate.query
        parameters.forEach { (key, value) ->
            query = query.replace(":$key", "'$value'")
        }
        return query
    }
}
