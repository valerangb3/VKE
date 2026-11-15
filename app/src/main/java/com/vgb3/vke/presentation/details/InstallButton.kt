package com.vgb3.vke.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vgb3.vke.R
import com.vgb3.vke.presentation.ButtonApp

@Composable
fun InstallButton(
    installState: InstallState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ButtonApp(
            text = stringResource(
                when (installState) {
                    InstallState.NotInstalled -> R.string.install_button
                    is InstallState.Installing -> R.string.install_cancel
                    InstallState.Installed -> R.string.install_completed
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = installState != InstallState.Installed,
            onClick = onClick,
        )
        if (installState is InstallState.Installing) {
            LinearProgressIndicator(
                progress = { installState.progress / 100f },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(stringResource(R.string.install_progress, installState.progress))
        }
    }
}
