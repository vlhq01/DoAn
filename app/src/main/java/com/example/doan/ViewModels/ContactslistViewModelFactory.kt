package com.example.doan.ViewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.doan.MyApplication

class ContactslistViewModelFactory(
    private val contactsListRepository: ContactsListRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactsListViewModel(
            contactsListRepository = contactsListRepository,
            application = application as MyApplication
        ) as T
    }
}