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

class HistoryNotificationViewModel(application: MyApplication) : AndroidViewModel(application) {
    var bllisttosort : MutableLiveData<MutableList<BalanceChangesSort>> = MutableLiveData()
    private var temp : MutableList<BalanceChangesSort> = mutableListOf()
    private val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    val docref = user?.let { db.collection("users").document(it.uid).collection("Wallet Balance Changes") }


    init {
        docref?.addSnapshotListener { snapshot, e ->
             if (e!= null){
                 Log.d(TAG, "Listen failed: ")
                 return@addSnapshotListener
             }

            if (snapshot!=null){
                temp.clear()
                getListBalanceChanges()
            }
        }
    }

    private fun getListBalanceChanges() {
        viewModelScope.launch {
            val uuid = user?.uid
            if (uuid != null) {
                db.collection("users").document(uuid).collection("Wallet Balance Changes").get().addOnSuccessListener {
                        documents ->
                    for(doc in documents){
                        var type  = doc.getLong("type")
                        Log.d(AccountsandSettingFragment.TAG, "TYPE: " +type)
                        if (type != null) {
                            when(type.toInt()){
                                BalanceChanges.TYPE_TRANSFER -> {
                                    val transfer = doc.toObject(Transfer::class.java)
                                    temp.add(BalanceChangesSort(transfer,transfer.date))
                                }
                                BalanceChanges.TYPE_TRANSFERFROM -> {
                                    val transferfrom = doc.toObject(TransferFrom::class.java)
                                    temp.add(BalanceChangesSort(transferfrom,transferfrom.date))
                                }
                                BalanceChanges.TYPE_TOPUP -> {
                                    val topup = doc.toObject(TopUp::class.java)
                                    temp.add(BalanceChangesSort(topup,topup.date))
                                }
                                BalanceChanges.TYPE_PAYMENT -> {
                                    val payment = doc.toObject(Payment::class.java)
                                    temp.add(BalanceChangesSort(payment,payment.date))
                                }
                                BalanceChanges.TYPE_WITHDRAW -> {
                                    val withdraw = doc.toObject(WithDraw::class.java)
                                    temp.add(BalanceChangesSort(withdraw,withdraw.date))
                                }
                            }
                        }
                    }
                    bllisttosort.postValue(temp)
                }.addOnFailureListener{
                    Log.d(AccountsandSettingFragment.TAG, "getfromdb: some thing is wrong")
                }
            }
        }
    }
}