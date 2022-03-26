package com.vlad.finboard.di

import android.app.Application
import android.content.Context
import com.vlad.finboard.app.App
import com.vlad.finboard.core.data.db.CategoriesDao
import com.vlad.finboard.core.data.db.FinboardDatabase
import com.vlad.finboard.core.data.db.FinancesDao
import com.vlad.finboard.di.module.DatabaseModule
import com.vlad.finboard.feature.categories.CategoriesManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun context(): Context
    fun financesDao(): FinancesDao
    fun categoriesDao(): CategoriesDao
    fun finboardDatabase(): FinboardDatabase
    fun categoriesManager(): CategoriesManager
    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance application: Application
        ): AppComponent
    }
}