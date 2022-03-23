package com.andresestevez.recipes.di

import android.Manifest
import android.app.Activity
import androidx.activity.ComponentActivity
import com.andresestevez.recipes.R
import com.andresestevez.recipes.ui.common.PermissionRequester
import com.andresestevez.recipes.ui.common.toast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object LocalRecipesFragmentModule {

    @Provides
    fun permissionRequesterProvider(activity: Activity) =
        PermissionRequester(activity as ComponentActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            onRationale = { activity.toast(activity.getString(R.string.rationale_local_dishes)) }
        )

}