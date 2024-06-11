package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible


class QuizQuestionsActivity : AppCompatActivity() , View.OnClickListener {

    private var mCurrentPosition : Int  = 1
    private var mQuestionsList:ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int  = 0

    private var progressCircular : ProgressBar? = null

    private var progressBar : ProgressBar? = null

    private var tvProgress: TextView? = null
    private var tvQuestion: TextView? = null
    private var tvImage: ImageView? = null

    private var tvOption1 : TextView? = null
    private var tvOption2 : TextView? = null
    private var tvOption3 : TextView? = null
    private var tvOption4 : TextView? = null

    private var correctAnswers = 0
    private var btnSubmit : Button? = null

    private var etName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        etName = intent.getStringExtra(Constants.userName).toString()
        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tv_progress)
        tvQuestion = findViewById(R.id.tv_question)
        tvImage = findViewById(R.id.iv_image)

        tvOption1= findViewById(R.id.tv_option1)
        tvOption2= findViewById(R.id.tv_option2)
        tvOption3= findViewById(R.id.tv_option3)
        tvOption4= findViewById(R.id.tv_option4)

        btnSubmit = findViewById(R.id.btnTwo)

        tvOption1?.setOnClickListener(this)
        tvOption2?.setOnClickListener(this)
        tvOption3?.setOnClickListener(this)
        tvOption4?.setOnClickListener(this)


        btnSubmit?.setOnClickListener(this)
        progressCircular = findViewById(R.id.progress_circular)

        mQuestionsList = Constants.getQuestions()

        setQuestion()


    }

    private fun setQuestion() {
        defaultOptionsView()

        val question: Question = mQuestionsList!![mCurrentPosition - 1]
        tvImage?.setImageResource(question.image)
        progressBar?.progress = mCurrentPosition
        tvProgress?.text = "$mCurrentPosition / ${progressBar?.max}"
        tvQuestion?.text = question.question
        tvOption1?.text = question.option1
        tvOption2?.text = question.option2
        tvOption3?.text = question.option3
        tvOption4?.text = question.option4

        if(mCurrentPosition == mQuestionsList!!.size){
            btnSubmit?.text = "FINISH"
        }else {
            btnSubmit?.text = "SUBMIT"
        }
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()

        progressCircular?.isVisible = false
        btnSubmit?.isVisible = true

        tvOption1?.let {
            options.add(0,it)
        }
        tvOption2?.let {
            options.add(1,it)
        }
        tvOption3?.let {
            options.add(2,it)
        }
        tvOption4?.let {
            options.add(3,it)
        }
        tvOption1?.isEnabled =true
        tvOption2?.isEnabled =true
        tvOption3?.isEnabled =true
        tvOption4?.isEnabled =true
        for (option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface =  Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }
    private fun selectedOptionView(tv:TextView , selectedOptionNum : Int){
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface,Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option1 -> {
                tvOption1?.let {
                    selectedOptionView(it, 1)
                }
            }

            R.id.tv_option2 -> {
                tvOption2?.let {
                    selectedOptionView(it, 2)
                }
            }

            R.id.tv_option3 -> {
                tvOption3?.let {
                    selectedOptionView(it, 3)
                }
            }

            R.id.tv_option4 -> {
                tvOption4?.let {
                    selectedOptionView(it, 4)
                }
            }

            R.id.btnTwo -> {
                btnSubmit?.isEnabled = true
                if (mSelectedOptionPosition == 0) {


                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {

                            setQuestion()
                        }

                        else -> {
                            navigateToResultActivity()
                        }
                    }
                } else {
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        answerView(mSelectedOptionPosition, R.drawable.correct_option_border_bg)
                        correctAnswers++
                    }

                    btnSubmit?.isVisible = false


                    tvOption1?.isEnabled = false
                    tvOption2?.isEnabled = false
                    tvOption3?.isEnabled = false
                    tvOption4?.isEnabled = false
                    mSelectedOptionPosition = 0


                    progressCircular?.isVisible = true
                    startProgressTimer()

                }
            }
        }
    }

    private fun startProgressTimer(){
        progressCircular?.progress =100
        val handler = Handler(Looper.getMainLooper())
        val delay = 20L
        val totalDuration = 2000L
        val step = 100 /(totalDuration/delay)

        var progress= 100
        val runnable = object : Runnable{
            override fun run() {
                progress -= step.toInt()
                progressCircular?.progress= progress
                if(progress>0){
                    handler.postDelayed(this,delay)
                }else{
                    if (mCurrentPosition < mQuestionsList!!.size) {
                        mCurrentPosition++
                        setQuestion()
                    } else {
                        navigateToResultActivity()
                    }
                }
            }
        }
        handler.post(runnable)
    }

    private fun navigateToResultActivity() {
        Toast.makeText(this, "Congratulations! You have made it to the end.", Toast.LENGTH_LONG).show()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(Constants.userName, etName)
        intent.putExtra(Constants.total_correct, correctAnswers)
        intent.putExtra(Constants.total_questions, mQuestionsList?.size)
        startActivity(intent)
        finish()
    }


    private fun answerView(answer:Int , drawableView:Int) {
        when (answer) {
            1-> {
                tvOption1?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            2-> {
                tvOption2?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            3-> {
                tvOption3?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            4-> {
                tvOption4?.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }
    }
}


