package com.example.doan.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.example.doan.DataSource.Contacts


class ContactsListRepository(val context : Context) {

    var PROJECTION = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
    )

    private val TAG = "debinf ContRepo"



    @SuppressLint("Range")
    fun fetchContacts(): List<Contacts>? {
        val contacts: MutableList<Contacts> = ArrayList()

        val cursor: Cursor? = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (cursor != null) {
            Log.i(TAG, "fetchContacts: cursor.getCount() is " + cursor.getCount())
        }
        if ((if (cursor != null) cursor.getCount() else 0) > 0) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val name: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phone: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val contact = Contacts(name, phone)
                    Log.i(TAG, "fetchContacts: phone is $phone")
                    contacts.add(contact)
                }
            }
        }
        if (cursor != null) {
            cursor.close()
        }
        return contacts
    }




}