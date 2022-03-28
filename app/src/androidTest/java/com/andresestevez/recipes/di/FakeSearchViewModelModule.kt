package com.andresestevez.recipes.di

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.usecases.GetRecipesByName
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [ViewModelComponent::class], replaces = [SearchViewModelModule::class])
class FakeSearchViewModelModule {

    @Provides
    @ViewModelScoped
    fun getRecipesByNameProvider(recipesRepository: RecipesRepository): GetRecipesByName =
        GetRecipesByName(recipesRepository)

}