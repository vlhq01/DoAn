package com.example.doan

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.doan.DataSource.Payment
import com.example.doan.DataSource.TopUp
import com.example.doan.DataSource.Transfer
import com.example.doan.utils.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCM:FirebaseMessagingService() {
    private val db = Firebase.firestore
    private val TAG = "MyFCM"
    override fun onNewToken(token: String) {
        //sendTokenToServer(token)

        Log.d(TAG, "onNewToken: $token")

    }

    fun sendTokenToServer(token : String?){
        val user = Firebase.auth.currentUser
        val fcmtoke = hashMapOf(
            "notitoken" to token,
        )
        if (user != null) {
            db.collection("users").document(user.uid)
                .set(fcmtoke, SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "onNewToken: successfully add token") }
                .addOnFailureListener{ e -> Log.d(TAG, "onNewToken: error while set token"+ e)}
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.data?.let {
            val type = it.getValue("type")
           when(type.toInt()){
               3 -> {
                   val amount = it.getValue("amount").toString().toInt()
                   Log.d(TAG, "onMessageReceived: " + amount)
                   val date = it.getValue("date").toString()
                   Log.d(TAG, "onMessageReceived: " + date)
                   val bank = it.getValue("bank").toString()
                   Log.d(TAG, "onMessageReceived: " + bank)
                   val odersn  =  it.getValue("odersn").toString()
                   val referenceid =  it.getValue("referenceid").toString()
                   showTopUpNotification(TopUp(amount,date,bank,odersn,referenceid))
               }

               1 -> {
                   val amount = it.getValue("amount").toString().toInt()
                   Log.d(TAG, "onMessageReceived: " + amount)
                   val date = it.getValue("date").toString()
                   Log.d(TAG, "onMessageReceived: " + date)
                   val receivername = it.getValue("receivername").toString()
                   Log.d(TAG, "onMessageReceived: " + receivername)
                   val odersn  =  it.getValue("odersn").toString()
                   val referenceid =  it.getValue("referenceid").toString()
                   showTransferNotification(Transfer(amount,date,odersn,receivername,referenceid))
               }

               2 -> {
                   val amount = it.getValue("amount").toString().toInt()
                   Log.d(TAG, "onMessageReceived: " + amount)
                   val date = it.getValue("date").toString()
                   Log.d(TAG, "onMessageReceived: " + date)
                   val merchantid = it.getValue("merchantid").toString()
                   Log.d(TAG, "onMessageReceived: " + merchantid)
                   val odersn  =  it.getValue("odersn").toString()
                   val referenceid =  it.getValue("referenceid").toString()
                   showPaymentNotification(Payment(amount,date,odersn,referenceid,merchantid))
               }
           }
        }
    }

    private fun showTopUpNotification(data: TopUp) {
        // Create an explicit intent for an Activity in your app
        //val intent = Intent(this, MainActivity::class.java).apply {
            //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //}
        //intent.putExtra(Constants.OPEN_TAB_FROM_NOTIFICATION,data)
        //val pendingIntent: PendingIntent =
            //PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder =
            NotificationCompat.Builder(this, getString(R.string.doan_notification_channel_id))
                .setSmallIcon(R.drawable.topupicon).setContentTitle("Money TopUp")
                .setContentText("Top Up "+ data.amount +" from "+ data.bank).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
        val n = builder.build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1234,n)
    }

    private fun showTransferNotification(data: Transfer) {
        // Create an explicit intent for an Activity in your app
        //val intent = Intent(this, MainActivity::class.java).apply {
        //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //}
        //intent.putExtra(Constants.OPEN_TAB_FROM_NOTIFICATION,data)
        //val pendingIntent: PendingIntent =
        //PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder =
            NotificationCompat.Builder(this, getString(R.string.doan_notification_channel_id))
                .setSmallIcon(R.drawable.money_transfer_10135195).setContentTitle("Money Transfer")
                .setContentText("Transfer "+ data.amount +" to "+ data.receivername).setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        val n = builder.build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1234,n)
    }

    private fun showPaymentNotification(data: Payment) {
        // Create an explicit intent for an Activity in your app
        //val intent = Intent(this, MainActivity::class.java).apply {
        //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //}
        //intent.putExtra(Constants.OPEN_TAB_FROM_NOTIFICATION,data)
        //val pendingIntent: PendingIntent =
        //PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder =
            NotificationCompat.Builder(this, getString(R.string.doan_notification_channel_id))
                .setSmallIcon(R.drawable.paymenticon).setContentTitle("Payment")
                .setContentText("You have paid "+ data.amount +" for mobile/data top up service with merchantid of"+ data.merchantid).setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        val n = builder.build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1234,n)
    }
}