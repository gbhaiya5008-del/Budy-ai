package com.example.airobotassistant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object VoiceManager {

    fun startListening(context: Context) {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "mr-IN") // मराठी किंवा hi-IN / en-US
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val userSpeech = matches?.get(0) ?: ""
                
                // बोलणे ChatGPT API ला पाठवा
                askChatGPT(context, userSpeech)
            }
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer.startListening(intent)
    }

    private fun askChatGPT(context: Context, command: String) {
        val apiKey = "YOUR_OPENAI_API_KEY" // तुमची OpenAI Key इथे टाका
        val client = OkHttpClient()

        // ChatGPT ला सांगा की त्याला फोन कंट्रोल करायचा आहे
        val prompt = "User command: '$command'. If user wants to open an app like WhatsApp or YouTube, reply strictly in JSON format like: {\"action\": \"OPEN_APP\", \"package\": \"com.whatsapp\"}"

        val jsonBody = JSONObject().apply {
            put("model", "gpt-4o-mini")
            put("messages", org.json.JSONArray().put(JSONObject().put("role", "user").put("content", prompt)))
        }

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                // इथून मिळालेला रिझल्ट PhoneControllerService ला पाठवून मोबाईलमध्ये काम केले जाईल
                PhoneControllerService.instance?.executeAction(responseData)
            }
            override fun onFailure(call: Call, e: IOException) {}
        })
    }
}