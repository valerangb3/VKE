package com.vgb3.vke.presentation.applist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgb3.vke.R
import com.vgb3.vke.ui.theme.VKETheme
import com.vgb3.vke.presentation.common.model.AppItem as AppItemModel

object MockData {
    @SuppressLint("ResourceType")
    val appList = listOf(
        AppItemModel(
            id = 1,
            picture = "",
            title = "Т-Банк",
            description = "Управляйте финансами в приложении...",
            pictureRes = R.drawable.tbank
        ),
        AppItemModel(
            id = 2,
            picture = "",
            title = "Т-Банк",
            description = "Управляйте финансами в приложении...",
            pictureRes = R.drawable.tbank
        ),
        AppItemModel(
            id = 2,
            picture = "",
            title = "Т-Банк",
            description = "Управляйте финансами в приложении...",
            pictureRes = R.drawable.tbank
        )
    )
}

@Composable
fun ListContent(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(8.dp),
    contentPadding: PaddingValues = PaddingValues(10.dp),
    onItemClick: (appId: Long) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(MockData.appList) { appModel ->
            AppItem(
                appItem = appModel,
                onClick = {
                    onItemClick(appModel.id.toLong())
                },
                onOnInstall = {}
            )
        }
    }
}

@Preview
@Composable
fun ListContentPreview() {
    VKETheme() {
        ListContent()
    }
}