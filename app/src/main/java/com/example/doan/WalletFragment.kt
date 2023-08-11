package com.example.doan


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doan.AccountsandSettingFragment.Companion.TAG
import com.example.doan.Adapters.LinkedBankAdapter
import com.example.doan.DataSource.Banks
import com.example.doan.DataSource.LinkedBanks

import com.example.doan.databinding.WalletfragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class WalletFragment : Fragment() {
    private val db = Firebase.firestore
    private var _binding: WalletfragmentBinding? = null
    private val binding get() = _binding!!
    var dataset: java.util.ArrayList<LinkedBanks>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WalletfragmentBinding.inflate(inflater, container, false)
        dataset = ArrayList()
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        binding.wallettoolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.linkedbanksrv.layoutManager = LinearLayoutManager(this.context)
        getbanklistfromdb()
        binding.addbanklayout.setOnClickListener({ showBottomSheetDialog() })
        val view = binding.root
        return view
    }


    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this.requireContext())
        bottomSheetDialog.setContentView(R.layout.linkingmethods_bottomsheetdialog)

        val bankaccountlayout =
            bottomSheetDialog.findViewById<LinearLayout>(R.id.BankaccountLinearLaySout)
        if (bankaccountlayout != null) {
            bankaccountlayout.setOnClickListener {
                val action = WalletFragmentDirections.actionWalletFragmentToBankListFragment()
                view?.findNavController()?.navigate(action)
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()

    }

    fun getbanklistfromdb() {
        val user = Firebase.auth
        val uuid = user.currentUser?.uid
        if (uuid != null) {
            db.collection("users").document(uuid).collection("linkedbankslist").get()
                .addOnSuccessListener { documents ->
                    for (doc in documents) {
                        val bank = doc.toObject(LinkedBanks::class.java)
                        dataset?.add(bank)
                    }
                    Log.d(TAG, "getbanklistfromdb: " + dataset.toString())
                    binding.linkedbanksrv.adapter = LinkedBankAdapter(dataset)
                }
        }

    }

}