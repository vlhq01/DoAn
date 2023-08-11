package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doan.Adapters.HistoryAdapter
import com.example.doan.Adapters.PaymentMethodAdapter
import com.example.doan.ClicklistenerInterface.PaymentMethodOnClickListener
import com.example.doan.DataSource.BalanceChanges
import com.example.doan.DataSource.BalanceChangesSort
import com.example.doan.DataSource.DateItem
import com.example.doan.DataSource.LinkedBanks
import com.example.doan.databinding.HistoryNotificationsFragmentsBinding
import com.example.doan.databinding.PaymentMethodBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PaymentMethodFragment : Fragment() {
    private val db = Firebase.firestore
    private var _binding: PaymentMethodBinding? = null
    private val binding get() = _binding!!
    var paymentmothodlist: MutableList<LinkedBanks> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PaymentMethodBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.paymentmethodrv.layoutManager = LinearLayoutManager(
            this.context,
            LinearLayoutManager.VERTICAL, false
        )
        val linkedBanksListViewModel = (activity as MainActivity).linkedBanksListViewModel
        linkedBanksListViewModel.linkedbankslist.observe(viewLifecycleOwner, Observer {
            paymentmothodlist = it
            binding.paymentmethodrv.adapter = PaymentMethodAdapter(
                paymentmothodlist as ArrayList<LinkedBanks>,
                paymentMethodOnClickListener
            )
        })
        binding.paymentmethodtoolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        return view
    }

    val paymentMethodOnClickListener = object : PaymentMethodOnClickListener {
        override fun onPaymentMethodClickListener(pos: Int) {
            val action =
                PaymentMethodFragmentDirections.actionPaymentMethodFragmentToMoneyInputFragment(
                    paymentmothodlist[pos]
                )
            findNavController().navigate(action)
        }
    }
}