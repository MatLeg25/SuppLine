package io.suppline.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import io.suppline.presentation.SuppLineViewModelContract

@Preview
@Composable
fun AddEditSupplementModal(
    viewModel: SuppLineViewModelContract = hiltViewModel(),
    setShowStats: (Boolean) -> Unit = {},
) {
    Dialog(onDismissRequest = { setShowStats(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.DarkGray,
        ) {
            AddEditItem(Modifier, viewModel)
        }
    }
}