package com.example.animalratingapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    private lateinit var list: List<String>
    private lateinit var animalList: MutableList<String>
    private lateinit var animalAdapter: ArrayAdapter<String>
    private lateinit var animalListView: ListView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var constraintLayoutRecent: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = listOf("Dog", "Cat", "Bear", "Rabbit")
        animalList = list.toMutableList()
        animalAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, animalList)
        animalListView = findViewById(R.id.listView_animal)
        animalListView.adapter = animalAdapter

        animalListView.setOnItemClickListener { _, _, position, _ ->
            val myIntent = Intent(this, AnimalRatingActivity::class.java)
            myIntent.putExtra("animalName", list[position])
            startActivity(myIntent)
        }

        constraintLayoutRecent = findViewById<ConstraintLayout>(R.id.constraintLayout_recent)
    }

    override fun onStart() {
        super.onStart()

        sharedPreferences = getSharedPreferences("AnimalRatingActivity", MODE_PRIVATE)

        for ( (index, animalName) in list.withIndex()) {
            val rating = sharedPreferences.getFloat(animalName, -1F)
            if (rating != -1F) {
                animalList[index] = "$animalName -- Rating: $rating/5"
            } else {
                animalList[index] = animalName
            }
        }
        animalAdapter.notifyDataSetChanged()

        val imageRecent = findViewById<ImageView>(R.id.image_recent)
        val textRecent = findViewById<TextView>(R.id.text_recent_animal)
        val ratingBarRecent = findViewById<RatingBar>(R.id.rating_Bar_Recent)

        val animalRecent = sharedPreferences.getString("Recent", "")
        if (animalRecent != "") {
            val id = when (animalRecent) {
                "Dog" -> R.drawable.dog
                "Cat" -> R.drawable.cat
                "Bear" -> R.drawable.bear
                else -> R.drawable.rabbit
            }
            imageRecent.setImageResource(id)
            textRecent.text = animalRecent
            ratingBarRecent.rating = sharedPreferences.getFloat(animalRecent, -1F)
            constraintLayoutRecent.visibility = View.VISIBLE
        }
    }

    fun clearRatings(view: View) {
        constraintLayoutRecent.visibility = View.INVISIBLE

        sharedPreferences = getSharedPreferences("AnimalRatingActivity", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        onStart()
    }
}