package com.puzzle.bench.poc_download_fonts.presentation

import android.app.Application
import com.facebook.stetho.Stetho

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

    }
}
