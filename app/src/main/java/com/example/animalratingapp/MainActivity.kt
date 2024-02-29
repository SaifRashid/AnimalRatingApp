package com.example.animalratingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animalList = arrayListOf("Dog", "Cat", "Bear", "Rabbit")
        val animalAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, animalList)
        val animalListView = findViewById<ListView>(R.id.listView_animal)
        animalListView.adapter = animalAdapter
    }
}