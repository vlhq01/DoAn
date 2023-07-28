package com.example.doan.DataSource

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Payment(val amount: Int, override var date: String, val  odersn:String, val merchantid : String, val referenceid : String,  override var type: Int = TYPE_PAYMENT)
    : BalanceChanges(type, date), Parcelable {
        constructor() : this(0,"","","","")
}