package com.example.doan

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.doan.DataSource.Data
import com.example.doan.DataSource.Mobile
import com.example.doan.DataSource.Payment
import com.example.doan.DataSource.TopUp
import com.example.doan.databinding.MobileDataPaymentBinding
import com.example.doan.databinding.MobileDataTopupBinding
import com.example.doan.utils.LoadingDialog
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MobileDataPaymentFragment : Fragment() {
    private var _binding: MobileDataPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mobile: Mobile
    private lateinit var data: Data
    private lateinit var phonenumber: String
    private lateinit var carrier: String
    private lateinit var functions: FirebaseFunctions
    private var priceafterdiscount: Int = 0
    private lateinit var loader: LoadingDialog

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            if (it != null) {
                mobile = it.getParcelable("mobile", Mobile::class.java)!!
            }

            if (it != null) {
                data = it.getParcelable("data", Data::class.java)!!
            }
            if (it != null) {
                phonenumber = it.getString("phonenumber").toString()
            }
            if (it != null) {
                carrier = it.getString("carrier").toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MobileDataPaymentBinding.inflate(inflater, container, false)
        functions = Firebase.functions
        loader = LoadingDialog(requireContext())
        binding.txtmdpaymentcarriername.text = carrier
        binding.txtmdpaymentphonenumber.text = phonenumber
        binding.mobiledatapaymenttoolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        if (mobile.value != 0) {
            binding.txtcontent.text = "Mobile card: " + carrier + " " + mobile.value
            binding.txtdiscount.text = mobile.discount.toString() + "%"
            binding.txtsubtotal.text = mobile.value.toString()
            priceafterdiscount =
                (mobile.value - (mobile.value * (mobile.discount.toDouble() / 100))).toInt()
            val df = DecimalFormat("#,###")
            val stringtospan =
                SpannableString("đ" + df.format(priceafterdiscount.toInt()).toString())
            stringtospan.setSpan(UnderlineSpan(), 5, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.txtpnfinalprice.text = stringtospan
            binding.txttotalprice.text = stringtospan
        }
        if (data.value != 0) {
            binding.txtcontent.text =
                "Data card: " + carrier + " " + data.datacapacity + " GB " + data.duration + " Days"
            binding.txtdiscount.text = data.discount.toString() + "%"
            binding.txtsubtotal.text = data.value.toString()
            priceafterdiscount =
                (data.value - (data.value * (data.discount.toDouble() / 100))).toInt()
            val df = DecimalFormat("#,###")
            val stringtospan =
                SpannableString("đ" + df.format(priceafterdiscount.toInt()).toString())
            stringtospan.setSpan(UnderlineSpan(), 5, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.txtpnfinalprice.text = stringtospan
            binding.txttotalprice.text = stringtospan
        }

        binding.btnpaynow.setOnClickListener({ showBottomSheetDialog() })
        val view = binding.root

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
                bottomSheetDialog.dismiss()

                //TransferMoney( binding.edtTransferAmount.text.toString().toInt() , receiverId )

                val mainViewModel = (activity as MainActivity).mainViewModel
                if (otp == mainViewModel.passcode.value) {
                    loader.show()
                    val odersn = getodersn()
                    val referenceid = getreferenceid()
                    val merchantid = getmerchantid()
                    val date = getdate()
                    MakePayment(priceafterdiscount, odersn, referenceid, merchantid, date)

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

    private fun MakePayment(
        amount: Int,
        odersn: String,
        referenceid: String,
        merchantid: String,
        date: String
    ): Task<String> {
        val data = hashMapOf(
            "Amount" to amount,
            "OderSN" to odersn,
            "ReferenceID" to referenceid,
            "MerchantID" to merchantid,
            "Date" to date
        )
        return functions.getHttpsCallable("MakePayment").call(data)
            .continueWith { task ->
                val result = task.result.data as String
                result
            }.addOnCompleteListener {
                loader.dismiss()
                val action =
                    MobileDataPaymentFragmentDirections.actionMobileDataPaymentFragmentToPaymentResultFragment(
                        Payment(amount, date, odersn, merchantid, referenceid)
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

    fun getmerchantid(): String {
        val allowedChars = ('0'..'9') + ('a'..'z')
        return (1..20)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getdate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        return current
    }
}