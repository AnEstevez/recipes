package com.andresestevez.recipes

import android.app.Application
import androidx.room.Room
import com.andresestevez.recipes.models.database.RecipeDatabase

class RecipesApp: Application() {
    lateinit var db: RecipeDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            RecipeDatabase::class.java, "recipe-db"
        ).build()
    }
}