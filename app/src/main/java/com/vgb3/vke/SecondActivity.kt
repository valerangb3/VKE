package com.vgb3.vke

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.vgb3.vke.ui.theme.VKETheme

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val text = "Data from first Activity: ${intent.getStringExtra(KEY_TEXT)?.isNotEmpty() ?: "No data"}"
        setContent {
            VKETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Text(text)
                    }
                }
            }
        }
    }

    companion object {
        const val KEY_TEXT = "FOR_SECOND_ACTIVITY"
    }
}