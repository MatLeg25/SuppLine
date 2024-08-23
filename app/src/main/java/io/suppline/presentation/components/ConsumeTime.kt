package io.suppline.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.suppline.domain.models.Supplement
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun ConsumeTime(
    modifier: Modifier = Modifier,
    isEditable: Boolean = true,
    length: Dp = 100.dp,
    model: Supplement,
    setConsumedTime: (supplement: Supplement, hour: Int, min: Int) -> Unit
) {
    val height = length / 2
    val time = model.timeToConsume

    fun setTime(h: Int, min: Int) {
        setConsumedTime(model, time.hour + h, time.minute + min)
    }


    Row(
        modifier = modifier.height(if (isEditable) height.times(4) else length),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HalfTablet(time = time.hour, isLeftSide = true, isEditable = isEditable, setConsumedTime = ::setTime)
        Box(contentAlignment = Alignment.Center) {
            VerticalDivider(modifier = Modifier.height(height))
            Text(text = ":")
        }
        HalfTablet(time = time.minute, isLeftSide = false, isEditable = isEditable, setConsumedTime = ::setTime)
    }

}

@Composable
fun HalfTablet(
    modifier: Modifier = Modifier,
    isEditable: Boolean = true,
    isLeftSide: Boolean = true,
    time: Int = 12,
    height: Dp = 50.dp,
    setConsumedTime: (hour: Int, min: Int) -> Unit
) {
    val shape: RoundedCornerShape
    val color: Color
    val alignment: Alignment

    if (isLeftSide) {
        shape = RoundedCornerShape(height / 2, 0.dp, 0.dp, height / 2)
        color = MaterialTheme.colorScheme.primary
        alignment = Alignment.CenterEnd
    }  else {
        shape = RoundedCornerShape(0.dp, height / 2, height / 2, 0.dp)
        color = MaterialTheme.colorScheme.secondary
        alignment = Alignment.CenterStart
    }

    Column(modifier = modifier) {
        if (isEditable) {
            IconButton(onClick = {
                if (isLeftSide) setConsumedTime(1, 0) else setConsumedTime(0, 1)
            }) {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "KeyboardArrowUp")
            }
        }
        Box(
            modifier = Modifier
                .size(height)
                .clip(shape = shape)
                .background(color = color),
            contentAlignment = alignment
        ) {
            Text(
                text = time.toString(),
                fontSize = (height / 2).value.sp
            )
        }
        if (isEditable) {
            IconButton(onClick = {
                if (isLeftSide) setConsumedTime(-1, 0) else setConsumedTime(0, -1)
            }) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "KeyboardArrowDown")
            }
        }

    }

}