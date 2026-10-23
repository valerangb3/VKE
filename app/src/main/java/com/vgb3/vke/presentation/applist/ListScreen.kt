package com.vgb3.vke.presentation.applist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vgb3.vke.presentation.common.Toolbar

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    onItemClick: (appId: Long) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                onBackClick = {}
            )
        }
    ) { contentPadding ->
        ListContent(
            onItemClick = onItemClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        )
    }
}