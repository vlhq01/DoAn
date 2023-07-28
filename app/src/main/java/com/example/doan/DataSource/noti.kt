package com.example.doan.DataSource

import androidx.annotation.DrawableRes

data class noti (
    @DrawableRes
    val imageResourceId: Int,
    val notitype : String,
    val header : String,
    val content :String,
    val timeday: String,
    val timehour: String
    )
