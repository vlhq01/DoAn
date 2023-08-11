package com.example.doan

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.doan.AccountsandSettingFragment.Companion.TAG
import com.example.doan.Adapters.MoneyTopUpAdapter
import com.example.doan.ClicklistenerInterface.TopUpValueItemClickListener
import com.example.doan.DataSource.LinkedBanks
import com.example.doan.DataSource.TopUp
import com.example.doan.DataSource.TopUpValue
import com.example.doan.DataSource.Transfer
import com.example.doan.databinding.MoneyinputFragmentBinding
import com.example.doan.utils.LoadingDialog
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MoneyInputFragment : Fragment() {
    private var _binding: MoneyinputFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataset: ArrayList<TopUpValue>
    private lateinit var functions: FirebaseFunctions
    private lateinit var linkpaymentmethod: LinkedBanks
    private lateinit var loader: LoadingDialog

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                linkpaymentmethod = it.getParcelable("paymentmethod", LinkedBanks::class.java)!!
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MoneyinputFragmentBinding.inflate(inflater, container, false)
        dataset = ArrayList<TopUpValue>()
        dataset.add(TopUpValue(100000))
        dataset.add(TopUpValue(200000))
        dataset.add(TopUpValue(300000))
        dataset.add(TopUpValue(500000))
        dataset.add(TopUpValue(1000000))
        dataset.add(TopUpValue(2000000))
        functions = Firebase.functions
        binding.pmmtll.setOnClickListener {
            val action =
                MoneyInputFragmentDirections.actionMoneyInputFragmentToPaymentMethodFragment()
            findNavController().navigate(action)
        }
        loader = LoadingDialog(requireContext())
        binding.topuptoolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.rvtopupvalue.adapter = MoneyTopUpAdapter(dataset, topUpValueItemClickListener)
        binding.rvtopupvalue.layoutManager = GridLayoutManager(context, 3)


        val view = binding.root
        binding.btntopup.setOnClickListener {
            if (linkpaymentmethod != LinkedBanks()) {
                showBottomSheetDialog()
            } else {
                Toast.makeText(
                    context,
                    "Please chose your top up payment method",
                    Toast.LENGTH_LONG
                )
            }

        }
        if (linkpaymentmethod != LinkedBanks()) {
            context?.let { Glide.with(it).load(linkpaymentmethod.imgurl).into(binding.imgmethod) }
            binding.pmcontent.text =
                linkpaymentmethod.bankname + " [" + linkpaymentmethod.banknumber + "]"
        }

        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        return view
    }

    val topUpValueItemClickListener = object : TopUpValueItemClickListener {
        override fun onTopUpValueItemClick(pos: Int) {
            Log.d(TAG, "onTopUpValueItemClick: " + dataset[pos].value.toString())
            binding.edtdepositamount.setText(dataset[pos].value.toString())
            context?.resources?.let { binding.edtdepositamount.setTextColor(it.getColor(R.color.orange)) }
            context?.resources?.let { binding.btntopup.setTextColor(it.getColor(R.color.white)) }
            context?.resources?.let { binding.btntopup.setBackgroundColor(it.getColor(R.color.orange)) }
        }
    }

    private fun TopUpMoney(
        amount: Int,
        date: String,
        odersn: String,
        referenceid: String,
        paymentmethodname: String
    ): Task<String> {
        val data = hashMapOf(
            "Amount" to amount,
            "OderSN" to odersn,
            "ReferenceID" to referenceid,
            "PaymentmethodName" to paymentmethodname,
            "Date" to date,
        )


        return functions.getHttpsCallable("TopUpMoney").call(data)
            .continueWith { task ->
                val result = task.result.data as String
                result
            }.addOnCompleteListener {
                loader.dismiss()
                val action =
                    MoneyInputFragmentDirections.actionMoneyInputFragmentToTopUpResultFragment(
                        TopUp(amount, date, paymentmethodname, odersn, referenceid)
                    )
                findNavController().navigate(action)
            }
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
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val current = LocalDateTime.now().format(formatter)
        return current
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
                    val odersn = getodersn()
                    val referenceid = getreferenceid()
                    val date = getdate()
                    val paymentmethodname = linkpaymentmethod.bankname
                    TopUpMoney(
                        binding.edtdepositamount.text.toString().toInt(),
                        date,
                        odersn,
                        referenceid,
                        paymentmethodname
                    )
                    bottomSheetDialog.dismiss()
                    loader.show()
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