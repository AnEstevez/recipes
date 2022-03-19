package com.andresestevez.recipes.di

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.usecases.GetRecipeById
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [ViewModelComponent::class], replaces = [DetailViewModelModule::class])
class FakeDetailViewModelModule {

    @Provides
    @ViewModelScoped
    fun getRecipeByIdProvider(recipesRepository: RecipesRepository) =
        GetRecipeById(recipesRepository)

}