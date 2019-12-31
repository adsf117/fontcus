package com.puzzle.bench.poc_download_fonts.presentation.di

import android.content.Context
import com.puzzle.bench.poc_download_fonts.data.local.LocalFetchFontFileImpl
import com.puzzle.bench.poc_download_fonts.data.remote.RemoteFetchFontFileImpl
import com.puzzle.bench.poc_download_fonts.data.remote.retrofit.RetrofitCliente
import com.puzzle.bench.poc_download_fonts.domain.RepositoryFetchFontFile
import com.puzzle.bench.poc_download_fonts.domain.RepositoryFetchFontFileImpl

object ServiceLocator {

    @Volatile
    private var repositoryFetchFontFile : RepositoryFetchFontFile? = null

    fun providerRepositoryFetchFontFile(context: Context): RepositoryFetchFontFile {
        synchronized(this) {
            return repositoryFetchFontFile
                ?: createRepositoryFetchFontFile(
                    context
                )
        }
    }

    private fun createRepositoryFetchFontFile(context: Context): RepositoryFetchFontFile {
        val repositoryFetchFontFileImpl = RepositoryFetchFontFileImpl(
            RemoteFetchFontFileImpl(RetrofitCliente().makeServiceDownloadFontsAPI()),
            LocalFetchFontFileImpl(context)
        )
        repositoryFetchFontFile = repositoryFetchFontFileImpl
        return repositoryFetchFontFileImpl
    }
}