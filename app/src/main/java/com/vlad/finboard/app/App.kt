package com.vlad.finboard.app

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.room.DatabaseConfiguration
import androidx.room.RoomDatabase
import androidx.viewbinding.BuildConfig
import com.vlad.finboard.R
import com.vlad.finboard.data.db.FinboardDatabase
import com.vlad.finboard.data.db.models.CategoryEntity
import com.vlad.finboard.data.db.models.NotesType
import com.vlad.finboard.di.AppComponent
import com.vlad.finboard.di.DaggerAppComponent
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class App: Application() {

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

        //пример
        val cat = CategoryEntity(2, "SPORT", NotesType.COSTS.toString(), R.color.purple_500)
        GlobalScope.launch {
            database.categoriesDao().insertCategory(cat)
        }
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }