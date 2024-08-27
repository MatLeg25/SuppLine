package io.suppline.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import io.suppline.SuppLineApp
import io.suppline.presentation.components.DefaultSections
import io.suppline.presentation.components.GroupByTime
import io.suppline.presentation.components.Logo
import io.suppline.presentation.components.ProgressBar
import io.suppline.presentation.components.TurnOnNotificationBtn
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
                        Logo(modifier = Modifier.clickable {
                            (application as SuppLineApp).showNotification()
                        })
                        Spacer(modifier = Modifier.weight(0.1f))
                        TurnOnNotificationBtn()
                        Spacer(modifier = Modifier.weight(0.1f))
                        if (state.groupSectionsByTime) GroupByTime(viewModel = viewModel)
                        else DefaultSections(viewModel = viewModel)
                        Spacer(modifier = Modifier.height(24.dp))
                        ProgressBar(
                            modifier = Modifier.fillMaxWidth().height(24.dp),
                            progress = state.progress
                        )
                    }
                }

            }
        }
    }
}


