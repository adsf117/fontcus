package com.puzzle.bench.poc_download_fonts.presentation

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.puzzle.bench.poc_download_fonts.R
import kotlinx.android.synthetic.main.activity_main.MyText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class SampleActivity : AppCompatActivity(), CoroutineScope {

    private val mJob by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        launch {
              (application as SampleApplication).channel.consume {
                val typeFace = this.receive()
                  applyFont(typeFace)
              }
        }

    }

    private fun loadFont() {
        launch {
            val result =  launchFonts(applicationContext,MyText,"Xtrusion%20(BRK).ttf")
            when(result.await()){
                LoadFontState.NoFoundFile -> Log.e("Error loading font: ", "file null")
                LoadFontState.MissingPermissions -> Toast.makeText(this@SampleActivity, "missing permissions", Toast.LENGTH_SHORT).show()
                LoadFontState.LowDiskSpace -> Toast.makeText(this@SampleActivity, "low space in disck", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun applyFont(typeface:Typeface) {
        MyText.typeface = typeface
    }

}
