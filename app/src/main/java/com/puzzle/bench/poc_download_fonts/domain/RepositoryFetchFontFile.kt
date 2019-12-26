package com.puzzle.bench.poc_download_fonts.domain

interface RepositoryFetchFontFile {
    suspend fun getFontFile(fontName: String): FetchFontState

}