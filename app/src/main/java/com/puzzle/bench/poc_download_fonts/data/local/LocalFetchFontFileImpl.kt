package com.puzzle.bench.poc_download_fonts.data.local

import android.os.Environment
import android.util.Log
import com.puzzle.bench.poc_download_fonts.domain.FetchFontState
import okhttp3.ResponseBody
import java.io.*

class LocalFetchFontFileImpl : LocalFetchFontFile {
    override suspend fun fontFileExists(fontName: String): Boolean {
        val file = File(getFontPhatName(fontName))
        return file.exists()
    }

    override suspend fun getFontFile(fontName: String): FetchFontState {
        val file = File(getFontPhatName(fontName))
        return FetchFontState(file)
    }

    override suspend fun saveFontFile(
        body: ResponseBody,
        fontName: String
    ): FetchFontState {
        writeResponseBodyToDsk(body, fontName)
        return FetchFontState(null)
    }

    private fun writeResponseBodyToDsk(body: ResponseBody, fontName: String): Boolean {
        try {
            val stringname = "${File.separator}${fontName}"
            val futureStudioIconFile =
                File(Environment.getExternalStorageDirectory(), stringname)

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(40096)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    Log.d("", "file download: $fileSizeDownloaded of $fileSize")
                }

                outputStream.flush()

                return true
            } catch (e: IOException) {
                return false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            return false
        }

    }


    private fun getFontPhatName(fontName: String) = "/sdcard/${fontName}"
}