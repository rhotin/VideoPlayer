package com.silverorange.videoplayer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.RepeatModeUtil
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.silverorange.videoplayer.MainViewModel
import com.silverorange.videoplayer.R

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayerCard(viewModel: MainViewModel) {
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            AndroidView(
                factory = { context ->
                    PlayerView(context).also {
                        it.player = viewModel.player
                        it.useController = false
                        it.setRepeatToggleModes(RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE)
                    }
                },
                update = {
                    when (lifecycle) {
                        Lifecycle.Event.ON_RESUME -> {
                            it.onPause()
                            it.player?.pause()
                        }

                        Lifecycle.Event.ON_PAUSE -> {
                            it.onResume()
                        }

                        else -> Unit
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )
            PlayerControls(viewModel)
        }
    }
}

@Composable
fun PlayerControls(viewModel: MainViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        //previous button
        IconButton(
            modifier = Modifier
                .background(Color.White, shape = CircleShape)
                .size(40.dp)
                .align(Alignment.CenterVertically),
            onClick = { viewModel.playPreviousVideo() }) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.previous),
                alpha = if (viewModel.hasPreviousVideo) {
                    1f
                } else {
                    0.3f
                },
                contentDescription = "Previous"
            )
        }
        //play/pause button
        IconButton(
            modifier = Modifier
                .background(Color.White, shape = CircleShape)
                .size(60.dp)
                .align(Alignment.CenterVertically),
            onClick = { viewModel.playVideo() }) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = if (viewModel.isPlaying) {
                    painterResource(id = R.drawable.pause)
                } else {
                    painterResource(id = R.drawable.play)
                },
                contentDescription = "Play/Pause"
            )
        }
        //next button
        IconButton(
            modifier = Modifier
                .background(Color.White, shape = CircleShape)
                .size(40.dp)
                .align(Alignment.CenterVertically),
            onClick = { viewModel.playNextVideo() }) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.next),
                alpha = if (viewModel.hasNextVideo) {
                    1f
                } else {
                    0.3f
                },
                contentDescription = "Next"
            )
        }
    }
}
