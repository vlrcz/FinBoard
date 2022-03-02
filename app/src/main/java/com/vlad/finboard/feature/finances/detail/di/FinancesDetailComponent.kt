package com.vlad.finboard.feature.finances.detail.di

import com.vlad.finboard.di.AppComponent
import com.vlad.finboard.feature.finances.detail.FinancesDetailFragment
import dagger.Component

@FinancesDetailScope
@Component(
    dependencies = [AppComponent::class]
)
interface FinancesDetailComponent {

    fun inject(financesDetailFragment: FinancesDetailFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): FinancesDetailComponent
    }
}