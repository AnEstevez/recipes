package com.andresestevez.recipes.di

import android.app.Application
import com.andresestevez.recipes.ui.detail.DetailViewModelFactory
import com.andresestevez.recipes.ui.main.MainViewModel
import com.andresestevez.recipes.ui.main.fragments.FavViewModelFactory
import com.andresestevez.recipes.ui.main.fragments.LocalRecipesViewModelFactory
import com.andresestevez.recipes.ui.main.fragments.SearchViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, UseCasesModule::class, DataModule::class, ViewModelsModule::class])
interface RecipesComponent {

    val favViewModelFactory: FavViewModelFactory
    val searchViewModelFactory: SearchViewModelFactory
    val localRecipesViewModelFactory: LocalRecipesViewModelFactory
    val detailViewModelFactory: DetailViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): RecipesComponent
    }
}