package com.vlad.finboard.di.module

import android.app.Application
import androidx.room.Room
import com.vlad.finboard.data.db.NotesDao
import com.vlad.finboard.data.db.NotesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesNotesDatabase(application: Application): NotesDatabase {
        return Room.databaseBuilder(
            application,
            NotesDatabase::class.java,
            NotesDatabase.DB_NAME
        )
            .build()
    }

    @Provides
    fun providesNotesDao(db: NotesDatabase): NotesDao {
        return db.notesDao()
    }
}