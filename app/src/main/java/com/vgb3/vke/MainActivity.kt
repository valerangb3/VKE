package com.vgb3.vke

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgb3.vke.navigation.AppNav
import com.vgb3.vke.presentation.applist.ListScreen
import com.vgb3.vke.ui.theme.VKETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKETheme {
                App()
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    AppNav()
    /*ListScreen(
        modifier = modifier
    )*/
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VKETheme {
        ListScreen()
    }
}