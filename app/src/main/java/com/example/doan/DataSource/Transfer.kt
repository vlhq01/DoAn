package com.example.doan.DataSource

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Transfer(var amount:Int,
                    override val date: String, val odersn:String, val receivername:String, val referenceid:String, override var type:Int = TYPE_TRANSFER)
    : BalanceChanges(type, date), Parcelable {
    constructor() : this(0,"","","","",BalanceChanges.TYPE_TRANSFER)
}