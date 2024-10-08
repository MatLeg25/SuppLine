package io.suppline.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.suppline.R

@Composable
fun SelectionButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val animatedButtonColor = animateColorAsState(
        targetValue = if (isSelected) Color.Green else Color.LightGray,
        animationSpec = tween(1000, 0, LinearEasing),
        label = stringResource(id = R.string.app_name)
    )

    Button(
        modifier = modifier,
        colors = ButtonColors(
            containerColor = animatedButtonColor.value,
            contentColor = animatedButtonColor.value,
            disabledContainerColor = animatedButtonColor.value,
            disabledContentColor = animatedButtonColor.value,
        ),
        onClick = { onClick() }) {

        AnimatedVisibility(visible = isSelected) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(id = R.string.app_name),
                tint = Color.Blue
            )
        }

    }

}