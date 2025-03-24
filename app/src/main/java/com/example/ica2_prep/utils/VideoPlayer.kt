package com.example.ica2_prep.utils

import android.app.PictureInPictureParams
import android.content.Context
import android.net.Uri
import android.util.Rational
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/*
// In <activity> in manifest
android:supportsPictureInPicture="true"
android:configChanges="screenSize|smallestScreenSize|screenLayout|orientation"

// VideoViewPlayer (basic UI)
VideoViewPlayer(R.raw.video, Modifier.fillMaxWidth().aspectRatio(16f / 9f))

// ExoVideoPlayer (more advanced UI)
ExoVideoPlayer(R.raw.video, Modifier.fillMaxWidth().aspectRatio(16f / 9f))

// PiP
enterPipMode(context)
 */

@Composable
fun VideoViewPlayer(videoUri: String, modifier: Modifier) {
    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                setVideoURI(Uri.parse(videoUri))
                setOnPreparedListener { start() }
                setMediaController(android.widget.MediaController(ctx).apply { setAnchorView(this@apply) })
            }
        },
        modifier = modifier
    )
}

@Composable
fun VideoViewPlayer(videoResId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    VideoViewPlayer(Uri.parse("android.resource://${context.packageName}/$videoResId").toString(), modifier)
}

@Composable
fun ExoVideoPlayer(videoUri: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var shouldEnterPipMode by remember { mutableStateOf(false) }

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
            prepare()
            playWhenReady = true
        }
    }

    // Show Video using AndroidView
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true  // Show playback controls
            }
        },
        modifier = modifier
    )

    // Release player when not needed
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    exoPlayer.addListener(object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            shouldEnterPipMode = isPlaying
        }
    })
}

@Composable
fun ExoVideoPlayer(videoResId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    ExoVideoPlayer(Uri.parse("android.resource://${context.packageName}/$videoResId").toString(), modifier)
}

fun enterPipMode(context: Context) {
    val activity = context as? ComponentActivity
    val params = PictureInPictureParams.Builder()
        .setAspectRatio(Rational(16, 9)) // Adjust aspect ratio
        .build()
    activity?.enterPictureInPictureMode(params)
}
