package com.example.doan.DataSource

import androidx.room.Embedded
import androidx.room.Relation

class BalanceChangesWithCurrentBalance(
    val balanceChanges: BalanceChanges,
    val currentbalance: Int
)