package kg.buhara

import android.app.Application
import android.content.Context
import kg.buhara.di.networkModule
import kg.buhara.di.repositoryModule
import kg.buhara.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


/**
 * Created by DAS since 2019-07-31
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class Application : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(
                listOf(
                    repositoryModule,
                    networkModule,
                    viewModelModule
                )
            )
        }
    }
}