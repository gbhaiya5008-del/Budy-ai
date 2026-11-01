package com.example.airobotassistant

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import org.json.JSONObject

class PhoneControllerService : AccessibilityService() {

    companion object {
        var instance: PhoneControllerService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    fun executeAction(jsonResponse: String?) {
        try {
            // ChatGPT कडून आलेला आदेश वाचणे
            val json = JSONObject(jsonResponse ?: "")
            val action = json.optString("action")

            if (action == "OPEN_APP") {
                val packageName = json.optString("package")
                val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                launchIntent?.let {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
}