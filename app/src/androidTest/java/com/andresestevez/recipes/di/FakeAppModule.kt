package com.andresestevez.recipes.di

import android.app.Application
import androidx.room.Room
import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.recipes.framework.PlayServicesLocationDataSource
import com.andresestevez.recipes.framework.database.RecipeDatabase
import com.andresestevez.recipes.framework.database.RoomDataSource
import com.andresestevez.recipes.framework.server.MealDBDataSource
import com.andresestevez.recipes.framework.server.TheMealDbClient
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppModule::class])
class FakeAppModule {

    @Provides
    @Singleton
    @Named("apiKeyTest")
    fun apiKeyProvider(application: Application): String = "666"

    @Provides
    @Singleton
    fun dataBaseProvider(application: Application) : RecipeDatabase = Room.inMemoryDatabaseBuilder(
        application,
        RecipeDatabase::class.java
    ).allowMainThreadQueries().build()

    @Provides
    @Singleton
    @Named("baseUrlTest")
    fun baseUrlProvider(application: Application): String = "http://localhost:8080/"

    @Provides
    @Singleton
    fun mealDBClientProvider(
        application: Application,
        @Named("baseUrlTest") baseUrl: String,
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