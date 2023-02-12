package com.dnguy38.dogey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MemeActivity : AppCompatActivity() {
    private lateinit var memeImageView: ImageView
    private lateinit var memeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)

        memeImageView = findViewById(R.id.memeImageView)
        memeTextView = findViewById(R.id.memeTextView)

        val bundle = intent.extras
        if (bundle != null) {
            val memeImageId = bundle.getInt(MEME_IMAGE_ID)
            val memeText = bundle.getString(MEME_TEXT)

            memeTextView.text = memeText

            val memeImage = getDrawable(memeImageId)

            memeImageView.setImageDrawable(memeImage)
        }
    }
}