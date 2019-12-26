package com.puzzle.bench.poc_download_fonts.data.remote


interface RemoteDownloadFontFile {
    suspend fun downloadFontsFile(fontName: String): FetchFonstsState

}
