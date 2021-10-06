package com.andresestevez.recipes.di

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.usecases.GetLocalRecipes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class MainViewModelModule {

    @Provides
    @ViewModelScoped
    fun getLocalRecipesProvider(recipesRepository: RecipesRepository) = GetLocalRecipes(recipesRepository)

}