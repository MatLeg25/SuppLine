package io.suppline.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.suppline.R

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().height(48.dp).background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 96.sp,
            style = MaterialTheme.typography.titleLarge
        )
    }
}