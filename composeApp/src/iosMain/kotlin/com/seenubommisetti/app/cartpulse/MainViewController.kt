package com.seenubommisetti.app.cartpulse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { IOSPlaceholderScreen() }

@Composable
private fun IOSPlaceholderScreen() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "CartPulse shared shopping logic is ready.",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "The chat UI in this sample is implemented in Android with Jetpack Compose.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
