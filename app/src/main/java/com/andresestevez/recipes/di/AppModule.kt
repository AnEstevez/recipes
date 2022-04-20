package com.andresestevez.recipes.di

import android.app.Application
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.recipes.BuildConfig
import com.andresestevez.recipes.R
import com.andresestevez.recipes.framework.PlayServicesLocationDataSource
import com.andresestevez.recipes.framework.database.RecipeDatabase
import com.andresestevez.recipes.framework.database.RoomDataSource
import com.andresestevez.recipes.framework.server.MealDBDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @Named("apiKey")
    fun apiKeyProvider(): String = BuildConfig.API_KEY

    @Provides
    @Singleton
    internal fun dataBaseProvider(application: Application): RecipeDatabase =
        RecipeDatabase.getDatabase(application)

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(application: Application): String =
        application.getString(R.string.mealdb_base_url)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    internal abstract fun bindLocalDataSource(localDataSource: RoomDataSource): LocalDataSource

    @Binds
    internal abstract fun bindRemoteDataSource(remoteDataSource: MealDBDataSource): RemoteDataSource

    @Binds
    internal abstract fun bindLocationDataSource(locationDataSource: PlayServicesLocationDataSource): LocationDataSource

}