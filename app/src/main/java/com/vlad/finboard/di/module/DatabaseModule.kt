package com.vlad.finboard.di.module

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vlad.finboard.R
import com.vlad.finboard.data.db.CategoriesDao
import com.vlad.finboard.data.db.FinboardDatabase
import com.vlad.finboard.data.db.NotesDao
import com.vlad.finboard.data.db.models.CategoryEntity
import com.vlad.finboard.data.db.models.NotesType
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesFinboardDatabase(application: Application): FinboardDatabase {
        return Room.databaseBuilder(
            application,
            FinboardDatabase::class.java,
            FinboardDatabase.DB_NAME
        )
            .addCallback(callback())
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

   private fun callback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val contentValues = ContentValues()
                with(contentValues) {
                    put("id", 1L)
                    put("name", "CAFE")
                    put("type", NotesType.COSTS.toString())
                    put("color", R.color.purple_200)
                }

                db.insert(CategoryEntity.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, contentValues)
            }
        }
    }
}