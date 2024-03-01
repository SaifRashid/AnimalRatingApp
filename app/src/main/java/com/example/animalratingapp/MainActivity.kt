package com.example.animalratingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    private lateinit var animalList: ArrayList<String>
    private lateinit var animalAdapter: ArrayAdapter<String>
    private lateinit var animalListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the list, adapter, and listView
        animalList = arrayListOf("Dog", "Cat", "Bear", "Rabbit")
        animalAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, animalList)
        animalListView = findViewById(R.id.listView_animal)
        animalListView.adapter = animalAdapter
    }

    override fun onStart() {
        super.onStart()

        val sharedPreference = getPreferences(MODE_PRIVATE)

        val dictionary = sharedPreference

        //
        animalAdapter.notifyDataSetChanged()
    }
}