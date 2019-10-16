package com.myozawoo.apkdownloader

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.myozawoo.apkdownloader.utils.ApkUtils
import com.myozawoo.apkdownloader.utils.ProgressResponseBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.closeQuietly
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.lang.Exception
import okio.*


class ApkInstaller  {

    fun install(context: Context, url: String, appName: String) {

        val mDialog = AlertDialog.Builder(context)
            .setTitle(appName)
            .setMessage("Downloading file (0%)")
            .setCancelable(false)
            .create()


        try {
            GlobalScope.launch(Dispatchers.IO) {
                var downloaded: File? = null
                async(Dispatchers.IO) {
                    val okHttpClient = OkHttpClient.Builder()
                    val httpLogginInterceptor = HttpLoggingInterceptor()
                    httpLogginInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
                    httpLogginInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    okHttpClient.addNetworkInterceptor(httpLogginInterceptor)
                    okHttpClient.addNetworkInterceptor{ chain ->
                        val originalResponse = chain.proceed(chain.request())
                        originalResponse.newBuilder()
                            .body(
                                ProgressResponseBody(
                                    originalResponse.body!!,
                                    object : ProgressListener {
                                        override fun update(
                                            bytesRead: Long,
                                            contentLength: Long,
                                            done: Boolean
                                        ) {
                                            GlobalScope.launch(Dispatchers.Main) {
                                                val progress =
                                                    ((100 * bytesRead) / contentLength).toInt()
                                                mDialog.setMessage("Downloading file ($progress%)")
                                            }

                                        }
                                    })
                            )
                            .build()
                    }
                    val request = Request.Builder()
                        .url(url)
                        .build()
                    GlobalScope.launch (Dispatchers.Main){ mDialog.show() }
                    val response = okHttpClient.build().newCall(request).execute()
                    val contentLength = response.body?.contentLength()
                    print("ContentLength: $contentLength")
                    val source = response.body?.source()
                    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "$appName.apk")
                    val sink = file.sink().buffer()
                    val sinkBuffer = sink.buffer
                    var totalBytesRead: Long = 0
                    var bytesRead: Long?
                    val bufferSize: Long = 8*1024
                    do {
                        bytesRead = source?.read(sinkBuffer, bufferSize)
                        if (bytesRead?.toInt() == -1) {
                            print("Source is null")
                            break
                        }
                        sink.emit()
                        totalBytesRead += bytesRead!!
                        val progress = (totalBytesRead * 100) / contentLength!!

                    }while (bytesRead?.toInt() != -1)

                    sink.flush()
                    sink.closeQuietly()
                    source?.closeQuietly()
                    mDialog.dismiss()
                    downloaded = file

                }.await()

                try {
                    ApkUtils.installApk(context, downloaded ?: throw RuntimeException("Invalid file format."))
                } catch (e: Exception) {
                    mDialog.dismiss()
                    e.printStackTrace()
                } finally {
                    if (mDialog.isShowing) mDialog.dismiss()
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun print(message: String) {
        Log.d("ApkInstaller", message)
    }

    interface ProgressListener {
        fun update(bytesRead: Long, contentLength: Long, done: Boolean)
    }





}