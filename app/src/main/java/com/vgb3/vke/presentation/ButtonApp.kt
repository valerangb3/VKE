package com.vgb3.vke.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgb3.vke.ui.theme.VKETheme

@Composable
fun ButtonApp(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable
@Preview
private fun PreviewButtonApp() {
    VKETheme() {
        ButtonApp("Установить") { }
    }
}