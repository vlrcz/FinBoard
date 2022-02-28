package com.vlad.finboard.feature.finances.list.di

import com.vlad.finboard.di.AppComponent
import com.vlad.finboard.feature.finances.list.FinancesFragment
import dagger.Component

@FinancesListScope
@Component(
    dependencies = [AppComponent::class]
)
interface FinancesListComponent {

    fun inject(financesFragment: FinancesFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): FinancesListComponent
    }
}