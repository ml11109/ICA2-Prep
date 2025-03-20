package com.example.ica2_prep.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/*
SpeechRecognizer that records continuously instead of stopping when user stops speaking

In manifest:
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
In <application> in manifest:
<service
    android:name="android.speech.RecognitionService"
    android:permission="android.permission.BIND_SPEECH_RECOGNITION_SERVICE" />

In main screen:
var audioPermissionGranted by remember { mutableStateOf(false) }
SpeechRecognizerManager.RequestAudioPermission { audioPermissionGranted = true }

To create:
val speechRecognizerManager = remember {
    SpeechRecognizerManager(
        context = context,
        onStop = {
            isRunning = false
            Toast.makeText(context, "Recording stopped", Toast.LENGTH_SHORT).show()
        },
        onResult = { text = it }
    )
}

To start:
speechRecognizerManager.startListening()

To stop:
speechRecognizerManager.stopListening()
 */

class ContinuousSpeechRecognizerManager(
    private val context: Context,
    private val onStop: () -> Unit,
    private val onResult: (String) -> Unit
) {
    private var speechRecognizer: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private var savedText = ""
    private var newText = ""

    fun startListening() {
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000)
            }

            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}

                override fun onBeginningOfSpeech() {
                    if (savedText.isNotEmpty() && newText.isNotEmpty() && !newText.startsWith(" ")) {
                        savedText += " "
                    }
                    savedText += newText
                    newText = ""
                }

                override fun onResults(results: Bundle?) {
                    onStop()
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        newText = matches[0]
                        if (savedText.isNotEmpty() && newText.isNotEmpty() && !newText.startsWith(" ")) newText = " $newText"
                        onResult("$savedText$newText")
                    }
                }
            })
        }

        speechRecognizer?.startListening(recognizerIntent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
    }

    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    companion object {
        @Composable
        fun RequestAudioPermission(onPermissionGranted: () -> Unit) {
            val context = LocalContext.current
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    onPermissionGranted()
                } else {
                    Toast.makeText(context, "Audio permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(Unit) {
                launcher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
}