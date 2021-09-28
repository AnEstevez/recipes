package com.andresestevez.recipes.di

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.usecases.*
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun getFavoriteRecipesProvider(recipesRepository: RecipesRepository) = GetFavoriteRecipes(recipesRepository)

    @Provides
    fun getLocalRecipesProvider(recipesRepository: RecipesRepository) = GetLocalRecipes(recipesRepository)

    @Provides
    fun getRecipeByIdProvider(recipesRepository: RecipesRepository) = GetRecipeById(recipesRepository)

    @Provides
    fun getRecipesByNameProvider(recipesRepository: RecipesRepository) = GetRecipesByName(recipesRepository)

    @Provides
    fun toggleRecipeFavoriteProvider(recipesRepository: RecipesRepository) = ToggleRecipeFavorite(recipesRepository)
}