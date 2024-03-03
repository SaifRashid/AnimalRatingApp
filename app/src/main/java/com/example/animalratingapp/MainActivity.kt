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

        sharedPreferences = getSharedPreferences("AnimalRatingActivity", MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()

        var sortedList = mutableListOf<String>()
        val sortedRatings = mutableMapOf<String, Float>()

        // Checks if all items have a rating, if not sorts list alphabetically
        for (animalName in list) {
            val rating = sharedPreferences.getFloat(animalName, -1F)
            if (rating == -1F) {
                sortedList = list.sorted().toMutableList()
                break
            } else {
                sortedRatings[animalName] = rating
            }
        }

        // Sorts ratings, if they are the same then sorts alphabetically
        if (sortedRatings.size == list.size) {
            val result = sortedRatings.toList()
                .sortedByDescending { (_, value) -> value }
                .groupBy({ (_, value) -> value }, { (key, _) -> key })
                .flatMap { it.value.sorted() }
                .toMutableList()
            sortedList.clear()
            sortedList.addAll(result)
        }

        // Maps animalList to the sortedList
        for ( (index, animalName) in sortedList.withIndex()) {
            val rating = sharedPreferences.getFloat(animalName, -1F)
            if (rating != -1F) {
                animalList[index] = "$animalName -- Rating: $rating/5"
            } else {
                animalList[index] = animalName
            }
        }
        animalAdapter.notifyDataSetChanged()

        animalListView.setOnItemClickListener { _, _, position, _ ->
            val myIntent = Intent(this, AnimalRatingActivity::class.java)
            myIntent.putExtra("animalName", sortedList[position])
            startActivity(myIntent)
        }

        // Recently rated animal
        constraintLayoutRecent = findViewById<ConstraintLayout>(R.id.constraintLayout_recent)
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

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        onStart()
    }
}