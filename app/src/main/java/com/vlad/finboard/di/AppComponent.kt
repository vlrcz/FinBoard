package com.vlad.finboard.di

import android.app.Application
import android.content.Context
import com.vlad.finboard.app.App
import com.vlad.finboard.core.data.db.CategoriesDao
import com.vlad.finboard.core.data.db.FinboardDatabase
import com.vlad.finboard.core.data.db.NotesDao
import com.vlad.finboard.di.module.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun notesDao(): NotesDao
    fun categoriesDao(): CategoriesDao
    fun finboardDatabase(): FinboardDatabase
    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance application: Application
        ): AppComponent
    }
}