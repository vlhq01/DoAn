package com.example.doan.ViewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.doan.MyApplication

class HistoryNotificationViewModelFactory(private val application: Application) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryNotificationViewModel(application = application as MyApplication) as T
    }
}