package com.puzzle.bench.poc_download_fonts.presentation

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import com.puzzle.bench.poc_download_fonts.domain.FetchFontStatus
import com.puzzle.bench.poc_download_fonts.presentation.di.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

fun CoroutineScope.loadTypeface(
    context: Context,
    fontName: String
): Deferred<LoadTypefaceState> {
    return async {
        val fileRepo = ServiceLocator.providerRepositoryFetchFontFile(context).getFontFile(fontName)
        when(fileRepo.error){
            is FetchFontStatus.NoError -> {
                fileRepo.fontFile?.let {
                    val typeFace = Typeface.createFromFile(it)
                    LoadTypefaceState(typeFace, LoadFontState.Success)
                } ?: LoadTypefaceState(state = LoadFontState.NoFoundFile)
            }
            is FetchFontStatus.MissingPermissions -> {
                LoadTypefaceState(state = LoadFontState.MissingPermissions)
            }
            is FetchFontStatus.LowDiskSpace -> {
                LoadTypefaceState(state = LoadFontState.LowDiskSpace)
            }
        }
    }
}

fun CoroutineScope.launchFonts(
    context: Context,
    view: TextView,
    fontName: String
): Deferred<LoadFontState> {
    return async {
        val fileRepo = ServiceLocator.providerRepositoryFetchFontFile(context).getFontFile(fontName)
        when(fileRepo.error){
            is FetchFontStatus.NoError -> {
                fileRepo.fontFile?.let {
                    val typeFace = Typeface.createFromFile(it)
                    view.typeface = typeFace
                    LoadFontState.Success
                } ?: LoadFontState.NoFoundFile
            }
            is FetchFontStatus.MissingPermissions -> {
                LoadFontState.MissingPermissions
            }
            is FetchFontStatus.LowDiskSpace -> {
                LoadFontState.LowDiskSpace
            }
        }
    }
}
