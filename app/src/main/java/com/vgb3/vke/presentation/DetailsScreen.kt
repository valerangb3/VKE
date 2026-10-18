package com.vgb3.vke.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Toolbar(
                onShareClick = {},
                onBackClick = {}
            )
        }
    ) { contentPadding ->
        DetailsContent(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        )
    }
}