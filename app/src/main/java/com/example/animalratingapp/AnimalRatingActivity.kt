package com.example.animalratingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar

class AnimalRatingActivity : AppCompatActivity() {
    private lateinit var animalName: String
    private lateinit var ratingBar: RatingBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_rating)

        ratingBar = findViewById(R.id.ratingBar)
        animalName = intent.getStringExtra("animalName")!!
        val image = findViewById<ImageView>(R.id.image_animal)

        val id = when (animalName) {
            "Dog" -> R.drawable.dog
            "Cat" -> R.drawable.cat
            "Bear" -> R.drawable.bear
            else -> R.drawable.rabbit
        }
        image.setImageResource(id)

        val sharedPreference = getPreferences(MODE_PRIVATE)
        val rating = sharedPreference.getFloat(animalName, -1F)
        if (rating != -1F) {
            ratingBar.rating = rating
        } else {
            ratingBar.rating = 0F
        }
    }

    fun saveRating(view: View) {
        val sharedPreference = getPreferences(MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putFloat(animalName, ratingBar.rating)
        editor.apply()
        Log.i("AnimalRatingActivity", "$animalName : ${ratingBar.rating}")
    }
}