package com.example.doan.DataSource

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class TransferFrom(
    var amount: Int,
    override val date: String,
    @PrimaryKey val odersn: String,
    val sendername: String,
    override var referenceid: String,
    override var type: Int = TYPE_TRANSFERFROM
) : BalanceChanges(type, referenceid, date), Parcelable {
    constructor() : this(0, "", "", "", "", TYPE_TRANSFERFROM)
}