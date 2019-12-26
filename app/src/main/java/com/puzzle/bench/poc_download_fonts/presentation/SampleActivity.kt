package com.puzzle.bench.poc_download_fonts.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.puzzle.bench.poc_download_fonts.R
import com.puzzle.bench.poc_download_fonts.data.local.LocalFetchFontFileImpl
import com.puzzle.bench.poc_download_fonts.data.remote.RemoteFetchFontFileImpl
import com.puzzle.bench.poc_download_fonts.data.remote.retrofit.RetrofitCliente
import com.puzzle.bench.poc_download_fonts.domain.NO_ERROR
import com.puzzle.bench.poc_download_fonts.domain.RepositoryFetchFontFileImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class SampleActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    private var repositoryFetchFontFile =
        RepositoryFetchFontFileImpl(
            RemoteFetchFontFileImpl(RetrofitCliente().makeServiceDownloadFontsAPI()),
            LocalFetchFontFileImpl()
        ) //TODO move this into a Service Locator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFont()
    }

    private fun loadFont() {
        mJob = Job()
        launch {
            val fontName = "Xtrusion%20(BRK).ttf"

            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
            )

            if (hasPermissions(applicationContext, permissions)) {
                val fileRepo = repositoryFetchFontFile.getFontFile(fontName)
                if (fileRepo.error == NO_ERROR) {
                    try {
                        // Display font from SD
                        val typeFace = Typeface.createFromFile(fileRepo.fontFile)
                        MyText.typeface = typeFace
                    } catch (e: Exception) {
                        Log.e("Error loading font: ", e.message)
                    }
                }
            } else {
                Toast.makeText(applicationContext, "missing permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
}
