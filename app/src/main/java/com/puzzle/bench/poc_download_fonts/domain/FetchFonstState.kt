package com.puzzle.bench.poc_download_fonts.domain

import java.io.File

const val NO_ERROR = ""

class FetchFontState(
    val fontFile: File?,
    val error: String = NO_ERROR //TODO create error for no permission and No space on SD
)
