package com.myozawoo.apkdownloader.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

object ApkUtils {

    fun installApk(context: Context, apkFile: File) {

        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", apkFile)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            val uri = getApkUri(apkFile)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        }

        context.startActivity(intent)
    }

    private fun getApkUri(apkFile: File): Uri {
        try {
            val command = listOf("chmod", "777", apkFile.toString())
            val builder = ProcessBuilder(command)
            builder.start()
        }catch (e: IOException) {
            e.printStackTrace()
        }
        val uri = Uri.fromFile(apkFile)
        return uri
    }

}