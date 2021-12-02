package com.andresestevez.recipes.di

import android.app.Application
import androidx.room.Room
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.recipes.R
import com.andresestevez.recipes.data.PlayServicesLocationDataSource
import com.andresestevez.recipes.data.database.RecipeDatabase
import com.andresestevez.recipes.data.database.RoomDataSource
import com.andresestevez.recipes.data.server.MealDBDataSource
import com.andresestevez.recipes.data.server.TheMealDbClient
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
    fun apiKeyProvider(application: Application): String = application.getString(R.string.api_key)

    @Provides
    @Singleton
    fun dataBaseProvider(application: Application) = Room.databaseBuilder(
        application,
        RecipeDatabase::class.java,
        "recipe-db"
    ).build()

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(application: Application): String =
        application.getString(R.string.mealdb_base_url)

    @Provides
    @Singleton
    fun mealDBClientProvider(
        application: Application,
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