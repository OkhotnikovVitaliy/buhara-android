package kg.buhara.di

import android.content.Intent
import android.util.Log
import kg.buhara.Application
import kg.buhara.Application.Companion.context
import kg.buhara.BuildConfig
import kg.buhara.utils.UserInfoPref
import kg.buhara.view.auth.LoginActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by DAS since 11/14/19
 *
 * Usage: functions to create network requirements such as okHttpClient
 *
 * How to call: just call [createNetworkClient] in AppInjector
 *
 */


private val sLogLevel =
    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

//private const val baseUrl = "http://buhara.sunrisetest.site/api/v1/"
private const val baseUrl = "http://82.146.41.217/api/v1/"
//private const val newBaseUrl = "http://151.80.71.13/api/v1/"

private fun getLogInterceptor() = HttpLoggingInterceptor().apply { level = sLogLevel }

fun createNetworkClient() = retrofitClient(baseUrl, okHttpClient(true))

private fun okHttpClient(addAuthHeader: Boolean) = OkHttpClient.Builder()
    .addInterceptor(getLogInterceptor()).apply {
        setTimeOutToOkHttpClient(
            this
        )
    }
    .addInterceptor(headersInterceptor(addAuthHeader)).build()

private fun retrofitClient(baseUrl: String, httpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create()) 
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

fun headersInterceptor(addAuthHeader: Boolean) = Interceptor { chain ->
    chain.proceed(
        chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .also {
                if (addAuthHeader) {
                    Log.e("HEADER", UserInfoPref.getAccessToken(Application.context))
                    it.addHeader("Authorization", UserInfoPref.getAccessToken(Application.context))
                }
            }
            .build()
    )


}

private fun setTimeOutToOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder) =
    okHttpClientBuilder.apply {
        readTimeout(30L, TimeUnit.SECONDS)
        connectTimeout(30L, TimeUnit.SECONDS)
        writeTimeout(30L, TimeUnit.SECONDS)
    }