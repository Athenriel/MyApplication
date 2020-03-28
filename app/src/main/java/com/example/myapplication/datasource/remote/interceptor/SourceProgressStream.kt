package com.example.myapplication.datasource.remote.interceptor

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datasource.remote.model.ProgressDownloadModel
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException
import kotlin.math.roundToInt

/**
 * Created by Athenriel on 25/03/2020.
 * A one-shot stream from the origin server to the client application with the raw bytes of the
 * response body. Each response body shows the progress of each request
 */
class SourceProgressStream(private val responseBody: ResponseBody?,
                           private val progressListener: MutableLiveData<ProgressDownloadModel>?,
                           private val resourceId: String?) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return responseBody?.contentLength() ?: 0L
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = sourceRead(responseBody!!.source()).buffer()
        }
        return bufferedSource as BufferedSource
    }

    private fun sourceRead(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                responseBody?.contentLength()?.let {
                    val percentage = (100 * totalBytesRead / it).toFloat().roundToInt()
                    progressListener?.postValue(
                        ProgressDownloadModel(
                            percentage,
                            resourceId
                        )
                    )
                }
                return bytesRead
            }
        }
    }

}
