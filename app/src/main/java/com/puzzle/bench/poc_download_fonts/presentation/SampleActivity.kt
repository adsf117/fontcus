package com.puzzle.bench.poc_download_fonts.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Typeface
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.puzzle.bench.poc_download_fonts.R
import com.puzzle.bench.poc_download_fonts.data.remote.RemoteFetchFontFileImpl
import com.puzzle.bench.poc_download_fonts.data.local.LocalFetchFontFileImpl
import com.puzzle.bench.poc_download_fonts.data.remote.retrofit.RetrofitCliente
import com.puzzle.bench.poc_download_fonts.domain.RepositoryFetchFontFileImpl
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
            val fileRepo = repositoryFetchFontFile.getFontFile(fontName)
            if (fileRepo.error.isEmpty()) {
                try {
                    // Display font from SD
                    val typeFace = Typeface.createFromFile(fileRepo.fontFile)
                    MyText.typeface = typeFace
                } catch (e: Exception) {
                    Log.e("Error: ", e.message)
                }
            }
        }

    }

}
