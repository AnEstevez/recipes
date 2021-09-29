package com.andresestevez.recipes

import android.app.Application

class RecipesApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}