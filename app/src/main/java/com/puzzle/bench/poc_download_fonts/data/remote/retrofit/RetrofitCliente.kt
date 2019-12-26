package com.puzzle.bench.poc_download_fonts.data.remote.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.facebook.stetho.okhttp3.StethoInterceptor


class RetrofitCliente {

    companion object FactoryDownloadFontsAPI {

        private fun getOkHttpClientBuilder(): OkHttpClient {
            return OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()
        }
    }

    fun makeServiceDownloadFontsAPI(): DownloadFontsAPI {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(getOkHttpClientBuilder())
            .baseUrl("http://webpagepublicity.com/free-fonts/x/")
            .build()
        return retrofit.create(DownloadFontsAPI::class.java)
    }
}
