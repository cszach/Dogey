package com.dnguy38.dogey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONObject

class MemeActivity : AppCompatActivity() {
    private val jsonBuilder = OpenAiJsonBuilder()
    private lateinit var apiClient: OpenAiClient
    private lateinit var memeImageView: ImageView
    private lateinit var memeTextView: TextView
    private val loadingImageId = R.drawable.swole_doge
    private val loadingTextId = R.string.loading_text
    private val failImageId = R.drawable.cheems
    private val failTextId = R.string.text_fail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)

        memeImageView = findViewById(R.id.memeImageView)
        memeTextView = findViewById(R.id.memeTextView)

        // Loading

        memeImageView.setImageDrawable(getDrawable(loadingImageId))
        memeTextView.text = getString(loadingTextId)

        val bundle = intent.extras
        if (bundle != null) {
            val memeImageId = bundle.getInt(MEME_IMAGE_ID)
            val memePromptId = bundle.getInt(PROMPT_ID)
            val prompt = getString(memePromptId)

            apiClient = OpenAiClient(getString(R.string.openai_url), getString(R.string.api_key))

            jsonBuilder.buildModel(getString(R.string.model))
            jsonBuilder.buildTemperature(resources.getInteger(R.integer.temperature))
            jsonBuilder.buildMaxTokens(resources.getInteger(R.integer.max_tokens))

            startMemeThread(memeImageId, prompt)
        }
    }

    private fun startMemeThread(memeImageId: Int, prompt: String) {
        jsonBuilder.buildPrompt(prompt)

        val data = jsonBuilder.build()

        if (data == null) {
            loadFailMeme()
            return
        }

        val memeGenerationThread = Thread() {
            val memeImage = getDrawable(memeImageId)
            val response = apiClient.responseFor(data)

            if (response == null) {
                runOnUiThread {
                    loadFailMeme()
                }
                return@Thread
            }

            val memeTextChoices = apiClient.extractCompletions(response)

            runOnUiThread {
                when (memeTextChoices.size) {
                    0 -> loadFailMeme()
                    else -> {
                        memeImageView.setImageDrawable(memeImage)
                        memeTextView.text = (JSONObject(memeTextChoices[0])).get("text") as String
                    }
                }
            }

            val result = when (memeTextChoices.size) {
                0 -> RESULT_CANCELED
                else -> RESULT_OK
            }

            val intent = Intent()
            setResult(result, intent)
        }

        memeGenerationThread.start()
    }

    private fun loadFailMeme() {
        memeImageView.setImageDrawable(getDrawable(failImageId))
        memeTextView.text = getString(failTextId)
    }
}