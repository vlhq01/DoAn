package com.example.doan

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.doan.DataSource.LinkedBanks
import com.example.doan.DataSource.TopUp
import com.example.doan.DataSource.WithDraw
import com.example.doan.databinding.WithdrawfragmentBinding
import com.example.doan.utils.LoadingDialog
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WithDrawFragment : Fragment() {
    private var _binding: WithdrawfragmentBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var functions: FirebaseFunctions
    private lateinit var withdrawbank: LinkedBanks
    private lateinit var loader: LoadingDialog

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                withdrawbank = it.getParcelable("withdrawbank", LinkedBanks::class.java)!!
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WithdrawfragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        activity?.actionBar?.title = "Transfer"
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }



        context?.let {
            Glide.with(it).load(withdrawbank.imgurl).into(binding.imgwithdrawbankdetail)
        }
        binding.txtwithdrawbanknamedetail.text = withdrawbank.bankname
        binding.txtwithdrawbanknumberdetail.text = withdrawbank.banknumber
        binding.edtWithDrawAmount.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtwithdrawfinalamount.text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.withdrawtoolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.btnwithdrawpay.setOnClickListener { showBottomSheetDialog() }
        functions = Firebase.functions
        loader = LoadingDialog(requireContext())

        return view
    }

    fun getodersn(): String {
        val allowedChars = ('0'..'9')
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getreferenceid(): String {
        val allowedChars = ('0'..'9')
        return (1..15)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getdate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        return current
    }

    private fun withdrawMoney(
        amount: Int,
        date: String,
        odersn: String,
        referenceid: String,
        withdrawbankname: String,
        withdrawbanknumber: String
    ): Task<String> {
        val data = hashMapOf(
            "Amount" to amount,
            "OderSN" to odersn,
            "ReferenceID" to referenceid,
            "WithDrawBankName" to withdrawbankname,
            "WithDrawBankNumber" to withdrawbanknumber,
            "Date" to date,
        )


        return functions.getHttpsCallable("WithDrawMoney").call(data)
            .continueWith { task ->
                val result = task.result.data as String
                result
            }.addOnCompleteListener {
                loader.dismiss()
                val action =
                    WithDrawFragmentDirections.actionWithDrawFragmentToWithDrawResuktFragment(
                        WithDraw(
                            amount,
                            date,
                            withdrawbankname,
                            withdrawbanknumber,
                            odersn,
                            referenceid
                        )
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
                if (otp == mainViewModel.passcode.value) {
                    loader.show()
                    withdrawMoney(
                        binding.edtWithDrawAmount.text.toString().toInt(),
                        getdate(),
                        getodersn(),
                        getreferenceid(),
                        withdrawbank.bankname,
                        withdrawbank.banknumber
                    )
                    bottomSheetDialog.dismiss()

                } else {
                    otpTextView.showError()
                    otpTextView.otp = ""
                    Toast.makeText(context, "Wrong PassCode", Toast.LENGTH_LONG)
                }
            }
        }
        bottomSheetDialog.show()

    }
}