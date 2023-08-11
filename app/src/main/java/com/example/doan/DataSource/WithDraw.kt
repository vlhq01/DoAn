package com.example.doan.DataSource

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class WithDraw(
    var amount: Int,
    override val date: String,
    val bank: String,
    val banknumber: String,
    @PrimaryKey val odersn: String,
    override var referenceid: String,
    override var type: Int = TYPE_WITHDRAW
) : BalanceChanges(type, referenceid, date), Parcelable {
    constructor() : this(0, "", "", "", "", "")
}