package com.puzzle.bench.poc_download_fonts.domain

import java.io.File

class FetchFontState(
    val fontFile: File?,
    val error: FetchFontStatus = FetchFontStatus.NoError
)

sealed class FetchFontStatus {
    object NoError : FetchFontStatus()
    object MissingPermissions : FetchFontStatus()
    object LowDiskSpace : FetchFontStatus()
}
