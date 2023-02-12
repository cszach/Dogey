package com.dnguy38.dogey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

const val MEME_IMAGE_ID = "com.dnguy38.dogey.meme_image"
const val MEME_TEXT = "com.dnguy38.dogey.meme_text"

class MainActivity : AppCompatActivity() {
    private val model: Dogey = Dogey()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Snackbar.make(findViewById(R.id.actionButton), "${model.count}", LENGTH_SHORT).show()
    }

    fun onGenerateMeme(view: View) {
        val intent = Intent(this, MemeActivity::class.java)
        val memeImageId = R.drawable.cute_cat
        var memeText: String = "default"
        val prompt = getString(R.string.cute_cat_prompt)

        val networkThread = Thread() {
            memeText = model.aiGeneratedMemeText("$prompt", getString(R.string.api_key))
        }
        networkThread.start()
        networkThread.join()

        intent.putExtra(MEME_IMAGE_ID, memeImageId)
        intent.putExtra(MEME_TEXT, memeText)

        println(memeText)

        startForResult.launch(intent)
        model.addCount()
    }
}