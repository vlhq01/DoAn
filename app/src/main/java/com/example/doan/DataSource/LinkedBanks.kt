package com.example.doan.DataSource

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LinkedBanks (
    var imgurl: String,

    var bankname: String,

    var banknumber: String,

    var ownername: String

    ) : Parcelable {
    constructor(): this("","","","")
}