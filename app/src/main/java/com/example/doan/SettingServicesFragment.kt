package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doan.databinding.ReenterPasscodeBinding
import com.example.doan.databinding.SettingServiceBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView

class SettingServicesFragment: Fragment() {
    private var _binding: SettingServiceBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingServiceBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.PasscodeChangingConstraintLayout.setOnClickListener {
            showBottomSheetDialog()
        }
        return view
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this.requireContext())
        bottomSheetDialog.setContentView(R.layout.password_bottomsheetdialod)
        val otpTextView: OtpTextView
        otpTextView = bottomSheetDialog.findViewById(R.id.otp_view)!!
        otpTextView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                otpTextView.requestFocusOTP()
            }

            override fun onOTPComplete(otp: String) {
                otpTextView.requestFocusOTP()
                //Toast.makeText(requireContext(), "otp inputed", Toast.LENGTH_LONG).show()


                //TransferMoney( binding.edtTransferAmount.text.toString().toInt() , receiverId )
                val mainViewModel = (activity as MainActivity).mainViewModel
                if(otp == mainViewModel.passcode.value){
                    val action =  SettingServicesFragmentDirections.actionSettingServicesFragmentToEnterNewPassCodeFragment()
                    findNavController().navigate(action)
                    bottomSheetDialog.dismiss()
                }
                else{
                    otpTextView.showError()
                    otpTextView.otp= ""
                    Toast.makeText(context,"Wrong PassCode", Toast.LENGTH_LONG)
                }


            }
        }
        bottomSheetDialog.show()

    }
}