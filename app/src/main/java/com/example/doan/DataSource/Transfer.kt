package com.example.doan.DataSource

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity
data class Transfer(
    var amount: Int,
    override val date: String,
    @PrimaryKey val odersn: String,
    val receivername: String,
    override var referenceid: String,
    override var type: Int = TYPE_TRANSFER
) : BalanceChanges(type, referenceid, date), Parcelable {
    constructor() : this(0, "", "", "", "", TYPE_TRANSFER)
}