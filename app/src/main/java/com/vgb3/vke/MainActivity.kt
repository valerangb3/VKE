package com.vgb3.vke

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vgb3.vke.navigation.AppNav
import com.vgb3.vke.ui.theme.VKETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKETheme {
                AppNav()
            }
        }
    }
}
