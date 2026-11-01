package com.example.airobotassistant

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast

class FloatingRobotService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: android.view.View

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // रोबोटची व्ह्यू तयार करा
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_robot, null)

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 100

        // रोबोटवर क्लिक केल्यावर व्हॉइस सुरू होईल
        val robotImage = floatingView.findViewById<ImageView>(R.id.robot_image_view)
        robotImage.setOnClickListener {
            Toast. मी ऐकतोय, बोला!", Toast.LENGTH_SHORT).show()
            // येथे Voice Recognition सुरू करा
            VoiceManager.startListening(this)
        }

        windowManager.addView(floatingView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized) windowManager.removeView(floatingView)
    }
}