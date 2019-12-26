package com.puzzle.bench.poc_download_fonts.data.remote

import okhttp3.ResponseBody

const val NO_ERROR = ""

class FetchFonstsState(
    val responseBody: ResponseBody?,
    val error: String = NO_ERROR
)
