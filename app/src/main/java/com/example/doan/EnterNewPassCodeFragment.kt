package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doan.databinding.EnterNewpasscodeBinding
import `in`.aabhasjindal.otptextview.OTPListener

class EnterNewPassCodeFragment : Fragment() {
    private var _binding: EnterNewpasscodeBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EnterNewpasscodeBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.enternewpasscodeotpview.otpListener = object : OTPListener {

            override fun onInteractionListener() {
                binding.enternewpasscodeotpview.requestFocusOTP()
            }


            override fun onOTPComplete(otp: String?) {
                    val action = EnterNewPassCodeFragmentDirections.actionEnterNewPassCodeFragmentToReEnterPasscodeFragment(otp.toString())
                    findNavController().navigate(action)
            }

        }
        return view
    }
}