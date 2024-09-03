package org.example.druidsample

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response

class DruidResponse(
        response: Response
    ) {
        val inputStream = response.body().asInputStream()
        val bufferedReader = inputStream.bufferedReader()
        var nextLine: String? = null
        var hasNextCalculated = false
        val objectMapper: ObjectMapper = ObjectMapper()

        private fun hasNext(): Boolean {
            if (!hasNextCalculated) {
                hasNextCalculated = true
                nextLine = bufferedReader.readLine() ?: return false
                while (nextLine != null && (nextLine?.isBlank() == true || nextLine == "(not set)" || nextLine == "null")) {
                    nextLine = bufferedReader.readLine()
                }

            }
            return nextLine != null
        }

        private fun next(): String {
            if (!hasNext()) {
                throw NoSuchElementException("No more lines")
            }

            hasNextCalculated = false

            while (true) {
                val line = nextLine ?: throw NoSuchElementException("No more lines")
                val res = objectMapper.readValue(line, DruidApiService.UserIdResponse::class.java)

                if (res.userId?.isBlank() == false && res.userId != "(not set)" && res.userId != "null") {
                    return res.userId
                }

                // 다음 줄 읽기
                nextLine =
                    bufferedReader.readLine() ?: throw NoSuchElementException("No more lines")
                hasNextCalculated = true
            }
        }

        fun getData(): Sequence<String> = sequence {
            var nextLine: String? = bufferedReader.readLine()

            while (nextLine != null) {
                // Skip unwanted lines
                while (nextLine != null && (nextLine?.isBlank() == true || nextLine == "(not set)" || nextLine == "null")) {
                    nextLine = bufferedReader.readLine()
                }

                if (nextLine != null) {
                    val res = objectMapper.readValue(nextLine, DruidApiService.UserIdResponse::class.java)

                    if (res.userId?.isBlank() == false && res.userId != "(not set)" && res.userId != "null") {
                        yield(res.userId)
                    }

                    nextLine = bufferedReader.readLine()
                }
            }
        }
    }
