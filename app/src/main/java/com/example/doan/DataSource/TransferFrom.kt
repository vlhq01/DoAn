package com.example.doan.DataSource

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransferFrom(var amount:Int,
                    override val date: String, val odersn:String, val sendername:String, val referenceid:String, override var type:Int = TYPE_TRANSFERFROM)
    : BalanceChanges(type, date), Parcelable {
    constructor() : this(0,"","","","",BalanceChanges.TYPE_TRANSFERFROM)
}