package com.example.doan.DataSource

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TopUp(
    var amount: Int,
    override val date: String, val bank: String,
    val odersn: String,
    override var referenceid: String,
    override var type: Int = TYPE_TOPUP
) : BalanceChanges(type, referenceid, date), Parcelable {
    constructor() : this(0, "", "", "", "")
}