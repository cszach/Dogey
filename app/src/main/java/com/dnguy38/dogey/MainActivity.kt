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
        R.drawable.cute_cat to R.string.cute_cat_prompt,
        R.drawable.gigachad to R.string.gigachad_prompt,
        R.drawable.this_is_fine to R.string.this_is_fine_prompt,
        R.drawable.waiting_skeleton to R.string.waiting_skeleton_prompt,
        R.drawable.blank_nut_button to R.string.blank_nut_button_prompt,
        R.drawable.brace_yourselves_x_is_coming to R.string.brace_yourselves_x_is_coming_prompt,
        R.drawable.wake_up_babe to R.string.wake_up_babe_prompt,
        R.drawable.resource_finally to R.string.finally_prompt
    )
    private lateinit var numberOfMemesText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberOfMemesText = getString(R.string.number_of_memes_text)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            model.addCount()
        }

        Snackbar.make(findViewById(R.id.actionButton), "$numberOfMemesText ${model.count}", LENGTH_SHORT).show()
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