package com.vgb3.vke.presentation.applist

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vgb3.vke.R
import com.vgb3.vke.presentation.common.model.AppItem as AppItemModel
import com.vgb3.vke.ui.theme.VKETheme

@Composable
fun AppItem(
    appItem: AppItemModel,
    onClick: () -> Unit,
    onOnInstall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(88.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = appItem.picture,
            placeholder = painterResource(appItem.pictureRes),
            error = painterResource(appItem.pictureRes),
            fallback = painterResource(appItem.pictureRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = appItem.title,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = appItem.description,
                lineHeight = 14.sp,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                maxLines = 2
            )
        }
        Button(
            modifier = Modifier
                .height(40.dp)
                .width(108.dp),
            onClick = onOnInstall
        ) {
            Text(
                text = stringResource(R.string.app_install),
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}

@SuppressLint("ResourceType")
@Preview
@Composable
fun AppItemPreview() {
    VKETheme() {
        AppItem(
            appItem = AppItemModel(
                id = 1,
                picture = "",
                title = "Т-Банк",
                description = "Управляйте финансами в приложении...",
                pictureRes = R.drawable.tbank
            ),
            onClick = {},
            onOnInstall = {}
        )
    }
}