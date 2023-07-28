package com.example.doan

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.doan.ViewModels.*

import com.example.doan.databinding.ActivityMainBinding
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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
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
        PermissionHelper.askPermission(
                requestPermissionLauncher,
        this,
        Manifest.permission.POST_NOTIFICATIONS
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

    private fun initHistoryNotificationViewModel(){
         val historyNotificationViewModelFactory = HistoryNotificationViewModelFactory(application)
        historyNotificationViewModel = ViewModelProvider(this, historyNotificationViewModelFactory)[HistoryNotificationViewModel::class.java]

    }

    private fun initLinkedBanksListViewModel(){
        val linkedBanksListViewModelFactory = LinkedBanksListViewModelFactory(application)
        linkedBanksListViewModel = ViewModelProvider(this, linkedBanksListViewModelFactory)[LinkedBanksListViewModel::class.java]
    }

    private fun initMainViewModel(){
        val mainViewModelFactory = MainViewModelFactory(application)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
    }
}