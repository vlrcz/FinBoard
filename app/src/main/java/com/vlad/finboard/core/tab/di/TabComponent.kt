package com.vlad.finboard.core.tab.di

import com.vlad.finboard.core.tab.TabFragment
import com.vlad.finboard.di.AppComponent
import dagger.Component

@TabScope
@Component(
    dependencies = [AppComponent::class]
)
interface TabComponent {

    fun inject(tabFragment: TabFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): TabComponent
    }
}