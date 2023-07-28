package com.example.doan.DataSource

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mobile(var value: Int, var discount:Int) : Parcelable {
    constructor() : this(0,0)
}
