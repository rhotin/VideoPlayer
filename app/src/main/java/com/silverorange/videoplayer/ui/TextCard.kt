package com.silverorange.videoplayer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.silverorange.videoplayer.MainViewModel

@Composable
fun TextCard(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        val data = viewModel.state.videoDto?.get(viewModel.libraryIndex)
        Text(text = "Title: " + (data?.title ?: "Unknown"))
        Text(text = "Author: " + (data?.author?.name ?: "Unknown"))
        Text(text = "Description: " + (data?.description ?: "Unavailable"))
    }
}