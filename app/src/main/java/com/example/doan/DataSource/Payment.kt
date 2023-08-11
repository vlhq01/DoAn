package com.example.doan.DataSource

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Payment(
    val amount: Int,
    override var date: String,
    val odersn: String,
    val merchantid: String,
    override var referenceid: String,
    override var type: Int = TYPE_PAYMENT
) : BalanceChanges(type, referenceid, date), Parcelable {
    constructor() : this(0, "", "", "", "")
}