package com.example.doan

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.doan.AccountsandSettingFragment.Companion.TAG
import com.example.doan.Adapters.BankLinkingAdapter
import com.example.doan.DataSource.Banks
import com.example.doan.DataSource.TopUp
import com.example.doan.databinding.AddbankaccountFragmentBinding
import com.example.doan.databinding.BanklistBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView

class AddBankDetailFragment: Fragment() {
    private var _binding: AddbankaccountFragmentBinding?=null
    private val binding get() = _binding!!
    private lateinit var data : Banks
    var dataset: java.util.ArrayList<Banks>? = null
    private lateinit var functions: FirebaseFunctions
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                data = it.getParcelable("bank" , Banks::class.java)!!
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddbankaccountFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        functions = Firebase.functions
        context?.let { Glide.with(it).load(data.imgUrl).into(binding.imgbankdetailicon) }



        binding.btnVerify.setOnClickListener {
            showBottomSheetDialog()
        }
        return view
    }

    private fun addbanktodb(imgurl: String, bankname: String, banknumber:String, ownername:String): Task<String> {
        val data = hashMapOf(
            "BankName" to bankname,
            "BankNumber" to banknumber,
            "BankImgUrl" to imgurl,
            "OwnerName" to ownername
        )
        Log.d(TAG, "addbanktodb: " + banknumber +"   " + ownername)
        return functions.getHttpsCallable("AddBankAccounts").call(data)
            .continueWith { task ->
                val result = task.result.data as String
                result
            }.addOnCompleteListener {
                val action =  AddBankDetailFragmentDirections.actionAddBankDetailFragmentToWalletFragment(
                )
                findNavController().navigate(action)
            }
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
                    val imgurl = data.imgUrl
                    val bankname = data.bankname
                    var banknumber = binding.edtAccountNumber.text.toString()
                    var ownername = binding.edtOwnerName.text.toString()
                    addbanktodb(imgurl,bankname, banknumber, ownername)

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