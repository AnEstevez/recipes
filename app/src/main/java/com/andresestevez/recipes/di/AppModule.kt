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
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
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
    fun localDataSourceProvider(db: RecipeDatabase) : LocalDataSource = RoomDataSource(db)

    @Provides
    fun remoteDataSourceProvider() : RemoteDataSource = MealDBDataSource()

    @Provides
    fun locationDataSourceProvider(application: Application) : LocationDataSource = PlayServicesLocationDataSource(application)
}