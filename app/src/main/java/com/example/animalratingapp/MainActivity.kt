package com.example.animalratingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var list: List<String>
    private lateinit var animalList: MutableList<String>
    private lateinit var animalAdapter: ArrayAdapter<String>
    private lateinit var animalListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = listOf("Dog", "Cat", "Bear", "Rabbit")
        animalList = mutableListOf("Dog", "Cat", "Bear", "Rabbit")
        animalAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, animalList)
        animalListView = findViewById(R.id.listView_animal)
        animalListView.adapter = animalAdapter

        animalListView.setOnItemClickListener { _, _, position, _ ->
            val myIntent = Intent(this, AnimalRatingActivity::class.java)
            myIntent.putExtra("animalName", list[position])
            startActivity(myIntent)
        }
    }

    override fun onStart() {
        super.onStart()

        val sharedPreference = getSharedPreferences("AnimalRatingActivity", MODE_PRIVATE)

        for ( (index, animalName) in list.withIndex()) {
            val rating = sharedPreference.getFloat(animalName, -1F)
            if (rating != -1F) {
                animalList[index] = "$animalName -- Rating: $rating"
            } else {
                animalList[index] = animalName
            }
        }
        animalAdapter.notifyDataSetChanged()

        val imageRecent = findViewById<ImageView>(R.id.image_recent)
        var textRecent = findViewById<TextView>(R.id.text_recent_animal)
        var ratingBarRecent = findViewById<RatingBar>(R.id.rating_Bar_Recent)

        val animalRecent = sharedPreference.getString("Recent", "")
        val id = when (animalRecent) {
            "Dog" -> R.drawable.dog
            "Cat" -> R.drawable.cat
            "Bear" -> R.drawable.bear
            else -> R.drawable.rabbit
        }
        imageRecent.setImageResource(id)
        textRecent.text = animalRecent
        ratingBarRecent.rating = sharedPreference.getFloat(animalRecent, -1F)


    }

}