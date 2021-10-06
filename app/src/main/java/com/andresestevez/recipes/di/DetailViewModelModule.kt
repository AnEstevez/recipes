package com.andresestevez.recipes.di

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.usecases.GetRecipeById
import com.andresestevez.usecases.ToggleRecipeFavorite
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DetailViewModelModule {

    @Provides
    @ViewModelScoped
    fun getRecipeByIdProvider(recipesRepository: RecipesRepository) = GetRecipeById(recipesRepository)

    @Provides
    @ViewModelScoped
    fun toggleRecipeFavoriteProvider(recipesRepository: RecipesRepository) = ToggleRecipeFavorite(recipesRepository)

}