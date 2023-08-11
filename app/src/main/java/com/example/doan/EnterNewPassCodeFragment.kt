package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doan.databinding.EnterNewpasscodeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import `in`.aabhasjindal.otptextview.OTPListener

class EnterNewPassCodeFragment : Fragment() {
    private var _binding: EnterNewpasscodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EnterNewpasscodeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.enternewpasscodeotpview.otpListener = object : OTPListener {

            override fun onInteractionListener() {
                binding.enternewpasscodeotpview.requestFocusOTP()
            }


            override fun onOTPComplete(otp: String?) {
                val action =
                    EnterNewPassCodeFragmentDirections.actionEnterNewPassCodeFragmentToReEnterPasscodeFragment(
                        otp.toString()
                    )
                findNavController().navigate(action)
            }

        }
        binding.enternewpasscodetoolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        return view
    }
}