package com.example.doan.ViewModels


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.doan.DataSource.Contacts
import com.example.doan.MyApplication
import kotlinx.coroutines.launch

class ContactsListViewModel(val contactsListRepository: ContactsListRepository,application: MyApplication):AndroidViewModel(application) {
    var contactslist : MutableLiveData<MutableList<Contacts>> = MutableLiveData()


    fun loadfromcontact(){
        viewModelScope.launch {
           val tempcontactlist = contactsListRepository.fetchContacts()
            contactslist.postValue(tempcontactlist as MutableList<Contacts>)
            Log.d(TAG, "loadfromcontact: " + tempcontactlist)
        }
    }


}