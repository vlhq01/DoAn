package com.example.doan.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class PermissionHelper {
    companion object{
       fun askPermission(
           requestPermissionLauncher: ActivityResultLauncher<String>,
           activity: Activity,
           permission : String
       ){
          if(ContextCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED){

          }else if(activity.shouldShowRequestPermissionRationale(permission)){

          }else{
              requestPermissionLauncher.launch(permission)
          }
       }
    }
}