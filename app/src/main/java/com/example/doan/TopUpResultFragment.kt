package com.example.doan

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doan.DataSource.TopUp
import com.example.doan.DataSource.Transfer
import com.example.doan.databinding.TopupresultBinding
import com.example.doan.databinding.TransferresultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TopUpResultFragment : Fragment() {
    private var _binding: TopupresultBinding? = null
    private val binding get() = _binding!!
    private var data: TopUp = TopUp()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                data = it.getParcelable("topupinfo", TopUp::class.java)!!
                Log.d(AccountsandSettingFragment.TAG, "onCreate: " + data.toString())
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = TopupresultBinding.inflate(inflater, container, false)
        binding.txttopupamount.text = data.amount.toString()
        binding.txttopupdate.text = data.date
        binding.txttopupbankname.text = data.bank
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        if (data.bank == "BIDV") {
            binding.imgtopupbankicon.setImageDrawable(context?.resources?.getDrawable(R.drawable.bidv))
        }

        if (data.bank == "Agribank") {
            binding.imgtopupbankicon.setImageDrawable(context?.resources?.getDrawable(R.drawable.agribank))
        }
        if (data.bank == "Vietcombank") {
            binding.imgtopupbankicon.setImageDrawable(context?.resources?.getDrawable(R.drawable.vietcambank))
        }

        if (data.bank == "Viettinbank") {
            binding.imgtopupbankicon.setImageDrawable(context?.resources?.getDrawable(R.drawable.viettinbank))
        }
        binding.imgtopuphomeicon.setOnClickListener({
            val action = TopUpResultFragmentDirections.actionTopUpResultFragmentToMainFragment()
            findNavController().navigate(action)
        })
        binding.txttopupodersndt.text = data.odersn
        binding.txttopuprefiddt.text = data.referenceid
        val view = binding.root
        return view
    }
}