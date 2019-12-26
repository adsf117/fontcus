package com.puzzle.bench.poc_download_fonts.domain

import java.io.File

const val NO_ERROR = ""
const val ERROR_WRITE_PERMISSIONS = "Write permission missing"
const val ERROR_NOT_ENOUGHT_SPACE = "Not enough space to store the file"

class FetchFontState(
    val fontFile: File?,
    val error: String = NO_ERROR //TODO create error for no permission and No space on SD
)
