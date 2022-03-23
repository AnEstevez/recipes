package com.andresestevez.recipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andresestevez.recipes.ui.main.fragments.RecipesFragmentFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@AndroidEntryPoint
class HiltTestActivity: AppCompatActivity() {
    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface RecipesFragmentFactoryEntryPoint {
        fun getFragmentFactory(): RecipesFragmentFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val entryPoint = EntryPointAccessors.fromActivity(
            this,
            RecipesFragmentFactoryEntryPoint::class.java
        )
        supportFragmentManager.fragmentFactory = entryPoint.getFragmentFactory()

        super.onCreate(savedInstanceState)
    }
}