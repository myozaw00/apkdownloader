package com.myozawoo.example

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myozawoo.apkdownloader.ApkInstaller
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

class MainActivity : AppCompatActivity() {

    private val READ_WRITE_EXTERNAL_STORAGE = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storagePermissionTask()
    }

    @AfterPermissionGranted(123)
    private fun storagePermissionTask() {

        if (EasyPermissions.hasPermissions(this, READ_WRITE_EXTERNAL_STORAGE[0]) &&
            EasyPermissions.hasPermissions(this, READ_WRITE_EXTERNAL_STORAGE[1])) {

            ApkInstaller().install(this,
                //url = "https://digitalonboard.s3-ap-southeast-1.amazonaws.com/android/Myanmar+News_v2.2_apkpure.com.apk",
                url = "https://digitalonboard.s3-ap-southeast-1.amazonaws.com/android/Myanmar+News_v2.2_apkpure.com.apk",
                appName = "MyanmarNews")



        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(
                    this,
                    123,
                    READ_WRITE_EXTERNAL_STORAGE[0],
                    READ_WRITE_EXTERNAL_STORAGE[1]
                )
                    .setRationale("Please provide require permissions to proceed next step.")
                    .build()


            )
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}
