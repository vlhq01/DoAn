package com.example.doan.ViewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.doan.AccountsandSettingFragment
import com.example.doan.Adapters.HistoryAdapter
import com.example.doan.DataSource.*
import com.example.doan.MyApplication
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HistoryNotificationViewModel(application: MyApplication, val notificationRepository: NotificationRepository) : AndroidViewModel(application) {
    var bllisttosort: MutableLiveData<MutableList<BalanceChangesSort>> = MutableLiveData()
    var notificationlist : MutableLiveData<MutableList<BalanceChangesWithCurrentBalance>> = MutableLiveData()
    private var temp: MutableList<BalanceChangesSort> = mutableListOf()
    val tempnotification: MutableList<BalanceChangesWithCurrentBalance> = mutableListOf()
    private val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    val docref =
        user?.let { db.collection("users").document(it.uid).collection("Wallet Balance Changes") }

    fun insertTopUp(topUp: TopUp) = viewModelScope.launch {
        notificationRepository.insertTopUp(topUp)
    }

    fun insertTransfer(transfer: Transfer) = viewModelScope.launch {
        notificationRepository.insertTransfer(transfer)
    }

    fun insertpayment(payment: Payment) = viewModelScope.launch {
        notificationRepository.inserPayment(payment)
    }

    fun insertTransferFrom(transferFrom: TransferFrom) = viewModelScope.launch {
        notificationRepository.insertTransferFrom(transferFrom)
    }

    fun insertWithDraw(withDraw: WithDraw) = viewModelScope.launch {
        notificationRepository.insertWithDraw(withDraw)
    }

    fun insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID: CurrentBalanceWithBalanceChangesID) = viewModelScope.launch {
        notificationRepository.insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID)
    }

    fun getallCurrentBalanceWithTopUp() = notificationRepository.getallCurrentBalanceWithTopUp()

    fun getallCurrentBalanceWithTransfer() = notificationRepository.getallCurrentBalanceWithTransfer()

    fun getallCurrentBalanceWithPayment() = notificationRepository.getallCurrentBalanceWithPayment()

    fun getallCurrentBalanceWithTransferFrom() = notificationRepository.getallCurrentBalanceWithTransferFrom()

    fun getallCurrentBalanceWithWithDraw() = notificationRepository.getallCurrentBalanceWithWithDraw()

    init {
        docref?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d(TAG, "Listen failed: ")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                temp.clear()
                getListBalanceChanges()
            }
        }
    }

    private fun getListBalanceChanges() {
        viewModelScope.launch {
            val uuid = user?.uid
            if (uuid != null) {
                db.collection("users").document(uuid).collection("Wallet Balance Changes").get()
                    .addOnSuccessListener { documents ->
                        for (doc in documents) {
                            var type = doc.getLong("type")
                            Log.d(AccountsandSettingFragment.TAG, "TYPE: " + type)
                            if (type != null) {
                                when (type.toInt()) {
                                    BalanceChanges.TYPE_TRANSFER -> {
                                        val transfer = doc.toObject(Transfer::class.java)
                                        temp.add(BalanceChangesSort(transfer, transfer.date))
                                    }
                                    BalanceChanges.TYPE_TRANSFERFROM -> {
                                        val transferfrom = doc.toObject(TransferFrom::class.java)
                                        temp.add(
                                            BalanceChangesSort(
                                                transferfrom,
                                                transferfrom.date
                                            )
                                        )
                                    }
                                    BalanceChanges.TYPE_TOPUP -> {
                                        val topup = doc.toObject(TopUp::class.java)
                                        temp.add(BalanceChangesSort(topup, topup.date))
                                    }
                                    BalanceChanges.TYPE_PAYMENT -> {
                                        val payment = doc.toObject(Payment::class.java)
                                        temp.add(BalanceChangesSort(payment, payment.date))
                                    }
                                    BalanceChanges.TYPE_WITHDRAW -> {
                                        val withdraw = doc.toObject(WithDraw::class.java)
                                        temp.add(BalanceChangesSort(withdraw, withdraw.date))
                                    }
                                }
                                BalanceChanges.TYPE_WITHDRAW -> {
                                    val withdraw = doc.toObject(WithDraw::class.java)
                                    temp.add(BalanceChangesSort(withdraw,withdraw.date))
                                }
                            }
                        }
                        bllisttosort.postValue(temp)
                    }.addOnFailureListener {
                    Log.d(AccountsandSettingFragment.TAG, "getfromdb: some thing is wrong")
                }
            }
        }
    }


}