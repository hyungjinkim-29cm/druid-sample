package org.example.druidsample

data class DruidApiCommand(
    val queryTemplateId: String,
    val parameters: Map<String, Any>
)
