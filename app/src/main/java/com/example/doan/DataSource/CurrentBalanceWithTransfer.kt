package com.example.doan.DataSource

import androidx.room.Embedded
import androidx.room.Relation

data class CurrentBalanceWithTransfer(
    @Embedded val currentBalanceWithBalanceChangesID : CurrentBalanceWithBalanceChangesID,
    @Relation(parentColumn = "crblreferenceid", entityColumn = "referenceid")
    val transfer: Transfer?
)