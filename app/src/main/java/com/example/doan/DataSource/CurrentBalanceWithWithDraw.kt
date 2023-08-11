package com.example.doan.DataSource

import androidx.room.Embedded
import androidx.room.Relation

class CurrentBalanceWithWithDraw(
    @Embedded
    val currentBalanceWithBalanceChangesID : CurrentBalanceWithBalanceChangesID,
    @Relation(parentColumn = "crblreferenceid", entityColumn = "referenceid")
    val withDraw: WithDraw
)