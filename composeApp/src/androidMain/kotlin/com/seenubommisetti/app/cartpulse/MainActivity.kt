package com.seenubommisetti.app.cartpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seenubommisetti.app.cartpulse.ui.ChatRoute
import com.seenubommisetti.app.cartpulse.viewmodel.ChatViewModel
import com.seenubommisetti.app.cartpulse.viewmodel.ChatViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val factory = ChatViewModelFactory(apiKey = BuildConfig.GEMINI_API_KEY)

        setContent {
            val chatViewModel: ChatViewModel = viewModel(factory = factory)
            ChatRoute(viewModel = chatViewModel)
        }
    }
}
