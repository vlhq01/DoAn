package com.example.doan.DataSource

import androidx.room.Embedded
import androidx.room.Relation

class CurrentBalanceWithTransferFrom(
    @Embedded
    val currentBalanceWithBalanceChangesID : CurrentBalanceWithBalanceChangesID,
    @Relation(parentColumn = "crblreferenceid", entityColumn = "referenceid")
    val transferFrom: TransferFrom?
)