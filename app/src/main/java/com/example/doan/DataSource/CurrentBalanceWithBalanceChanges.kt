package com.example.doan.DataSource

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nullable

@Entity
data class CurrentBalanceWithBalanceChangesID(
              @PrimaryKey
              val crblreferenceid: String,
              val currentbalance: Int?
)