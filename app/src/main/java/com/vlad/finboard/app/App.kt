package com.vlad.finboard.app

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.viewbinding.BuildConfig
import com.vlad.finboard.core.data.db.FinboardDatabase
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.di.AppComponent
import com.vlad.finboard.di.DaggerAppComponent
import javax.inject.Inject
import timber.log.Timber

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var database: FinboardDatabase

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
        appComponent.inject(this)

        initDb()
    }

    private fun initDb() {
        database.query("select * from ${CategoryEntity.TABLE_NAME}", null)
    }//todo удалить
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }