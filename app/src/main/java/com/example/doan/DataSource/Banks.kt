package com.example.doan.DataSource

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Banks(
    val imgUrl:String,
    val bankname: String
) : Parcelable