package com.andresestevez.recipes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Recipe::class], version = 3)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {

    companion object {
        fun build(context: Context) = Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            "recipe-db"
        ).build()
    }

    abstract fun recipeDao(): RecipeDao
}