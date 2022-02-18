package com.vlad.finboard.di

import android.app.Application
import android.content.Context
import com.vlad.finboard.app.App
import com.vlad.finboard.data.db.CategoriesDao
import com.vlad.finboard.data.db.FinboardDatabase
import com.vlad.finboard.data.db.NotesDao
import com.vlad.finboard.data.db.models.CategoryMapper
import com.vlad.finboard.di.module.DatabaseModule
import com.vlad.finboard.presentation.notes.save.SaveNoteFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun notesDao(): NotesDao
    fun categoriesDao(): CategoriesDao
    fun finboardDatabase(): FinboardDatabase
    fun categoryMapper(): CategoryMapper

    fun inject(saveNoteFragment: SaveNoteFragment)
    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance application: Application
        ): AppComponent
    }
}