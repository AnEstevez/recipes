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
import dagger.Binds
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
    @Named("apiKey")
    fun apiKeyProvider(): String = "666"

    @Provides
    @Singleton
    fun dataBaseProvider(application: Application): RecipeDatabase = Room.inMemoryDatabaseBuilder(
        application,
        RecipeDatabase::class.java
    ).allowMainThreadQueries().build()

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(): String = "http://localhost:8080/"

}


@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppDataModule::class])
abstract class FakeAppDataModule {

    @Binds
    abstract fun bindLocalDataSource(localDataSource: RoomDataSource): LocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: MealDBDataSource): RemoteDataSource

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: PlayServicesLocationDataSource): LocationDataSource

}