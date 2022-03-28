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
import com.andresestevez.recipes.framework.server.TheMealDbClient
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
    fun dataBaseProvider(application: Application) = RecipeDatabase.getDatabase(application)

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(application: Application): String =
        application.getString(R.string.mealdb_base_url)

    @Provides
    @Singleton
    fun mealDBClientProvider(
        @Named("baseUrl") baseUrl: String,
    ): TheMealDbClient = TheMealDbClient(baseUrl)

    @Provides
    fun localDataSourceProvider(db: RecipeDatabase): LocalDataSource = RoomDataSource(db)

    @Provides
    fun remoteDataSourceProvider(mealDbClient: TheMealDbClient): RemoteDataSource =
        MealDBDataSource(mealDbClient)

    @Provides
    fun locationDataSourceProvider(application: Application): LocationDataSource =
        PlayServicesLocationDataSource(application)

}