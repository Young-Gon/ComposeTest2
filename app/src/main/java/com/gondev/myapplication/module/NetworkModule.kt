package com.gondev.myapplication.module

import android.app.Application
import com.gondev.myapplication.BuildConfig
import com.gondev.myapplication.model.api.ProductsAPI
import com.gondev.myapplication.model.repository.TokenRepository
import com.gondev.statemanager.adapter.ResponseAdapter
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CONNECT_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 15L
private const val READ_TIMEOUT = 15L

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json = Json {
        if (!BuildConfig.DEBUG)
            ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    @ExperimentalSerializationApi
    fun provideAPIService(application: Application,
                          tokenRepository: TokenRepository) =
        Retrofit.Builder()
            .baseUrl("http://d2bab9i9pr8lds.cloudfront.net")
            .addCallAdapterFactory(ResponseAdapter.Factory())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okhttpClient(application, tokenRepository))
            .build()
            .create(ProductsAPI::class.java)

    private fun okhttpClient(application: Application,
                             tokenRepository: TokenRepository) =
        OkHttpClient.Builder().apply {
            cache(Cache(application.cacheDir, 10 * 1024 * 1024))
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }

            addInterceptor(TokenInterceptor(tokenRepository))
        }.build()
}

class TokenInterceptor(
    private val tokenRepository: TokenRepository
): Interceptor {
    var token: String? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            tokenRepository.token.collect { token ->
                this@TokenInterceptor.token = token
            }
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().apply {
            if(token != null) {
                newBuilder().addHeader("Authorization", "Bearer $token").build()
            }
        }

        return chain.proceed(newRequest)
    }
}