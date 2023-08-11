package com.example.doan.DataSource

import androidx.room.Entity

@Entity
open class BalanceChanges(
    open var type: Int,
    open var referenceid: String,
    open val date: String
) {
    companion object {
        const val TYPE_DATE = 0
        const val TYPE_TRANSFER = 1
        const val TYPE_PAYMENT = 2
        const val TYPE_TOPUP = 3
        const val TYPE_TRANSFERFROM = 4
        const val TYPE_WITHDRAW = 5
    }
}