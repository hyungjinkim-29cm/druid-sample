package org.example.druidsample

import org.springframework.stereotype.Service

@Service
class DruidQueryTemplateService {
    fun getQueryTemplateById(queryTemplateId: String): QueryTemplate {
        //todo : fetch from database by queryTemplateId
        if (queryTemplateId == "test") {
            return QueryTemplate("SELECT __time, channel, cityName, comment FROM wikipedia WHERE countryName = :countryName ORDER BY __time DESC")
        }

        return QueryTemplate("INVALID QUERY")
    }

    data class QueryTemplate(val query: String)
}
