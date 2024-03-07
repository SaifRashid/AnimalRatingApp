package com.example.animalratingapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

class AnimalRatingActivity : AppCompatActivity() {
    private lateinit var animalName: String
    private lateinit var ratingBar: RatingBar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_rating)

        ratingBar = findViewById(R.id.ratingBar)
        animalName = intent.getStringExtra("animalName")!!
        val image = findViewById<ImageView>(R.id.image_animal)
        val animalTitle = findViewById<TextView>(R.id.text_animal_name)
        animalTitle.text = animalName

        val id = when (animalName) {
            "Dog" -> R.drawable.dog
            "Cat" -> R.drawable.cat
            "Bear" -> R.drawable.bear
            else -> R.drawable.rabbit
        }
        image.setImageResource(id)

        sharedPreferences = getSharedPreferences("AnimalRatingActivity", MODE_PRIVATE)
        val rating = sharedPreferences.getFloat(animalName, -1F)
        if (rating != -1F) {
            ratingBar.rating = rating
        } else {
            ratingBar.rating = 0F
        }
    }

    fun saveRating(view: View) {
        val editor = sharedPreferences.edit()
        editor.putFloat(animalName, ratingBar.rating)
        editor.putString("Recent", animalName)
        editor.apply()
        finish()
    }
}