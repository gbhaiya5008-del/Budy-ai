package com.example.airobotassistant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class StandByActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // स्क्रीन कायम चालू आणि आडवी (Landscape) ठेवा
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Full Screen (Status bar & Navigation bar लपवा)
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )

        setContentView(R.layout.activity_standby)

        setupCalendar()
        setupBatteryIndicator()
    }

    private fun setupCalendar() {
        val calendar = Calendar.getInstance()
        val tvMonth = findViewById<TextView>(R.id.tvMonthName)
        val grid = findViewById<GridLayout>(R.id.calendarGrid)

        // महिन्याचे नाव सेट करा (उदा. NOVEMBER)
        val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
        tvMonth.text = monthFormat.format(calendar.time).uppercase()

        val todayDate = calendar.get(Calendar.DAY_OF_MONTH)
        val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0 for Sunday

        grid.removeAllViews()

        // महिन्याच्या सुरुवातीची रिकामी जागा
        for (i in 0 until firstDayOfWeek) {
            val emptyTv = TextView(this)
            grid.addView(emptyTv)
        }

        // सर्व तारखा भरणे
        for (day in 1..totalDays) {
            val dayTv = TextView(this).apply {
                text = day.toString()
                textSize = 15f
                gravity = Gravity.CENTER
                setPadding(12, 10, 12, 10)

                if (day == todayDate) {
                    // आजच्या तारखेला लाल गोल हायलाइट करा (Image 2 मधील 11 तारखेसारखे)
                    setTextColor(Color.WHITE)
                    setBackgroundResource(R.drawable.bg_today_circle)
                } else {
                    setTextColor(Color.parseColor("#FF453A"))
                }
            }
            grid.addView(dayTv)
        }
    }

    private fun setupBatteryIndicator() {
        val tvBattery = findViewById<TextView>(R.id.tvBattery)
        val batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                tvBattery.text = "$level"
            }
        }
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }
}