package org.example.druidsample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class DruidSampleApplication

fun main(args: Array<String>) {
	runApplication<DruidSampleApplication>(*args)
}
