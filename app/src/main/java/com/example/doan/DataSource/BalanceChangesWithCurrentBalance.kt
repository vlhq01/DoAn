package com.example.doan.DataSource

import androidx.room.Embedded
import androidx.room.Relation

class BalanceChangesWithCurrentBalance(
    val balanceChanges: BalanceChanges,
    @Embedded val referenceid: String,
    @Embedded val currentbalance: Int
)