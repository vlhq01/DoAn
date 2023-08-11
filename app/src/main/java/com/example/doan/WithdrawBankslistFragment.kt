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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doan.Adapters.LinkedBankAdapter
import com.example.doan.Adapters.WithdrawBankListAdapter
import com.example.doan.ClicklistenerInterface.WithDrawBankListItemClicklistener
import com.example.doan.DataSource.LinkedBanks
import com.example.doan.databinding.MoneywithdrawbanklistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WithdrawBankslistFragment : Fragment() {

    private val db = Firebase.firestore
    private var _binding: MoneywithdrawbanklistBinding? = null
    private val binding get() = _binding!!
    var dataset: java.util.ArrayList<LinkedBanks>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MoneywithdrawbanklistBinding.inflate(inflater, container, false)
        dataset = ArrayList()
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        binding.withdrawbanklist.layoutManager = LinearLayoutManager(this.context)
        getbanklistfromdb()
        binding.withdrawbanklisttoolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        val view = binding.root
        return view
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
                    Log.d(
                        AccountsandSettingFragment.TAG,
                        "getbanklistfromdb: " + dataset.toString()
                    )
                    binding.withdrawbanklist.adapter =
                        WithdrawBankListAdapter(dataset, withDrawBankListItemClicklistener)
                }
        }
    }

    val withDrawBankListItemClicklistener = object : WithDrawBankListItemClicklistener {
        override fun onWithDrawBanklistItemClick(pos: Int) {
            val action =
                WithdrawBankslistFragmentDirections.actionWithdrawBankslistFragmentToWithDrawFragment(
                    dataset?.get(pos) as LinkedBanks
                )
            findNavController().navigate(action)
        }

    }

}