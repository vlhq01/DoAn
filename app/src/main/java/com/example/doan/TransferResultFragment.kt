package com.example.doan

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doan.AccountsandSettingFragment.Companion.TAG
import com.example.doan.DataSource.Transfer
import com.example.doan.databinding.SignInBinding
import com.example.doan.databinding.TransferresultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TransferResultFragment : Fragment() {
    private var _binding: TransferresultBinding? = null
    private val binding get() = _binding!!
    private var data: Transfer = Transfer()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                data = it.getParcelable("transferinfo", Transfer::class.java)!!
                Log.d(TAG, "onCreate: " + data.toString())
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TransferresultBinding.inflate(inflater, container, false)
        binding.txttfamount.text = data.amount.toString()
        binding.txttfdate.text = data.date
        binding.txttfreceiverdt.text = data.receivername
        binding.txttfodersndt.text = data.odersn
        binding.txttfrefiddt.text = data.referenceid
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        binding.imgtransferhomeicon.setOnClickListener({
            val action =
                TransferResultFragmentDirections.actionTransferResultFragmentToMainFragment()
            findNavController().navigate(action)
        })
        val view = binding.root
        return view
    }
}