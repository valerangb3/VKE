package com.vgb3.vke.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vgb3.vke.R
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
        onRetryClick = viewModel::loadDetails,
        modifier = modifier,
    )
}

@Composable
fun DetailsScreen(
    state: DetailsUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                onShareClick = {},
                onBackClick = onBackClick,
            )
        },
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

            DetailsUiState.Error -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.app_details_load_error),
                    textAlign = TextAlign.Center,
                )
                Button(onClick = onRetryClick) {
                    Text(stringResource(R.string.app_list_retry))
                }
            }

            is DetailsUiState.Content -> DetailsContent(
                appDetails = state.details,
                similarApps = state.similarApps,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .safeDrawingPadding(),
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
