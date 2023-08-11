package com.example.doan.DataSource

import androidx.room.Embedded
import androidx.room.Relation

data class CurrentBalanceWithPayment(
    @Embedded
    val currentBalanceWithBalanceChangesID : CurrentBalanceWithBalanceChangesID,
    @Relation(parentColumn = "crblreferenceid", entityColumn = "referenceid")
    val payment : Payment?
)