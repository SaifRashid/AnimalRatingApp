package com.example.animalratingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView

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

        animalListView.setOnItemClickListener { parent, view, position, id ->
            val myIntent = Intent(this, AnimalRatingActivity::class.java)
            myIntent.putExtra("animalName", list[position])
            startActivity(myIntent)
        }
    }

    override fun onStart() {
        super.onStart()

        val sharedPreference = getPreferences(MODE_PRIVATE)

        for ( (index, animalName) in list.withIndex()) {
            val rating = sharedPreference.getFloat(animalName, -1F)
            Log.i("MainActivity", "$animalName : $rating")
            if (rating != -1F) {
                animalList[index] = "$animalName -- Rating: $rating"
            } else {
                animalList[index] = "$animalName $rating"
            }
        }
        animalAdapter.notifyDataSetChanged()
    }

}