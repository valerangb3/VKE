package com.vgb3.vke.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.vgb3.vke.R
import com.vgb3.vke.ui.theme.VKETheme

@Composable
fun ScreenshotsList(
    screenshotUrlList: List<String>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.app_details_screenshots),
            modifier = Modifier.padding(contentPadding),
        )
        Spacer(Modifier.height(8.dp))
        // Ряд вертикальных карточек фиксированной ширины, как в сторах:
        // на экране одновременно видно 2–3 скриншота, остальные — прокруткой.
        LazyRow(
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(screenshotUrlList) { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    // Crop: картинка заполняет всю карточку 9:16;
                    // если пропорции чуть отличаются, лишнее обрежется, а не останутся поля.
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(160.dp)
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(12.dp))
                        // Фон-заглушка, пока картинка грузится (или если не загрузилась).
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenshotsListPreview() {
    VKETheme {
        ScreenshotsList(
            screenshotUrlList = List(4) { "" },
            contentPadding = PaddingValues(horizontal = 16.dp),
        )
    }
}
