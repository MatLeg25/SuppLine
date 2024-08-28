package io.suppline.presentation.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.suppline.R
import io.suppline.domain.models.Supplement
import java.util.Locale

@Composable
fun ConsumeTime(
    modifier: Modifier = Modifier,
    isEditable: Boolean = true,
    length: Dp = 100.dp,
    model: Supplement,
    setConsumedTime: (supplement: Supplement, hourDelta: Int, minDelta: Int) -> Unit
) {
    val height = length / 2
    val time = model.timeToConsume


    Row(
        modifier = modifier.height(if (isEditable) height.times(4) else length),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HalfTablet(
            time = time.hour,
            isLeftSide = true,
            isEditable = isEditable,
            model = model,
            setConsumedTime = setConsumedTime
        )
        Box(contentAlignment = Alignment.Center) {
            VerticalDivider(modifier = Modifier.height(height))
            Text(text = ":")
        }
        HalfTablet(
            time = time.minute,
            isLeftSide = false,
            isEditable = isEditable,
            model = model,
            setConsumedTime = setConsumedTime
        )
    }

}

@Composable
fun HalfTablet(
    modifier: Modifier = Modifier,
    isEditable: Boolean = true,
    isLeftSide: Boolean = true,
    time: Int = 12,
    height: Dp = 50.dp,
    model: Supplement,
    setConsumedTime: (model: Supplement, hourDelta: Int, minDelta: Int) -> Unit
) {
    val shape: RoundedCornerShape
    val backgroundColor: Color
    val fontColor: Color
    val alignment: Alignment

    if (isLeftSide) {
        shape = RoundedCornerShape(height / 2, 0.dp, 0.dp, height / 2)
        backgroundColor = MaterialTheme.colorScheme.outlineVariant
        fontColor = MaterialTheme.colorScheme.onPrimary
        alignment = Alignment.CenterEnd
    } else {
        shape = RoundedCornerShape(0.dp, height / 2, height / 2, 0.dp)
        backgroundColor = MaterialTheme.colorScheme.inversePrimary
        fontColor = MaterialTheme.colorScheme.onPrimary
        alignment = Alignment.CenterStart
    }

    Column(modifier = modifier) {
        AnimatedVisibility(visible = isEditable) {
            IconButton(onClick = {
                if (isLeftSide) setConsumedTime(model, 1, 0) else setConsumedTime(model, 0, 1)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = stringResource(id = R.string.keyboard_arrow_up)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(height)
                .clip(shape = shape)
                .background(color = backgroundColor),
            contentAlignment = alignment
        ) {
            Text(
                text = String.format(Locale.ROOT, "%02d", time),
                color = fontColor,
                fontSize = (height / 2).value.sp
            )
        }
        AnimatedVisibility(visible = isEditable) {
            IconButton(onClick = {
                if (isLeftSide) setConsumedTime(model, -1, 0) else setConsumedTime(model, 0, -1)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.keyboard_arrow_up)
                )
            }
        }

    }

}