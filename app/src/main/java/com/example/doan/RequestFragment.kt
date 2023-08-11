package com.example.doan

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.doan.databinding.RequestFragmentBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RequestFragment : Fragment() {
    private var _binding: RequestFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var qrbitmap: Bitmap
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RequestFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        val mainViewModel = (activity as MainActivity).mainViewModel
        mainViewModel.name.observe(viewLifecycleOwner, Observer {
            createQR(mainViewModel.name.value.toString(), mainViewModel.phone.value.toString())
        })
        mainViewModel.phone.observe(viewLifecycleOwner, Observer {
            createQR(mainViewModel.name.value.toString(), mainViewModel.phone.value.toString())
        })
        binding?.btnstoreqr?.setOnClickListener { storeImage(qrbitmap) }
        return view
    }

    private fun createQR(name: String, phone: String) {

        val jsoncontent = JSONObject()
        jsoncontent.put("name", name)
        jsoncontent.put("phone", phone)
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(jsoncontent.toString(), BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        qrbitmap = bitmap
        binding?.imgrequestqr?.setImageBitmap(bitmap)
    }

    /** Create a File for saving an image or video  */
    private fun getOutputMediaFile(): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir = File(
            (Environment.getExternalStorageDirectory().toString()
                    + "/Download/"

                    )
        )

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val timeStamp: String = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
        val mediaFile: File
        val mImageName = "MI_$timeStamp.jpg"
        mediaFile = File((mediaStorageDir.path + File.separator).toString() + mImageName)
        return mediaFile
    }

    private fun storeImage(image: Bitmap) {
        val pictureFile = getOutputMediaFile()
        if (pictureFile == null) {
            Log.d(
                TAG,
                "Error creating media file, check storage permissions: "
            ) // e.getMessage());
            return
        }
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "File not found: " + e.toString())
        } catch (e: IOException) {
            Log.d(TAG, "Error accessing file: " + e.toString())
        }
    }
}