package com.andresestevez.recipes.di

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.usecases.GetLocalRecipes
import com.andresestevez.usecases.ToggleRecipeFavorite
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [ViewModelComponent::class], replaces = [MainViewModelModule::class])
class FakeMainViewModelModule {

    @Provides
    @ViewModelScoped
    fun getLocalRecipesProvider(recipesRepository: RecipesRepository) = GetLocalRecipes(recipesRepository)

    @Provides
    @ViewModelScoped
    fun toggleRecipeFavoriteProvider(recipesRepository: RecipesRepository) =
        ToggleRecipeFavorite(recipesRepository)
}