package com.example.doan.ViewModels

import com.example.doan.DataSource.*
import com.example.doan.roomdatabase.NotificationDatabase

class NotificationRepository(private val notificationDatabase : NotificationDatabase) {
    suspend fun insertTopUp(topUp: TopUp) = notificationDatabase.getNotificationDao().insertTopUp(topUp)

    suspend fun insertTransfer(transfer: Transfer) = notificationDatabase.getNotificationDao().insertTransfer(transfer)

    suspend fun inserPayment(payment: Payment) = notificationDatabase.getNotificationDao().insertPayment(payment)

    suspend fun insertTransferFrom(transferFrom: TransferFrom) = notificationDatabase.getNotificationDao().insertTransferFrom(transferFrom)

    suspend fun insertWithDraw(withDraw: WithDraw) = notificationDatabase.getNotificationDao().insertWithDraw(withDraw)

    suspend fun insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID: CurrentBalanceWithBalanceChangesID) =
        notificationDatabase.getNotificationDao().insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID)

    fun getallCurrentBalanceWithTopUp() = notificationDatabase.getNotificationDao().getallCurrentBalanceWithTopUp()

    fun getallCurrentBalanceWithTransfer() = notificationDatabase.getNotificationDao().getallCurrentBalanceWithTransfer()

    fun getallCurrentBalanceWithPayment() = notificationDatabase.getNotificationDao().getallCurrentBalanceWithPayment()

    fun getallCurrentBalanceWithTransferFrom() = notificationDatabase.getNotificationDao().getallCurrentBalanceWithTransferFrom()

    fun getallCurrentBalanceWithWithDraw() = notificationDatabase.getNotificationDao().getallCurrentBalanceWithWithDraw()

}