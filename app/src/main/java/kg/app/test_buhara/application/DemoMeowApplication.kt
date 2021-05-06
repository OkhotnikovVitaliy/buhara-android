package kg.app.test_buhara.application

import android.app.Application
import kg.app.test_buhara.module.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DemoMeowApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Adding Koin modules to our application
        startKoin {
            androidContext(this@DemoMeowApplication)
            modules(appModules)
        }
    }
}