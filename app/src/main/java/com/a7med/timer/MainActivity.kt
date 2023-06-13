package com.a7med.timer

import android.media.tv.TvContract.BaseTvColumns
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toDuration
import kotlin.time.toDurationUnit

class MainActivity : AppCompatActivity() {
    var startTimeInMilli: Long = 25 * 60 * 1000
    var remainingTime: Long = startTimeInMilli
    var timer: CountDownTimer? = null
    var isTimerRunning: Boolean = false


    lateinit var titleTv: TextView
    lateinit var timerTv: TextView
    lateinit var startBtn: Button
    lateinit var resetBtn: ImageButton
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        startBtn.setOnClickListener {
            if (!isTimerRunning) {
                startTimer(startTimeInMilli)
                titleTv.text = resources.getText(R.string.keep_going)
            }
        }

        resetBtn.setOnClickListener {
            resetTimer()
        }

    }

    private fun initView() {
        titleTv = findViewById(R.id.title_tv)
        timerTv = findViewById(R.id.timer_tv)
        startBtn = findViewById(R.id.start_btn)
        resetBtn = findViewById(R.id.reset_btn)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun startTimer(startTime: Long) {
        timer = object : CountDownTimer(startTime, 1000) {
            override fun onTick(timeLeft: Long) {
                remainingTime = timeLeft
                updateTime()
                progressBar.progress = (remainingTime.toDouble() / startTimeInMilli.toDouble()).times(100).toInt()


            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "timer finished", Toast.LENGTH_SHORT).show()
                isTimerRunning = false
                progressBar.progress = 0
            }
        }.start()
        isTimerRunning = true

    }

    private fun resetTimer() {
        timer?.cancel()
        isTimerRunning = false
        remainingTime = startTimeInMilli
        timerTv.text = resources.getText(R.string.time)
        titleTv.text = resources.getText(R.string.take_pomodoro)
        progressBar.progress = 100
    }


    private fun updateTime() {
        val minutes = remainingTime.div(1000).div(60) // time in minutes
        val seconds = remainingTime.div(1000).mod(60) // time in seconds
        val time = String.format(Locale.US, "%02d:%02d", minutes, seconds)
        timerTv.text = time

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("remaining time", remainingTime)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedTime = savedInstanceState.getLong("remaining time")
        if (savedTime != startTimeInMilli) {
            Log.d("ahmed", "saved time :$savedTime")
            startTimer(savedTime)
        }
    }
}