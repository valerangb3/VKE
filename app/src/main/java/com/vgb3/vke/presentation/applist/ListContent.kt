package com.vgb3.vke.presentation.applist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgb3.vke.data.mockApps
import com.vgb3.vke.ui.theme.VKETheme
import com.vgb3.vke.presentation.common.model.AppItem as AppItemModel

@Composable
fun ListContent(
    apps: List<AppItemModel>,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(8.dp),
    contentPadding: PaddingValues = PaddingValues(vertical = 10.dp),
    onItemClick: (appId: Long) -> Unit = {},
    onInstallClick: (app: AppItemModel) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        contentPadding = contentPadding,
    ) {
        items(apps, key = { it.id }) { appModel ->
            AppItem(
                appItem = appModel,
                onClick = {
                    onItemClick(appModel.id.toLong())
                },
                onOnInstall = { onInstallClick(appModel) }
            )
        }
    }
}

@Preview
@Composable
fun ListContentPreview() {
    VKETheme() {
        ListContent(apps = mockApps)
    }
}
