package com.puzzle.bench.poc_download_fonts.data.local

import android.os.Environment
import com.puzzle.bench.poc_download_fonts.domain.FetchFontState
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

    override suspend fun saveFontFile(byteArray: ByteArray, fontName: String): FetchFontState {
        writeToSD(byteArray, fontName)
        return FetchFontState(null)
    }

    private fun writeToSD(byteArray: ByteArray, fontName: String): Boolean {
        try {
            val stringName = "${File.separator}${fontName}"
            val futureStudioIconFile =
                File(Environment.getExternalStorageDirectory(), stringName)

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(40096)
                var fileSizeDownloaded: Long = 0

                inputStream = byteArray.inputStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    val read = inputStream.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
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