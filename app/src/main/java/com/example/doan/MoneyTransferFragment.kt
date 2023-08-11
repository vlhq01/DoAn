package com.example.doan


import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doan.AccountsandSettingFragment.Companion.TAG
import com.example.doan.DataSource.Transfer
import com.example.doan.databinding.MoneytransferFragmentBinding
import com.example.doan.utils.LoadingDialog
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MoneyTransferFragment : Fragment() {
    private var _binding: MoneytransferFragmentBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var receiverName: String
    private lateinit var receiverPhone: String
    private var receiverId = ""
    private var receivername = ""
    private lateinit var functions: FirebaseFunctions
    private var senderBalance = 0
    private var receiverBalance = 0

    private lateinit var loader: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            if (it != null) {
                receiverName = it.getString("name").toString()
                receiverPhone = it.getString("phone").toString()
                Toast.makeText(context, receiverPhone, Toast.LENGTH_LONG)
            }
        }


    }

    override fun onResume() {
        super.onResume()
        db.collection("users").whereEqualTo("phone", receiverPhone).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    receiverId = document.id
                    receivername = document.getString("name").toString()
                }
            }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoneytransferFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        loader = LoadingDialog(requireContext())
        binding.txtReceiverTransferName.text = receiverName
        binding.txtReceiverTransferPhone.text = receiverPhone
        activity?.actionBar?.title = "Transfer"
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = false
        }
        binding.transferdetailtoolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.btnTransfer.setOnClickListener({ showBottomSheetDialog() })
        functions = Firebase.functions


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
                if (otp == mainViewModel.passcode.value) {
                    val odersn = getodersn()
                    val referenceid = getreferenceid()
                    val date = getdate()
                    loader.show()
                    TransferMoney(
                        binding.edtTransferAmount.text.toString().toInt(),
                        receiverId,
                        odersn,
                        referenceid,
                        date
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

    private fun TransferMoney(
        amount: Int,
        receiverid: String,
        odersn: String,
        referenceid: String,
        date: String
    ): Task<String> {
        val data = hashMapOf(
            "Amount" to amount,
            "OderSN" to odersn,
            "ReferenceID" to referenceid,
            "ReceiverID" to receiverid,
            "Date" to date,
        )
        return functions.getHttpsCallable("TransfertoAnotherAccount").call(data)
            .continueWith { task ->
                val result = task.result.data as String
                result
            }.addOnCompleteListener {
                loader.dismiss()
                val action =
                    MoneyTransferFragmentDirections.actionMoneyTransferFragmentToTransferResultFragment(
                        Transfer(amount, date, odersn, receivername, referenceid)
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
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        return current
    }

    private fun localtransfer(amount: Int, receiverid: String) {
        val user = FirebaseAuth.getInstance().currentUser
        Log.d(TAG, receiverid)
        val senderdocref = user?.let { db.collection("users").document(it.uid) }
        val receiverdocref = db.collection("users").document(receiverid)
        if (user != null) {
            db.runTransaction { transaction ->
                val sendersnapshot = senderdocref?.let { transaction.get(it) }
                val receiversnapshot = transaction.get(receiverdocref)
                if (sendersnapshot != null) {
                    senderBalance = sendersnapshot.get("balance").toString().toInt()
                    receiverBalance = receiversnapshot.get("balance").toString().toInt()
                    Log.d(TAG, receiverBalance.toString())

                    if (senderBalance > amount) {
                        transaction.update(senderdocref, "balance", senderBalance - amount)

                        transaction.update(receiverdocref, "balance", receiverBalance + amount)
                    } else {
                        Toast.makeText(context, "Balance is not enough", Toast.LENGTH_LONG)
                    }
                }
            }

        }
    }


}

