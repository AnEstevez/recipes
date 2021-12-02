package com.andresestevez.recipes.di

import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DataModule::class])
class FakeDataModule {

    @Provides
    fun recipeRepositoryProvider(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        locationDataSource: LocationDataSource,
        @Named("apiKeyTest") apiKey: String
    ) = RecipesRepository(localDataSource, remoteDataSource, locationDataSource, apiKey)
}