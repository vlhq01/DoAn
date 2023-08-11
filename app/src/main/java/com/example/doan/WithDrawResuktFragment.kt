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
import com.example.doan.DataSource.TopUp
import com.example.doan.DataSource.WithDraw

import com.example.doan.databinding.WithdrawresultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class WithDrawResuktFragment : Fragment() {
    private var _binding: WithdrawresultBinding? = null
    private val binding get() = _binding!!
    private var data: WithDraw = WithDraw()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                data = it.getParcelable("withdrawinfo", WithDraw::class.java)!!
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = WithdrawresultBinding.inflate(inflater, container, false)
        binding.txtwithdrawresultamount.text = "-" + data.amount.toString()+ "đ"
        binding.txtwithdrawresultdate.text = data.date
        binding.txtwithdrawbankname.text = data.bank
        binding.txtwithdrawbanknumber.text = data.banknumber
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        if (data.bank == "BIDV") {
            binding.imgwithdrawbankicon.setImageDrawable(context?.resources?.getDrawable(R.drawable.bidv))
        }

        if (data.bank == "Agribank") {
            binding.imgwithdrawbankicon.setImageDrawable(context?.resources?.getDrawable(R.drawable.agribank))
        }
        if (data.bank == "Vietcombank") {
            binding.imgwithdrawbankicon.setImageDrawable(context?.resources?.getDrawable(R.drawable.vietcambank))
        }

        if (data.bank == "Viettinbank") {
            binding.imgwithdrawbankicon.setImageDrawable(context?.resources?.getDrawable(R.drawable.viettinbank))
        }
        binding.imgwithdrawhomeicon.setOnClickListener {
            val action =
                WithDrawResuktFragmentDirections.actionWithDrawResuktFragmentToMainFragment()
            findNavController().navigate(action)
        }
        binding.txtwithdrawodersndt.text = data.odersn
        binding.txtwithdrawrefiddt.text = data.referenceid
        val view = binding.root
        return view
    }
}