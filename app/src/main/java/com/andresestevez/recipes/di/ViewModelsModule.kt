package com.andresestevez.recipes.di

import com.andresestevez.recipes.ui.detail.DetailViewModelFactory
import com.andresestevez.recipes.ui.main.MainViewModel
import com.andresestevez.recipes.ui.main.fragments.*
import com.andresestevez.usecases.*
import dagger.Module
import dagger.Provides

@Module
class ViewModelsModule {

    @Provides
    fun favViewModelProvider(getFavoriteRecipes: GetFavoriteRecipes) =
        FavViewModelFactory(getFavoriteRecipes).create(FavViewModel::class.java)

    @Provides
    fun localRecipesViewModelProvider(getLocalRecipes: GetLocalRecipes) =
        LocalRecipesViewModelFactory(getLocalRecipes).create(LocalRecipesViewModel::class.java)

    @Provides
    fun searchViewModelProvider(getRecipesByName: GetRecipesByName) =
        SearchViewModelFactory(getRecipesByName).create(SearchViewModel::class.java)

    @Provides
    fun detailViewModelFactoryProvider(getRecipeById: GetRecipeById, toggleRecipeFavorite: ToggleRecipeFavorite) =
        DetailViewModelFactory(getRecipeById, toggleRecipeFavorite)

}