package com.dnguy38.dogey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

const val MEME_IMAGE_ID = "com.dnguy38.dogey.meme_image_id"
const val PROMPT_ID = "com.dnguy38.dogey.meme_text_id"

class MainActivity : AppCompatActivity() {
    private val model: Dogey = Dogey()
    private val availableMemes = mapOf(
        R.drawable.cute_cat to R.string.cute_cat_prompt
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            model.addCount()
        }

        Snackbar.make(findViewById(R.id.actionButton), "${model.count}", LENGTH_SHORT).show()
    }

    fun onGenerateMeme(view: View) {
        val intent = Intent(this, MemeActivity::class.java)

        val memeImageId = availableMemes.keys.random()
        val promptId = availableMemes[memeImageId]

        intent.putExtra(MEME_IMAGE_ID, memeImageId)
        intent.putExtra(PROMPT_ID, promptId)

        startForResult.launch(intent)
    }
}