package com.puzzle.bench.poc_download_fonts.data.local

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.Log
import androidx.core.app.ActivityCompat
import com.puzzle.bench.poc_download_fonts.domain.FetchFontState
import com.puzzle.bench.poc_download_fonts.domain.FetchFontStatus
import java.io.*


class LocalFetchFontFileImpl(private var context: Context) : LocalFetchFontFile {
    override suspend fun fontFileExists(fontName: String): Boolean {
        val file = File(getFontPhatName(fontName))
        return file.exists()
    }

    override suspend fun getFontFile(fontName: String): FetchFontState {
        if (!hasPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))) {
            return FetchFontState(null, FetchFontStatus.MissingPermissions)
        }
        val file = File(getFontPhatName(fontName))
        return FetchFontState(file)
    }

    override suspend fun saveFontFile(byteArray: ByteArray, fontName: String) =
        if (!hasPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)))
            FetchFontState(null, FetchFontStatus.MissingPermissions)
        else if (writeToSD(byteArray, fontName))
            FetchFontState(null, FetchFontStatus.NoError)
        else FetchFontState(null, FetchFontStatus.LowDiskSpace)

    private fun writeToSD(byteArray: ByteArray, fontName: String): Boolean {
        if (getAvailableInternalMemorySize() < byteArray.size) {
            return false
        }

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

    private fun hasPermissions(permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    fun getAvailableInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long
        val availableBlocks: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.blockSizeLong
            availableBlocks = stat.availableBlocksLong
        } else {
            blockSize = stat.blockSize.toLong()
            availableBlocks = stat.availableBlocks.toLong()
        }
        return availableBlocks * blockSize
    }
}