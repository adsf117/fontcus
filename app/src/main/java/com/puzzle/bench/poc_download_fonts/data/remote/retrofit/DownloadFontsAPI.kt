package com.puzzle.bench.poc_download_fonts.data.remote.retrofit

import okhttp3.ResponseBody
import retrofit2.http.Url
import retrofit2.http.GET
import retrofit2.http.Streaming

interface DownloadFontsAPI {
    @Streaming
    @GET
    suspend fun downloadFontFile(@Url fileUrl: String): ResponseBody
}
