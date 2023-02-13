package com.dnguy38.dogey

class OpenAiJsonBuilder {
    private var model: String? = null
    private var prompt: String? = null
    private var temperature: Int? = 1
    private var maxTokens: Int? = 16

    fun buildModel(model: String) {
        this.model = model
    }

    fun buildPrompt(prompt: String) {
        this.prompt = prompt
    }

    fun buildTemperature(temperature: Int) {
        this.temperature = temperature
    }

    fun buildMaxTokens(maxTokens: Int) {
        this.maxTokens = maxTokens
    }

    fun build(): String? {
        if (model == null || prompt == null) {
            return null
        }

        return """
            {
                "model": "text-davinci-003",
                "prompt": "$prompt",
                "max_tokens": $maxTokens,
                "temperature": $temperature
            }
        """.trimIndent()
    }
}