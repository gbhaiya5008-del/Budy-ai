// जेव्हा तुम्ही आवाजाने सांगाल:
if (userSpeech.contains("clock", ignoreCase = true) || 
    userSpeech.contains("calendar", ignoreCase = true) || 
    userSpeech.contains("कॅलेंडर", ignoreCase = true) || 
    userSpeech.contains("घड्याळ", ignoreCase = true)) {

    // लगेच ही StandBy स्क्रीन उघडा
    val intent = Intent(context, StandByActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}