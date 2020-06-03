package kg.buhara.di

import kg.buhara.data.remote.Api
import org.koin.dsl.module
import retrofit2.Retrofit


/**
 * Created by DAS since 12/27/18
 *
 * Usage: this methods handle DI
 *
 * How to call: just startActivityForResult koin in application class
 *
 */

private val retrofit: Retrofit = createNetworkClient()

private val REST_API: Api = retrofit.create(Api::class.java)

val networkModule = module {
    single { REST_API }
}