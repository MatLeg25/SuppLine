package io.suppline.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime

@Composable
@Preview
fun ConsumeTime(
    modifier: Modifier = Modifier,
    time: LocalDateTime = LocalDateTime.now(),
    length: Dp = 100.dp
) {
    val hour = time.hour
    val minute = time.minute
    val size = length / 2
    Row(Modifier.height(size)) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape = RoundedCornerShape(size / 2, 0.dp, 0.dp, size / 2))
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = hour.toString(),
                fontSize = (size / 2).value.sp
            )
        }
        Box(contentAlignment = Alignment.Center) {
            VerticalDivider()
            Text(text = ":")
        }
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape = RoundedCornerShape(0.dp, size / 2, size / 2, 0.dp))
                .background(color = MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = minute.toString(),
                fontSize = (size / 2).value.sp
            )
        }
    }
}