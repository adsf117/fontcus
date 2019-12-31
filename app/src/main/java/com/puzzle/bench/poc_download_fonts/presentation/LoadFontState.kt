package com.puzzle.bench.poc_download_fonts.presentation

import android.graphics.Typeface

sealed class LoadFontState {
    object Success: LoadFontState()
    object NoFoundFile: LoadFontState()
    object MissingPermissions: LoadFontState()
    object LowDiskSpace: LoadFontState()
}

class LoadTypefaceState(
    val typeface: Typeface? = null,
    val state: LoadFontState
)