package com.example.animalratingapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    private lateinit var list: List<String>
    private lateinit var animalList: MutableList<String>
    private lateinit var animalAdapter: ArrayAdapter<String>
    private lateinit var animalListView: ListView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var constraintLayoutRecent: ConstraintLayout
    private lateinit var imageRecent: ImageView
    private lateinit var textRecent: TextView
    private lateinit var ratingBarRecent: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = listOf("Dog", "Cat", "Bear", "Rabbit")

        animalList = list.toMutableList()
        animalAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, animalList)
        animalListView = findViewById(R.id.listView_animal)
        animalListView.adapter = animalAdapter

        sharedPreferences = getSharedPreferences("AnimalRatingActivity", MODE_PRIVATE)

        constraintLayoutRecent = findViewById(R.id.constraintLayout_recent)
        imageRecent = findViewById(R.id.image_recent)
        textRecent = findViewById(R.id.text_recent_animal)
        ratingBarRecent = findViewById(R.id.rating_Bar_Recent)
    }

    override fun onStart() {
        super.onStart()

        val sortedList = mutableListOf<String>()
        val sortedRatings = mutableMapOf<String, Float>()

        for (animalName in list) {
            val rating = sharedPreferences.getFloat(animalName, -1F)
                sortedRatings[animalName] = rating
        }

        // Sorts ratings, if they are the same then sorts alphabetically
        val result = sortedRatings.toList()
            .sortedByDescending { (_, value) -> value }
            .groupBy({ (_, value) -> value }, { (key, _) -> key })
            .flatMap { it.value.sorted() }
            .toMutableList()
        sortedList.addAll(result)

        // Maps animalList to the sortedList
        for ( (index, animalName) in sortedList.withIndex()) {
            val rating = sortedRatings[animalName]
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
        Toast.makeText(this, "All ratings have been cleared!", Toast.LENGTH_SHORT).show()
    }
}