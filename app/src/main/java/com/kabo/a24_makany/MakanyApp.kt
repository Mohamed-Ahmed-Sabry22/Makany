package com.kabo.a24_makany

import android.app.Application
import com.kabo.a24_makany.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MakanyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MakanyApp)
            modules(appModule)
        }
    }
}