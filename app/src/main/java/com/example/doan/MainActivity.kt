package com.example.doan

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.doan.DataSource.*
import com.example.doan.ViewModels.*

import com.example.doan.databinding.ActivityMainBinding
import com.example.doan.roomdatabase.NotificationDatabase
import com.example.doan.utils.Constants

import com.example.doan.utils.PermissionHelper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    lateinit var historyNotificationViewModel: HistoryNotificationViewModel
    lateinit var linkedBanksListViewModel: LinkedBanksListViewModel
    lateinit var mainViewModel: MainViewModel
    lateinit var contactsListViewModel: ContactsListViewModel
    var currentBalance = 0
    val activity = this
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            ActivityResultCallback { isGranted ->
                if (isGranted) {
                    Log.d("TAG", ": requestPermissionLauncher granted")
                } else {
                    Log.d("TAG", ": requestPermissionLauncher denied")
                }
            })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        lifecycleScope.launch {
            val user = Firebase.auth.currentUser
            val token = user?.let { getAndStoreRegToken(it.uid) }
            if (token != null) {
                Log.d(TAG, token)
            }
        }
        binding.bottomnavigation.setupWithNavController(navController)
        initHistoryNotificationViewModel()
        initLinkedBanksListViewModel()
        initMainViewModel()
        initcontactslistviewmodel()
        PermissionHelper.askPermission(
            requestPermissionLauncher,
            this,
            Manifest.permission.POST_NOTIFICATIONS
        )

        val receivemoney = intent.extras
        val sendername = receivemoney?.getString("sendername")
        val amount = receivemoney?.getString("amount")?.toInt()
        val date = receivemoney?.getString("date")
        val referenceid = receivemoney?.getString("referenceid")
        val odersn = receivemoney?.getString("odersn")
        Log.d(TAG, "onCreateReceiveData: " + sendername)
        if(sendername != null && amount != null && date != null && referenceid != null && odersn != null){
            lifecycleScope.launch {
                insertreceivetranferformtodatabase(TransferFrom(amount,date,odersn,sendername,referenceid))
            }

        }

    }

    val receiver = object : BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                val type = intent?.getIntExtra("type", -1)
                when (type) {
                    BalanceChanges.TYPE_TRANSFER -> {
                        val transfer = intent.getParcelableExtra("transfer", Transfer::class.java)
                        mainViewModel.balance.observe(activity, Observer {
                            if (it != null) {
                                currentBalance = it
                            }
                            if (transfer != null) {
                                sendnotificationtransfertodatabase(transfer,currentBalance)
                            }
                        })
                        Log.d(TAG, "onReceive: " + transfer)
                    }

                    BalanceChanges.TYPE_TOPUP -> {
                        val topup = intent.getParcelableExtra("topup", TopUp::class.java)
                            mainViewModel.balance.observe(activity, Observer {
                                if (it != null) {
                                    currentBalance = it
                                }
                                if (topup != null) {
                                    Log.d(TAG, "onReceivecurrentbalancelaunch: " + currentBalance)
                                    sendnotificationtopuptodatabase(topup, currentBalance)
                                }
                            })
                            Log.d(TAG, "onReceivecurrentbalancelaunch: " + currentBalance)
                            Log.d(TAG, "onReceivecrbl: " + currentBalance)
                    }



                    BalanceChanges.TYPE_PAYMENT -> {
                        val payment = intent.getParcelableExtra("payment", Payment::class.java)
                        mainViewModel.balance.observe(activity, Observer {
                            if (it != null) {
                                currentBalance = it
                            }
                            if (payment != null) {
                                sendnotificationpaymenttodatabase(payment,currentBalance)
                            }
                        })
                        Log.d(TAG, "onReceive: " + payment)
                    }

                    BalanceChanges.TYPE_WITHDRAW -> {
                        val withdraw = intent.getParcelableExtra("withdraw", WithDraw::class.java)
                        Log.d(TAG, "receivewithdraw: " + withdraw)
                        mainViewModel.balance.observe(activity, Observer {
                            if (it != null) {
                                currentBalance = it
                            }
                            if (withdraw != null) {
                                sendnotificationwithdrawtodatabase(withdraw,currentBalance)
                            }
                        })
                    }

                    BalanceChanges.TYPE_TRANSFERFROM -> {
                        val transferfrom = intent.getParcelableExtra("transferfrom", TransferFrom::class.java)
                        mainViewModel.balance.observe(activity, Observer {
                            if (it != null) {
                                currentBalance = it
                            }
                            if (transferfrom != null) {
                                sendnotificationtransferfromtodatabase(transferfrom,currentBalance)
                            }
                        })
                    }
                }

            } catch (e: java.lang.Exception) {
                Log.d(TAG, "onReceive: " + e)
            }
        }

    }

    fun sendnotificationtopuptodatabase(topUp: TopUp, currentBalance: Int){
        val currentBalanceWithBalanceChangesID = CurrentBalanceWithBalanceChangesID(topUp.referenceid, currentBalance)
        historyNotificationViewModel.insertTopUp(topUp)
        historyNotificationViewModel.insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID)
    }

    fun sendnotificationtransfertodatabase(transfer: Transfer, currentBalance: Int){
        val currentBalanceWithBalanceChangesID = CurrentBalanceWithBalanceChangesID(transfer.referenceid, currentBalance)
        historyNotificationViewModel.insertTransfer(transfer)
        Log.d(TAG, "sendnotificationtransfertodatabase888888888: " + currentBalanceWithBalanceChangesID.crblreferenceid)
        historyNotificationViewModel.insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID)
    }

    fun sendnotificationpaymenttodatabase(payment: Payment,currentBalance: Int){
        val currentBalanceWithBalanceChangesID = CurrentBalanceWithBalanceChangesID(payment.referenceid, currentBalance)
        historyNotificationViewModel.insertpayment(payment)
        historyNotificationViewModel.insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID)
    }

    fun sendnotificationwithdrawtodatabase(withDraw: WithDraw,currentBalance: Int){
        val currentBalanceWithBalanceChangesID = CurrentBalanceWithBalanceChangesID(withDraw.referenceid, currentBalance)
        historyNotificationViewModel.insertWithDraw(withDraw)
        historyNotificationViewModel.insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID)
    }

    fun sendnotificationtransferfromtodatabase(transferFrom: TransferFrom,currentBalance: Int){
        val currentBalanceWithBalanceChangesID = CurrentBalanceWithBalanceChangesID(transferFrom.referenceid, currentBalance)
        historyNotificationViewModel.insertTransferFrom(transferFrom)
        historyNotificationViewModel.insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID)
    }
    override fun onStart() {
           super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            (receiver),
            IntentFilter(Constants.BALANCECHANGES)
        )
    }

    private suspend fun getAndStoreRegToken(uid: String): String {
        // [START log_reg_token]
        var token = Firebase.messaging.token.await()

        // Check whether the retrieved token matches the one on your server for this user's device
        val preferences = this.getPreferences(Context.MODE_PRIVATE)
        val tokenStored = preferences.getString("deviceToken", "")
        lifecycleScope.launch {
            if (tokenStored == "" || tokenStored != token) {
                // If you have your own server, call API to send the above token and Date() for this user's device

                // Example shown below with Firestore
                // Add token and timestamp to Firestore for this user
                val deviceToken = hashMapOf(
                    "token" to token,
                )

                // Get user ID from Firebase Auth or your own server
                Firebase.firestore.collection("users").document(uid)
                    .set(deviceToken, SetOptions.merge()).await()
            }
        }
        Log.d(TAG, "got token: $token")

        return token
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initHistoryNotificationViewModel() {
        val db = NotificationDatabase.getInstance(this)
        val notificationRepository = NotificationRepository(db)
        val historyNotificationViewModelFactory = HistoryNotificationViewModelFactory(application, notificationRepository)
        historyNotificationViewModel = ViewModelProvider(
            this,
            historyNotificationViewModelFactory
        )[HistoryNotificationViewModel::class.java]

    }

    private fun initLinkedBanksListViewModel() {
        val linkedBanksListViewModelFactory = LinkedBanksListViewModelFactory(application)
        linkedBanksListViewModel = ViewModelProvider(
            this,
            linkedBanksListViewModelFactory
        )[LinkedBanksListViewModel::class.java]
    }

    private fun initMainViewModel() {
        val mainViewModelFactory = MainViewModelFactory(application)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
    }

    private fun initcontactslistviewmodel() {
        val contactsListRepository = ContactsListRepository(this)
        val contactslistViewModelFactory =
            ContactslistViewModelFactory(contactsListRepository, application)
        contactsListViewModel =
            ViewModelProvider(this, contactslistViewModelFactory)[ContactsListViewModel::class.java]
    }

    fun insertreceivetranferformtodatabase(transferFrom: TransferFrom){
        mainViewModel.balance.observe(activity, Observer {
            if (it != null) {
                currentBalance = it
            }
            val currentBalanceWithBalanceChangesID = CurrentBalanceWithBalanceChangesID(transferFrom.referenceid, currentBalance)
            historyNotificationViewModel.insertTransferFrom(transferFrom)
            historyNotificationViewModel.insertCurrentBalanceWithBalanceChangesID(currentBalanceWithBalanceChangesID)
        })

    }
}