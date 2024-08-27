package io.suppline.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Section(
    modifier: Modifier = Modifier,
    header: String = "12:00",
    content: @Composable () -> Unit
) {
    val lineColor = MaterialTheme.colorScheme.inversePrimary

    AnimatedVisibility(
        modifier = modifier,
        visible = true
    ) {
        Column {
            Row(
                modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(0.3f), color = lineColor)
                Text(text = header)
                HorizontalDivider(modifier = Modifier.weight(0.3f), color = lineColor)
            }
            content()
        }

    }
}