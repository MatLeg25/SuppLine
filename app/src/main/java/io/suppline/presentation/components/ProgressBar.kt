package io.suppline.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
        AnimatedLinearProgressIndicator(modifier, progress)
    }

}

@Composable
fun AnimatedLinearProgressIndicator(modifier: Modifier, progress: Float) {
    val progressAnimDuration = 1_500
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing),
        label = "",
    )

    LinearProgressIndicator(
        color = Color.Blue,
        trackColor = Color.Black,
        progress = { progressAnimation },
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
    )

}