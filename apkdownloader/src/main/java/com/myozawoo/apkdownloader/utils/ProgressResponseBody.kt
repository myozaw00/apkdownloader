package com.myozawoo.apkdownloader.utils

import com.myozawoo.apkdownloader.ApkInstaller
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

class ProgressResponseBody internal constructor(
    private val responseBody: ResponseBody,
    private val progressListener: ApkInstaller.ProgressListener
) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead.toInt() != -1) bytesRead else 0
                progressListener.update(
                    totalBytesRead,
                    responseBody.contentLength(),
                    bytesRead .toInt()== -1
                )
                return bytesRead
            }
        }
    }
}