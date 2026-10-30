package com.vgb3.vke.presentation.applist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.vgb3.vke.R
import com.vgb3.vke.presentation.common.Toolbar
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.ui.theme.VKETheme


@Composable
fun ListScreen(
    viewModel: AppListViewModel,
    modifier: Modifier = Modifier,
    onItemClick: (appId: Long) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is AppListEvent.ShowSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    launch {
                        snackbarHostState.showSnackbar(
                            context.getString(event.messageRes, *event.formatArgs.toTypedArray())
                        )
                    }
                }
            }
        }
    }

    ListScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onToolbarClick = viewModel::onToolbarClick,
        onBackClick = onBackClick,
        onItemClick = onItemClick,
        onInstallClick = viewModel::onInstallClick,
        modifier = modifier,
    )
}

/**
 * «Глупый» экран: только рисует то, что ему передали. Удобен для Preview.
 */
@Composable
fun ListScreen(
    state: AppListUiState,
    snackbarHostState: SnackbarHostState,
    onToolbarClick: () -> Unit,
    onBackClick: () -> Unit,
    onItemClick: (appId: Long) -> Unit,
    onInstallClick: (app: App) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = stringResource(R.string.app_list_title),
                onBackClick = onBackClick,
                onClick = onToolbarClick,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        when (state) {
            AppListUiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            is AppListUiState.Content -> ListContent(
                apps = state.apps,
                onItemClick = onItemClick,
                onInstallClick = onInstallClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListScreenPreview() {
    VKETheme {
        ListScreen(
            state = AppListUiState.Content(previewApps),
            snackbarHostState = remember { SnackbarHostState() },
            onToolbarClick = {},
            onBackClick = {},
            onItemClick = {},
            onInstallClick = {},
        )
    }
}
