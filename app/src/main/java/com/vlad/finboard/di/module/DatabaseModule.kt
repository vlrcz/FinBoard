package com.vlad.finboard.di.module

import android.app.Application
import androidx.room.Room
import com.vlad.finboard.core.data.db.CategoriesDao
import com.vlad.finboard.core.data.db.FinboardDatabase
import com.vlad.finboard.core.data.db.FinancesDao
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
    fun providesFinancesDao(db: FinboardDatabase): FinancesDao {
        return db.financesDao()
    }

    @Provides
    fun providesCategoriesDao(db: FinboardDatabase): CategoriesDao {
        return db.categoriesDao()
    }
}