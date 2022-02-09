package com.vlad.finboard.app

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.viewbinding.BuildConfig
import com.vlad.finboard.di.AppComponent
import com.vlad.finboard.di.DaggerAppComponent
import timber.log.Timber

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }

        appComponent = DaggerAppComponent
            .factory()
            .create(this, this)
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }