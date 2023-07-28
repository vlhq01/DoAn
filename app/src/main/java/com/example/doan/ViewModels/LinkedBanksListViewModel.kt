package com.example.doan.ViewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.doan.AccountsandSettingFragment
import com.example.doan.Adapters.LinkedBankAdapter
import com.example.doan.DataSource.BalanceChangesSort
import com.example.doan.DataSource.LinkedBanks
import com.example.doan.MyApplication
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LinkedBanksListViewModel(application: MyApplication): AndroidViewModel(application) {
    var linkedbankslist : MutableLiveData<MutableList<LinkedBanks>> = MutableLiveData()
    private var temp : MutableList<LinkedBanks> = mutableListOf()
    private val db = Firebase.firestore
    init{
        getbanklistfromdb()
    }

    fun getbanklistfromdb(){
        val user = Firebase.auth
        val uuid = user.currentUser?.uid
        if (uuid != null) {
            db.collection("users").document(uuid).collection("linkedbankslist").get().addOnSuccessListener {
                    documents ->
                for(doc in documents){
                    val bank = doc.toObject(LinkedBanks::class.java)
                    temp.add(bank)
                }
                linkedbankslist.postValue(temp)
            }.addOnFailureListener({
                Log.d(TAG, "getbanklistfromdb: something is wrong ")
            })
        }

    }
}