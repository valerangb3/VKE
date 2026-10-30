package com.vgb3.vke.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgb3.vke.R
import com.vgb3.vke.presentation.ButtonApp
import com.vgb3.vke.presentation.common.SimilarApps
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.ui.theme.VKETheme

@Composable
fun DetailsContent(
    appDetails: AppDetails,
    similarApps: List<SimilarApp>,
    modifier: Modifier = Modifier
) {
    var descriptionCollapsed by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {

        DetailsHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            appDetails = appDetails
        )
        Spacer(Modifier.height(16.dp))
        ButtonApp(

            text = stringResource(R.string.install_button),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {}
        )

        Spacer(Modifier.height(12.dp))
        SimilarApps(
            apps = similarApps,
            onDownloadClick = { },
            contentPadding = PaddingValues(horizontal = 16.dp),
        )

        Spacer(Modifier.height(12.dp))
        ScreenshotsList(
            screenshotUrlList = appDetails.screenshotItems,
            contentPadding = PaddingValues(horizontal = 16.dp),
        )
        Spacer(Modifier.height(12.dp))
        DetailsDescription(
            description = appDetails.description,
            collapsed = descriptionCollapsed,
            onReadMoreClick = { descriptionCollapsed = !descriptionCollapsed },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        Spacer(Modifier.height(12.dp))
        Developer(
            name = appDetails.developer,
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DetailsContentPreview() {
    VKETheme() {
        DetailsContent(
            appDetails = previewAppDetails,
            similarApps = previewSimilarApps,
        )
    }
}
