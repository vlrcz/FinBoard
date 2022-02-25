package com.vlad.finboard.di

import android.app.Application
import android.content.Context
import androidx.viewbinding.ViewBinding
import com.vlad.finboard.app.App
import com.vlad.finboard.core.data.db.CategoriesDao
import com.vlad.finboard.core.data.db.FinboardDatabase
import com.vlad.finboard.core.data.db.FinancesDao
import com.vlad.finboard.di.module.DatabaseModule
import com.vlad.finboard.feature.finances.FinancesFragment
import com.vlad.finboard.feature.finances.FinancesMapper
import com.vlad.finboard.feature.finances.detail.FinancesDetailFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun financesDao(): FinancesDao
    fun categoriesDao(): CategoriesDao
    fun finboardDatabase(): FinboardDatabase
    fun inject(app: App)
    fun inject(financesDetailFragment: FinancesDetailFragment)
    fun inject(financesFragment: FinancesFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance application: Application
        ): AppComponent
    }
}