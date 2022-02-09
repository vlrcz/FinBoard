package com.vlad.finboard.di

import android.app.Application
import android.content.Context
import com.vlad.finboard.data.db.NotesDao
import com.vlad.finboard.di.module.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun notesDao(): NotesDao

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance application: Application
        ): AppComponent
    }
}