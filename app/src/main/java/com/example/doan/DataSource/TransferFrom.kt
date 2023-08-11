package com.example.doan.DataSource

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransferFrom(
    var amount: Int,
    override val date: String,
    val odersn: String,
    val sendername: String,
    override var referenceid: String,
    override var type: Int = TYPE_TRANSFERFROM
) : BalanceChanges(type, referenceid, date), Parcelable {
    constructor() : this(0, "", "", "", "", TYPE_TRANSFERFROM)
}