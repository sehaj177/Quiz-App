package com.example.quizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity


class ResultActivity : AppCompatActivity() {

    private var etName : String = ""
    private var correctAnswers : Int = 0
    private var totalQuestions : Int = 0


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvName : TextView = findViewById(R.id.etName)
        val score : TextView = findViewById(R.id.score)
        val btnFinish : Button = findViewById(R.id.finish)

        etName = intent.getStringExtra(Constants.userName).toString()
        correctAnswers = intent.getIntExtra(Constants.total_correct,0)
        totalQuestions = intent.getIntExtra(Constants.total_questions,0)

        tvName?.text= etName
        score?.text= "Your Score is ${correctAnswers} out of ${totalQuestions}"

        btnFinish.setOnClickListener(){
            startActivity(Intent(this,MainActivity::class.java))
        }

     }




}