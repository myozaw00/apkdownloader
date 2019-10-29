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
                url = "https://download.apkpure.com/b/apk/Y29tLmJ1cm1hY29ycC5tb2IubmV3ZGFpbHlfMTA4XzExOWM0YzI4?_fn=TXlhbm1hciBOZXdzX3YyLjJfYXBrcHVyZS5jb20uYXBr&k=487d5f3f6c1e19bb6d7e83ebc667fb5f5dba64ef&as=5d54c6baa2d09f542b81ec485ed35d7d5db7c267&_p=Y29tLmJ1cm1hY29ycC5tb2IubmV3ZGFpbHk&c=1%7CNEWS_AND_MAGAZINES%7CZGV2PWJ1cm1hY29ycCZ0PWFwayZzPTYwMTM3OTMmdm49Mi4yJnZjPTEwOA",
                appName = "Myanmar_News",
                description = "Please do not close the app.")



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
