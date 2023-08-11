package com.example.doan.DataSource

import androidx.room.Embedded

class CurrentBalanceWithBalanceChanges(
    @Embedded val referenceid: String,
    @Embedded val currentbalance: Int,

    val balanceChanges: BalanceChanges
)