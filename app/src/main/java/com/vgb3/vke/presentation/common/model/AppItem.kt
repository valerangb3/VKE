package com.vgb3.vke.presentation.common.model

import androidx.annotation.DrawableRes

data class AppItem(
    val id: Int,
    val picture: String,
    @DrawableRes
    val pictureRes: Int,
    val title: String,
    val description: String
)
