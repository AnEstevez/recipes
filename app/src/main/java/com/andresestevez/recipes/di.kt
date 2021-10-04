package com.andresestevez.recipes

import android.app.Application
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.recipes.data.PlayServicesLocationDataSource
import com.andresestevez.recipes.data.database.RecipeDatabase
import com.andresestevez.recipes.data.database.RoomDataSource
import com.andresestevez.recipes.data.server.MealDBDataSource
import com.andresestevez.recipes.ui.detail.DetailFragment
import com.andresestevez.recipes.ui.detail.DetailViewModel
import com.andresestevez.recipes.ui.detail.DetailViewModelFactory
import com.andresestevez.recipes.ui.main.MainFragment
import com.andresestevez.recipes.ui.main.MainViewModel
import com.andresestevez.recipes.ui.main.fragments.*
import com.andresestevez.usecases.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, scopeModule))
    }
}

private val appModule = module {
    single(named("apiKey")) { androidApplication().getString(R.string.api_key) }
    single { RecipeDatabase.build(get())}
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> { MealDBDataSource() }
    factory<LocationDataSource>{ PlayServicesLocationDataSource(get()) }
    viewModel { LocalRecipesViewModelFactory(get()).create(LocalRecipesViewModel::class.java) }
    factory { GetLocalRecipes(get()) }
}

private val dataModule = module {
    factory { RecipesRepository(get(),get(),get(), get(named("apiKey"))) }
}

private val scopeModule = module {
    scope(named<MainFragment>()) {
        viewModel { MainViewModel() }
    }

    scope(named<FavFragment>()) {
        viewModel { FavViewModelFactory(get()).create(FavViewModel::class.java) }
        scoped { GetFavoriteRecipes(get()) }
    }

    scope(named<SearchFragment>()) {
        viewModel { SearchViewModelFactory(get()).create(SearchViewModel::class.java) }
        scoped { GetRecipesByName(get()) }
    }

    scope(named<DetailFragment>()) {
        viewModel { DetailViewModelFactory(get(), get()).create(DetailViewModel::class.java) }
        scoped { GetRecipeById(get()) }
        scoped { ToggleRecipeFavorite(get()) }
    }
}