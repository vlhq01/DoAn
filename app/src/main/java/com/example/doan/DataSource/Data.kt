package com.example.doan.DataSource

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    var networlprovider: String,
    var datacapacity: Double,
    var duration: Int,
    var value: Int,
    var discount: Int
) :
    Parcelable {
    constructor() : this("", 0.0, 0, 0, 0)
}