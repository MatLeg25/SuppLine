package io.suppline.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(text = text)
    }
}