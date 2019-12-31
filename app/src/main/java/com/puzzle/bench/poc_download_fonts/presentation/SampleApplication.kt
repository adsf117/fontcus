package com.puzzle.bench.poc_download_fonts.presentation

import android.app.Application
import android.graphics.Typeface
import android.util.Log
import android.widget.Toast
import com.facebook.stetho.Stetho
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SampleApplication : Application(), CoroutineScope {

    private val mJob by lazy { Job() }
    val channel = ConflatedBroadcastChannel<Typeface>()
    val fontName = "Xtrusion%20(BRK).ttf"

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        launch {
            val result =  loadTypeface(applicationContext,fontName).await()
            when(result.state){
                LoadFontState.Success -> result.typeface?.let { channel.send(it) }
                LoadFontState.NoFoundFile -> {
                    channel.close()
                    Log.e("Error loading font: ", "file null")
                }
                LoadFontState.MissingPermissions ->{
                    channel.close()
                    Toast.makeText(applicationContext, "missing permissions", Toast.LENGTH_SHORT).show()
                }
                LoadFontState.LowDiskSpace -> {
                    channel.close()
                    Toast.makeText(applicationContext, "low space in disck", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
