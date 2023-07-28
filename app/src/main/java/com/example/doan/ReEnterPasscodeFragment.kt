package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doan.DataSource.Data
import com.example.doan.DataSource.TopUp
import com.example.doan.databinding.EnterNewpasscodeBinding
import com.example.doan.databinding.ReenterPasscodeBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import `in`.aabhasjindal.otptextview.OTPListener

class ReEnterPasscodeFragment : Fragment() {
    private var _binding: ReenterPasscodeBinding?=null
    private val binding get() = _binding!!
    private var newotp : String = ""
    private lateinit var functions: FirebaseFunctions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                newotp = it.getString("passcode").toString()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReenterPasscodeBinding.inflate(inflater,container,false)
        val view = binding.root
        functions = Firebase.functions
        binding.reenterpasscodeotp.otpListener = object : OTPListener {

            override fun onInteractionListener() {
                binding.reenterpasscodeotp.requestFocusOTP()
            }


            override fun onOTPComplete(otp: String?) {
                if(newotp == otp){

                    ChangePasscode(newotp)
                }
                else{
                    binding.reenterpasscodeotp.showError()
                    Toast.makeText(context,"Not matched with the new Passcode that you have entered,Please enter the code again", Toast.LENGTH_LONG)
                }

            }

        }
        return view
    }

    private fun ChangePasscode(newpasscode: String): Task<String> {
        val data = hashMapOf(
            "PassCode" to newpasscode
        )
        return functions.getHttpsCallable("ChangePasscode").call(data)
            .continueWith { task ->
                val result = task.result.data as String
                result
            }.addOnCompleteListener {
                val action =  ReEnterPasscodeFragmentDirections.actionReEnterPasscodeFragmentToAccountsandSettingFragment(

                )
                findNavController().navigate(action)
            }
    }
}