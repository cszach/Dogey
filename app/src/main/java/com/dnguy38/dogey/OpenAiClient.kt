package com.dnguy38.dogey

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class OpenAiClient(url: String, apiKey: String) {
    private val TAG = "OpenAiClient"
    private val url = URL(url)
    private val connection = this.url.openConnection() as HttpURLConnection

    init {
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Authorization", "Bearer $apiKey")
        connection.doOutput = true
    }

    fun responseFor(json: String): JSONObject? {
        val out = DataOutputStream(connection.outputStream)

        out.writeBytes(json)
        out.flush()
        out.close()

        Log.d(TAG, "Response: ${connection.responseCode} ${connection.responseMessage}")

        val inputStream: InputStream = when (connection.responseCode) {
            HttpURLConnection.HTTP_OK -> connection.inputStream
            else -> connection.errorStream
        }

        val reader = BufferedReader(InputStreamReader(inputStream))
        var response = ""
        var readLine: String?

        do {
            readLine = reader.readLine()
            response += readLine.orEmpty()
        } while (readLine != null)

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            Log.e(TAG, response)
            return null
        }

        Log.d(TAG, "Server responded: $response")

        return JSONObject(response)
    }

    fun extractCompletions(json: JSONObject): Array<String> {
        val choicesJson = json.get("choices") as JSONArray
        val choices = Array(choicesJson.length()) {
            choicesJson.getString(it)
        }

        return choices
    }
}