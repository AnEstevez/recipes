package com.andresestevez.recipes

import android.app.Application
import com.andresestevez.recipes.di.DaggerRecipesComponent
import com.andresestevez.recipes.di.RecipesComponent

class RecipesApp: Application() {

    lateinit var component: RecipesComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerRecipesComponent
            .factory()
            .create(this)
    }
}