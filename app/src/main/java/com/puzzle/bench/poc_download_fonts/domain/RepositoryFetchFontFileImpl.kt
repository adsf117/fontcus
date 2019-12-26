package com.puzzle.bench.poc_download_fonts.domain

import com.puzzle.bench.poc_download_fonts.data.remote.RemoteDownloadFontFile
import com.puzzle.bench.poc_download_fonts.data.local.LocalFetchFontFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RepositoryFetchFontFileImpl constructor(
    private val remoteDownloadFontFile: RemoteDownloadFontFile,
    private val localFetchFontFile: LocalFetchFontFile
) : RepositoryFetchFontFile {
    override suspend fun getFontFile(fontName: String): FetchFontState = withContext(
        Dispatchers.IO
    ) {
        var fetchFontState =
            FetchFontState(null, "Error")
        if (localFetchFontFile.fontFileExists(fontName)) {
            fetchFontState = localFetchFontFile.getFontFile(fontName)
        } else {
            val response = remoteDownloadFontFile.downloadFontsFile(fontName)
            response.responseBody?.let { responseBody ->
                localFetchFontFile.saveFontFile(responseBody, fontName)
                fetchFontState = localFetchFontFile.getFontFile(fontName)
            }
        }
        return@withContext fetchFontState
    }

}
