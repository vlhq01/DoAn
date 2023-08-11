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
import com.example.doan.DataSource.Payment
import com.example.doan.DataSource.Transfer
import com.example.doan.databinding.PaymentresultBinding
import com.example.doan.databinding.TransferresultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PaymentResultFragment : Fragment() {
    private var _binding: PaymentresultBinding? = null
    private val binding get() = _binding!!
    private var data: Payment = Payment()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                data = it.getParcelable("paymentinfo", Payment::class.java)!!
                Log.d(AccountsandSettingFragment.TAG, "onCreate: " + data.toString())
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = PaymentresultBinding.inflate(inflater, container, false)
        binding.txtpmamount.text = data.amount.toString()
        binding.txtpmdate.text = data.date
        binding.txtpmodersndt.text = data.odersn
        binding.txtpmrefiddt.text = data.referenceid
        binding.txtpmmeciddt.text = data.merchantid
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        binding.imgpaymenthomeicon.setOnClickListener({
            val action = PaymentResultFragmentDirections.actionPaymentResultFragmentToMainFragment()
            findNavController().navigate(action)
        })
        val view = binding.root
        return view
    }
}