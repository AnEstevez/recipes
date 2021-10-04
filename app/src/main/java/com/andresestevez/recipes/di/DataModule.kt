package com.andresestevez.recipes.di

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun recipeRepositoryProvider(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        locationDataSource: LocationDataSource,
        @Named("apiKey") apiKey: String
    ) = RecipesRepository(localDataSource, remoteDataSource, locationDataSource, apiKey)
}