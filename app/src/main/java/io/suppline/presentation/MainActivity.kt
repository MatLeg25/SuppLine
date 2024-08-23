package io.suppline.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import io.suppline.presentation.components.Logo
import io.suppline.presentation.components.ProgressBar
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
                    Column(
                        modifier = Modifier.padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Logo()
                        Spacer(modifier = Modifier.weight(0.1f))
                        LazyColumn(modifier = Modifier.weight(0.9f)) {

                            itemsIndexed(state.supplementsMap.keys.toList()) { index, item ->
                                if (index == 0) HorizontalDivider()
                                Supplement(
                                    modifier = Modifier,
                                    model = item,
                                    isConsumed = state.supplementsMap[item] ?: false,
                                    onClick = { viewModel.toggleConsumed(item) }
                                )
                                HorizontalDivider()
                            }

                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                ProgressBar(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(24.dp),
                                    progress = state.progress
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


