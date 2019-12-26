package com.puzzle.bench.poc_download_fonts.data.local

import com.puzzle.bench.poc_download_fonts.domain.FetchFontState
import okhttp3.ResponseBody

interface LocalFetchFontFile {
    suspend fun fontFileExists(fontName: String) : Boolean
    suspend fun getFontFile(fontName: String) : FetchFontState
    suspend fun saveFontFile(body: ResponseBody, fontName: String) : FetchFontState


}