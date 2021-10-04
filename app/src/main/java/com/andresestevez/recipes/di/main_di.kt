package com.andresestevez.recipes.di

import android.Manifest
import android.app.Activity
import androidx.activity.ComponentActivity
import com.andresestevez.data.repository.RecipesRepository
import com.andresestevez.recipes.R
import com.andresestevez.recipes.ui.common.PermissionRequester
import com.andresestevez.recipes.ui.common.toast
import com.andresestevez.usecases.GetLocalRecipes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class MainFragmentViewModelModule {

    @Provides
    @ViewModelScoped
    fun getLocalRecipesProvider(recipesRepository: RecipesRepository) = GetLocalRecipes(recipesRepository)

}

@Module
@InstallIn(FragmentComponent::class)
object MainFragmentModule {

    @Provides
    fun permissionRequesterProvider(activity: Activity) = PermissionRequester(activity as ComponentActivity,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        onRationale = {activity.toast(activity.getString(R.string.rationale_local_dishes))}
    )
}