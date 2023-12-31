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
import com.example.doan.DataSource.Transfer
import com.example.doan.DataSource.TransferFrom
import com.example.doan.databinding.TransferfromResultBinding
import com.example.doan.databinding.TransferresultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TransferFromResultFragment : Fragment() {
    private var _binding: TransferfromResultBinding? = null
    private val binding get() = _binding!!
    private var data: TransferFrom = TransferFrom()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                data = it.getParcelable("transferfrominfo", TransferFrom::class.java)!!
                Log.d(AccountsandSettingFragment.TAG, "onCreate: " + data.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TransferfromResultBinding.inflate(inflater, container, false)
        binding.txttffamount.text = "+" +data.amount.toString()+"đ"
        context?.resources?.let { binding.txttffamount.setTextColor(it.getColor(R.color.green)) }
        binding.txttffdate.text = data.date
        binding.txttffsenderdt.text = data.sendername
        binding.txttffodersndt.text = data.odersn
        binding.txttffrefiddt.text = data.referenceid
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        val view = binding.root
        return view
    }
}