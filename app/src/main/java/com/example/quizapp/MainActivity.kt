package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_main)

        val btn : Button = findViewById(R.id.btnOne)
        val etName : AppCompatEditText = findViewById(R.id.et_name)
        btn.setOnClickListener {
            if (etName.text.toString().isNotEmpty()){
                val intent = Intent(this,QuizQuestionsActivity::class.java)
                intent.putExtra(Constants.userName,etName.text.toString())
                startActivity(intent)

            }else{
                Toast.makeText(this,"Please enter your name" ,Toast.LENGTH_LONG).show()
            }
        }

    }
}