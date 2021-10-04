package com.andresestevez.recipes.di

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.usecases.GetFavoriteRecipes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class FavFragmentViewModelModule {

    @Provides
    @ViewModelScoped
    fun getFavoriteRecipesProvider(recipesRepository: RecipesRepository) = GetFavoriteRecipes(recipesRepository)

}