package com.vgb3.vke.presentation.applist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vgb3.vke.R

@Composable
fun RatingsControls(
    state: AppListUiState.Content,
    onSequentialClick: () -> Unit,
    onParallelClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(
            onClick = onSequentialClick,
            enabled = !state.ratingsLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.ratings_sequential))
        }
        OutlinedButton(
            onClick = onParallelClick,
            enabled = !state.ratingsLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.ratings_parallel))
        }
        if (state.ratingsLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text(stringResource(R.string.ratings_loading))
        }
        state.sequentialDurationMs?.let {
            Text(stringResource(R.string.ratings_sequential_duration, it))
        }
        state.parallelDurationMs?.let {
            Text(stringResource(R.string.ratings_parallel_duration, it))
        }
        if (state.ratingsError) {
            Text(
                text = stringResource(R.string.ratings_error),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
