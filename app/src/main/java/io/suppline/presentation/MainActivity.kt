package io.suppline.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.suppline.presentation.components.Supplement
import io.suppline.presentation.ui.theme.SuppLineTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SuppLineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuppLineTheme {

                val state = viewModel.state.value

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier.padding(innerPadding),
                    ) {

                        items(state.supplementsMap.keys.toList()) {
                            Supplement(
                                modifier = Modifier.padding(innerPadding),
                                model = it,
                                isConsumed = state.supplementsMap[it] ?: false,
                                onClick = { viewModel.toggleConsumed(it) }
                            )
                        }

                        item {
                            LinearProgressIndicator(progress = { state.progress })
                        }
                    }

                }
            }
        }
    }
}


