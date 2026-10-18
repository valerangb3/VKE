package com.vgb3.vke.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vgb3.vke.R

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        ButtonApp(
            text = stringResource(R.string.install_button),
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        )
    }
}