package com.example.doan

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doan.Adapters.ContactlistAdapter
import com.example.doan.ClicklistenerInterface.ContactsListItemClickListener
import com.example.doan.DataSource.Contacts
import com.example.doan.databinding.MoneytransferContactlistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ContactsListFragment : Fragment() {
    private var _binding: MoneytransferContactlistBinding? = null
    private val binding get() = _binding!!
    private var contactsArrayList: ArrayList<Contacts>? = null
    private var searchcontactsArrayList: ArrayList<Contacts>? = null
    private var contactsArrayList2: ArrayList<Contacts>? = null
    private val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoneytransferContactlistBinding.inflate(inflater, container, false)
        contactsArrayList = ArrayList()
        contactsArrayList2 = ArrayList()
        searchcontactsArrayList = ArrayList()
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    binding.btngetcontacts.visibility = View.GONE
                    binding.contactlistRecycler.visibility = View.VISIBLE
                    loadcontactfromviewmodel()
                } else {

                }
            }
        Log.d(TAG, "onCreateView: " + contactsArrayList.toString())
        contactsArrayList2?.add(Contacts("Bo", "0396577711"))
        contactsArrayList2?.add(Contacts("Test2", "6505553434"))
        contactsArrayList2?.add(Contacts("Test1", "6505553433"))
        contactsArrayList2?.add(Contacts("Test3", "+84868606421"))
        binding.contactlistRecycler.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        onPermissionRequested(requestPermissionLauncher)
        val contactsListViewModel = (activity as MainActivity).contactsListViewModel
        contactsListViewModel.contactslist.observe(viewLifecycleOwner, Observer {
            contactsArrayList = it as ArrayList<Contacts>
            Log.d(TAG, "onCreateView: " + it)
            binding.contactlistRecycler.adapter =
                ContactlistAdapter(contactsListItemClickListener, contactsArrayList)
        })

        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }

        binding.transfertoolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        binding.contactsearchview.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                var searchcontact: Contacts
                db.collection("users").whereEqualTo("phone", query).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val receivername = document.getString("name").toString()
                            val receiverphone = document.getString("phone").toString()
                            searchcontact = Contacts(receivername, receiverphone)
                            searchcontactsArrayList!!.clear()
                            searchcontactsArrayList!!.add(searchcontact)
                        }
                    }.addOnCompleteListener {
                        Log.d(TAG, "onQueryTextSubmit: " + searchcontactsArrayList)
                        binding.contactlistRecycler.adapter = ContactlistAdapter(
                            searchcontactsListItemClickListener,
                            searchcontactsArrayList
                        )
                    }

                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        val view = binding.root
        return view
    }

    val contactsListItemClickListener = object : ContactsListItemClickListener {
        override fun onContactsListItemItemClick(pos: Int) {
            val username = contactsArrayList?.get(pos)?.name.toString()
            val userphone = contactsArrayList?.get(pos)?.phone.toString()
            val action =
                ContactsListFragmentDirections.actionContactsListFragmentToMoneyTransferFragment(
                    username,
                    userphone
                )
            findNavController().navigate(action)
        }
    }

    val searchcontactsListItemClickListener = object : ContactsListItemClickListener {
        override fun onContactsListItemItemClick(pos: Int) {
            val username = searchcontactsArrayList?.get(pos)?.name.toString()
            val userphone = searchcontactsArrayList?.get(pos)?.phone.toString()
            val action =
                ContactsListFragmentDirections.actionContactsListFragmentToMoneyTransferFragment(
                    username,
                    userphone
                )
            findNavController().navigate(action)
        }
    }


    private fun onPermissionRequested(requestPermissionLauncher: ActivityResultLauncher<String>) {
        // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher. You can use either a val, as shown in this snippet,
// or a lateinit var in your onAttach() or onCreate() method.

        when {
            this.context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_CONTACTS
                )
            } == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                binding.btngetcontacts.visibility = View.GONE
                binding.contactlistRecycler.visibility = View.VISIBLE
                //getContacts();
                loadcontactfromviewmodel()
                Toast.makeText(
                    this.context,
                    "All the permissions are granted..",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                binding.btngetcontacts.visibility = View.VISIBLE
                binding.contactlistRecycler.visibility = View.GONE
                binding.btngetcontacts.setOnClickListener {
                    requestPermissionLauncher.launch(
                        android.Manifest.permission.READ_CONTACTS
                    )
                }

            }
        }
    }

    private fun loadcontactfromviewmodel() {
        val contactsListViewModel = (activity as MainActivity).contactsListViewModel
        contactsListViewModel.loadfromcontact()
    }


}