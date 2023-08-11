package com.example.doan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doan.databinding.QrFragmentBinding
import com.example.doan.utils.PermissionHelper
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject
import kotlin.Result


class QrFragment : Fragment() {
    private var _binding: QrFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var qrScanIntegrator: IntentIntegrator
    private lateinit var selectImageFromGalleryResult: ActivityResultLauncher<String>
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            ActivityResultCallback { isGranted ->
                if (isGranted) {
                    Log.d("TAG", ": requestPermissionLauncher granted")
                } else {
                    Log.d("TAG", ": requestPermissionLauncher denied")
                }
            })

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = QrFragmentBinding.inflate(inflater, container, false)
        setupScanner()
        performAction()
        selectImageFromGalleryResult = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                    context?.contentResolver, Uri.parse(it.toString())
                )

                val intArray = IntArray(bitmap.width * bitmap.height)
//copy pixel data from the Bitmap into the 'intArray' array
//copy pixel data from the Bitmap into the 'intArray' array
                bitmap.getPixels(
                    intArray,
                    0,
                    bitmap.width,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height
                )

                val source: LuminanceSource =
                    RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
                val binarybitmap = BinaryBitmap(HybridBinarizer(source))

                val reader: Reader = MultiFormatReader()
                val result = reader.decode(binarybitmap)
                val obj = JSONObject(result.text)
                val receivername = obj.getString("name")
                val receiverphone = obj.getString("phone")
                movetomoneytransferfragment(receivername, receiverphone)
            }
        }
        binding?.btnrescan?.setOnClickListener { performAction() }
        binding?.btnsfi?.setOnClickListener {
            scanfromimage()
        }
        val view = binding?.root
        return view
    }

    private fun setupScanner() {
        qrScanIntegrator = IntentIntegrator.forSupportFragment(this)
        qrScanIntegrator.setOrientationLocked(false)
    }


    private fun performAction() {
        // Code to perform action when button is clicked.
        qrScanIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                Toast.makeText(activity, R.string.result_not_found, Toast.LENGTH_LONG).show()
            } else {
                // If QRCode contains data.
                try {
                    // Converting the data to json format
                    val obj = JSONObject(result.contents)
                    val receivername = obj.getString("name")
                    val receiverphone = obj.getString("phone")
                    movetomoneytransferfragment(receivername, receiverphone)


                    // Show values in UI.


                } catch (e: JSONException) {
                    e.printStackTrace()

                    // Data not in the expected format. So, whole object as toast message.
                    Toast.makeText(activity, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun movetomoneytransferfragment(name: String, phone: String) {
        val action = QrFragmentDirections.actionQrFragmentToMoneyTransferFragment(name, phone)
        findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun scanfromimage() {
        when {
            this.context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_CONTACTS
                )
            } == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                loadimage()
            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                PermissionHelper.askPermission(
                    requestPermissionLauncher,
                    activity as MainActivity,
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun loadimage() {
        selectImageFromGalleryResult.launch("image/*")
    }
}