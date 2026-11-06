package com.vgb3.vke.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vgb3.vke.presentation.common.Toolbar
import com.vgb3.vke.ui.theme.VKETheme

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailsScreen(
        state = state,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
fun DetailsScreen(
    state: DetailsUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                onShareClick = {},
                onBackClick = onBackClick
            )
        }
    ) { contentPadding ->
        when (state) {
            DetailsUiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            is DetailsUiState.Content -> DetailsContent(
                appDetails = state.details,
                similarApps = state.similarApps,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .safeDrawingPadding()
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DetailsScreenPreview() {
    VKETheme {
        DetailsScreen(
            state = DetailsUiState.Content(previewAppDetails, previewSimilarApps),
            onBackClick = {},
        )
    }
}
