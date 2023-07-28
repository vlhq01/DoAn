package com.example.doan

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doan.Adapters.ContactlistAdapter
import com.example.doan.DataSource.ContactDataSource
import com.example.doan.DataSource.Contacts
import com.example.doan.DataSource.Datasource
import com.example.doan.databinding.MoneytransferContactlistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.Duration


class ContactsListFragment : Fragment() , LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding: MoneytransferContactlistBinding?=null
    private val binding get() = _binding!!
    private var contactsArrayList: ArrayList<Contacts>? = null
    private var contactsArrayList2: ArrayList<Contacts>? = null
    var PROJECTION = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        ContactsContract.CommonDataKinds.Phone.NUMBER,

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoneytransferContactlistBinding.inflate(inflater,container,false)
        contactsArrayList = ArrayList()
        contactsArrayList2 = ArrayList()

        contactsArrayList2?.add(Contacts(0,"Bo","0396577711"))
        contactsArrayList2?.add(Contacts(1,"Test2","6505553434"))
        contactsArrayList2?.add(Contacts(2,"Test1","6505553433"))
        contactsArrayList2?.add(Contacts(3,"Test3","+84868606421"))
        binding.contactlistRecycler.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        binding.contactlistRecycler.adapter = ContactlistAdapter(this,contactsArrayList2)
        onPermissionRequested()

        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }

        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

    }
    override fun onResume() {
        super.onResume()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)



    }
    private fun onPermissionRequested(){
        // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher. You can use either a val, as shown in this snippet,
// or a lateinit var in your onAttach() or onCreate() method.
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                getContacts()
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

        when {
            this.context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_CONTACTS
                )
            } == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.



                getContacts();

                Toast.makeText(this.context, "All the permissions are granted..", Toast.LENGTH_SHORT).show()
            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_CONTACTS)
            }
        }
    }
    private fun getContacts(){
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     *
     * This will always be called from the process's main thread.
     *
     * @param id The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        return activity?.let {
            CursorLoader(
                it,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null
            )
        }!!




    }


    override fun onLoaderReset(loader: Loader<Cursor>) {
        return
    }


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null) {
            while ( data.moveToNext()){
                val id = data.getLong(0)
                val name = data.getString(1)
                val phone = data.getString(2)

                contactsArrayList?.add(Contacts(id,phone,name))

            }
        }
        binding.contactlistRecycler.adapter?.notifyDataSetChanged()

    }

    override fun onStop() {
        super.onStop()
        binding.contactlistRecycler.adapter = null
    }
}