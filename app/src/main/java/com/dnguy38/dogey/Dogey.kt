package com.dnguy38.dogey

import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class Dogey {
    var count: Int = 0

    fun addCount() {
        count++
    }

    fun aiGeneratedMemeText(prompt: String, apiKey: String): String? {
        val url = URL("https://api.openai.com/v1/completions")
        val connection = url.openConnection() as HttpURLConnection
        var response = ""
        var text: String

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            // connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            val dataJson = """
                {
                    "model": "text-davinci-003",
                    "prompt": "$prompt"
                }""".trimIndent()
            println(dataJson)

            val out = DataOutputStream(connection.outputStream)
            // val dataJsonBytes = dataJson.encodeToByteArray()

            out.writeBytes(dataJson)
            out.flush()
            out.close()

            println(connection.responseCode)
            println(connection.responseMessage)

            var inputStream: InputStream

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.inputStream
            } else {
                inputStream = connection.errorStream
            }

            val reader = BufferedReader(InputStreamReader(inputStream))
            var readLine: String?

            while (true) {
                readLine = reader.readLine()

                if (readLine == null) {
                    break
                }

                response += readLine
            }

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return null
            }

            println(response)

            val json = JSONObject(response)

            val choicesJson = json.get("choices") as JSONArray
            val firstChoice = choicesJson.get(0) as JSONObject
            text = firstChoice.get("text") as String

            println(text)
        } finally {
            connection.disconnect()
        }

        return text.trim().trim('"')
    }
}