package com.example.ica2_prep.utils

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/*
// To create
val tts = rememberTextToSpeech()

// To speak
speakText(tts, "text to speak")

// To stop
tts.stop()
 */

@Composable
fun rememberTextToSpeech(): TextToSpeech {
    val context = LocalContext.current
    lateinit var tts: TextToSpeech

    tts = remember { TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    } }

    DisposableEffect(context) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    return tts
}

fun speakText(tts: TextToSpeech, text: String) {
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
}
