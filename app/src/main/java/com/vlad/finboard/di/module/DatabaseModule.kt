package com.vlad.finboard.di.module

import android.app.Application
import androidx.room.Room
import com.vlad.finboard.data.db.CategoriesDao
import com.vlad.finboard.data.db.FinboardDatabase
import com.vlad.finboard.data.db.NotesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    companion object {
        const val CATEGORIES = "categories.db"
    }

    @Provides
    @Singleton
    fun providesFinboardDatabase(application: Application): FinboardDatabase {
        return Room.databaseBuilder(
            application,
            FinboardDatabase::class.java,
            FinboardDatabase.DB_NAME
        )
            .createFromAsset(CATEGORIES)
            .build()
    }

    @Provides
    fun providesNotesDao(db: FinboardDatabase): NotesDao {
        return db.notesDao()
    }

    @Provides
    fun providesCategoriesDao(db: FinboardDatabase): CategoriesDao {
        return db.categoriesDao()
    }
}