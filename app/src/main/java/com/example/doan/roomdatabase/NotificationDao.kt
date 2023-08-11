package com.example.doan.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.doan.DataSource.*

@Dao
interface NotificationDao {
      @Insert(onConflict = OnConflictStrategy.IGNORE)
      suspend fun insertTopUp(topUp: TopUp)

      @Insert(onConflict = OnConflictStrategy.IGNORE)
      suspend fun insertTransfer(transfer: Transfer)

      @Insert(onConflict = OnConflictStrategy.IGNORE)
      suspend fun insertPayment(payment: Payment)

      @Insert(onConflict = OnConflictStrategy.IGNORE)
      suspend fun insertTransferFrom(transferFrom: TransferFrom)

      @Insert(onConflict = OnConflictStrategy.IGNORE)
      suspend fun insertWithDraw(withDraw: WithDraw)

      @Insert(onConflict = OnConflictStrategy.IGNORE)
      suspend fun insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID: CurrentBalanceWithBalanceChangesID)

      @Transaction
      @Query("SELECT * FROM CurrentBalanceWithBalanceChangesID,TopUp WHERE CurrentBalanceWithBalanceChangesID.crblreferenceid = TopUp.referenceid")
      fun getallCurrentBalanceWithTopUp(): LiveData<List<CurrentBalanceWithTopUp>>

      @Transaction
      @Query("SELECT * FROM CurrentBalanceWithBalanceChangesID,Transfer WHERE CurrentBalanceWithBalanceChangesID.crblreferenceid = Transfer.referenceid")
      fun getallCurrentBalanceWithTransfer(): LiveData<List<CurrentBalanceWithTransfer>>

      @Transaction
      @Query("SELECT * FROM CurrentBalanceWithBalanceChangesID,Payment WHERE CurrentBalanceWithBalanceChangesID.crblreferenceid = Payment.referenceid")
      fun getallCurrentBalanceWithPayment(): LiveData<List<CurrentBalanceWithPayment>>

      @Transaction
      @Query("SELECT * FROM CurrentBalanceWithBalanceChangesID,TransferFrom WHERE CurrentBalanceWithBalanceChangesID.crblreferenceid = TransferFrom.referenceid")
      fun getallCurrentBalanceWithTransferFrom(): LiveData<List<CurrentBalanceWithTransferFrom>>

      @Transaction
      @Query("SELECT * FROM CurrentBalanceWithBalanceChangesID, WithDraw WHERE CurrentBalanceWithBalanceChangesID.crblreferenceid = WithDraw.referenceid")
      fun getallCurrentBalanceWithWithDraw(): LiveData<List<CurrentBalanceWithWithDraw>>


}