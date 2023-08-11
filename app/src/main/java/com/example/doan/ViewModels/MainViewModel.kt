package com.example.doan.ViewModels

import android.content.ContentValues
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.doan.AccountsandSettingFragment
import com.example.doan.AccountsandSettingFragment.Companion.TAG
import com.example.doan.AccountsandSettingFragmentDirections
import com.example.doan.FireBaseUserLiveData.ViewModel.ProjectViewModel

import com.example.doan.MyApplication
import com.example.doan.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class MainViewModel(application: MyApplication) : AndroidViewModel(application) {
    var balance: MutableLiveData<Int?> = MutableLiveData()
    var passcode: MutableLiveData<String?> = MutableLiveData()
    var phone: MutableLiveData<String?> = MutableLiveData()
    var name: MutableLiveData<String?> = MutableLiveData()
    private val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    val docref =
        user?.let { db.collection("users").document(it.uid) }
    init {
        docref?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d(ContentValues.TAG, "Listen failed: ")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                getdatafromdb()
            }
        }
    }
    fun mainviewmodelobserve(){
        docref?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d(ContentValues.TAG, "Listen failed: ")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                getdatafromdb()
            }
        }
    }

    fun getdatafromdb() {
        viewModelScope.launch {
            val uuid = user?.uid
            if (uuid != null) {
                db.runTransaction {
                    db.collection("users").document(uuid).get().addOnSuccessListener { documents ->
                        val tempbalance = documents.getLong("balance")
                        val temppasscode = documents.getString("passcode")
                        val tempphone = documents.getString("phone")
                        val tempname = documents.getString("name")
                        if (tempbalance != null) {
                            balance.postValue(tempbalance.toInt())
                        }
                        Log.d(TAG, "getdatafromdbcurrentbalance: " + tempbalance)
                        passcode.postValue(temppasscode)
                        phone.postValue(tempphone)
                        name.postValue(tempname)
                    }.addOnFailureListener {
                        Log.d(AccountsandSettingFragment.TAG, "getfromdb: some thing is wrong")
                    }
                }
            }
        }
    }
}