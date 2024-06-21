package com.capstone.aquamate.progressrequest

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File

class ProgressRequestBody (
    private val file: File,
    private val progressListener: (progress: Int) -> Unit
) : RequestBody() {

    override fun contentType() = "image/jpeg".toMediaTypeOrNull()

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = file.inputStream()
        var uploaded = 0L

        inputStream.use { input ->
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                progressListener((100 * uploaded / fileLength).toInt())
                uploaded += read
                sink.write(buffer, 0, read)
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}
