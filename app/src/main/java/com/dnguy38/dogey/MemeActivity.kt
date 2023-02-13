package com.dnguy38.dogey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

const val COUNT = "com.dnguy38.dogey.count"

class MemeActivity : AppCompatActivity() {
    private val model: Dogey = Dogey()
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

            startMemeThread(memeImageId, prompt)
        }
    }

    private fun startMemeThread(memeImageId: Int, prompt: String) {
        val memeGenerationThread = Thread() {
            val memeImage = getDrawable(memeImageId)
            val memeText = model.aiGeneratedMemeText(prompt, getString(R.string.api_key))

            runOnUiThread {
                when (memeText) {
                    null -> {
                        memeImageView.setImageDrawable(getDrawable(failImageId))
                        memeTextView.text = getString(failTextId)
                    }
                    else -> {
                        memeImageView.setImageDrawable(memeImage)
                        memeTextView.text = memeText
                    }
                }
            }

            val result = when (memeText) {
                null -> RESULT_CANCELED
                else -> RESULT_OK
            }

            if (memeText != null) {
                model.addCount()
            }

            val intent = Intent()
            intent.putExtra(COUNT, model.count)
            setResult(result, intent)
        }

        memeGenerationThread.start()
    }
}