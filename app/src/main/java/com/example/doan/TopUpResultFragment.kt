package com.example.doan

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doan.DataSource.TopUp
import com.example.doan.DataSource.Transfer
import com.example.doan.databinding.TopupresultBinding
import com.example.doan.databinding.TransferresultBinding

class TopUpResultFragment : Fragment() {
    private var _binding: TopupresultBinding?=null
    private val binding get() = _binding!!
    private var data : TopUp = TopUp()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if(it!= null){
                data = it.getParcelable("topupinfo" , TopUp::class.java)!!
                Log.d(AccountsandSettingFragment.TAG, "onCreate: " + data.toString())
            }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = TopupresultBinding.inflate(inflater,container,false)
        binding.txttopupamount.text = data.amount.toString()
        binding.txttopupdate.text = data.date
        binding.txttopupbankname.text = data.bank
        if(binding.txttopupbankname.text == "BIDV"){
            binding.imgtopupbankicon.setImageResource(R.drawable.bidv)
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