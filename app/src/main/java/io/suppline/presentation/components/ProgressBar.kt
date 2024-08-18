package io.suppline.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun ProgressBar(
    modifier: Modifier = Modifier,
    progress: Float = 0.5f
) {
    val progressPercent = (progress * 100).toInt().toString() + "%"
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = progressPercent)
        LinearProgressIndicator(progress = { progress })
    }

}