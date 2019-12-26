package com.puzzle.bench.poc_download_fonts.data.remote

import com.puzzle.bench.poc_download_fonts.data.remote.retrofit.DownloadFontsAPI

import java.lang.Exception

class RemoteFetchFontFileImpl constructor(private val api: DownloadFontsAPI) :
    RemoteDownloadFontFile {

    override suspend fun downloadFontsFile(fontName: String): FetchFonstsState  {
        return try {
            val responseBody = api.downloadFontFile(fontName)
            FetchFonstsState(responseBody)
        } catch (ex: Exception) {
            FetchFonstsState(
                null,
                ex.message.toString()
            )
        }
    }

}
