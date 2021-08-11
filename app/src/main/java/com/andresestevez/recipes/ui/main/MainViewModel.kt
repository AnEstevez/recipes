package com.andresestevez.recipes.ui.main

import android.Manifest
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andresestevez.recipes.R
import com.andresestevez.recipes.models.PermissionRequester
import com.andresestevez.recipes.ui.common.toast
import com.andresestevez.recipes.ui.main.MainViewModel.UiModel.RequestLocalRecipes
import com.google.android.material.tabs.TabLayout

class MainViewModel(private val activity: Activity): ViewModel() {

    sealed class UiModel {
        class RequestLocalRecipes(val tab: TabLayout.Tab?): UiModel()
    }

    companion object {
        const val LOCAL_RECIPES_FRAGMENT = 2
    }

    private val _model = MutableLiveData<UiModel>()
    val model : LiveData<UiModel>
        get() = _model

    val permissionRequester =
        PermissionRequester(activity as ComponentActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            onRationale = {activity.toast(activity.getString(R.string.rationale_local_dishes))}
        )

    fun onTabSelected(tab: TabLayout.Tab?) {
        _model.value = RequestLocalRecipes(tab)
    }

    fun onTabReselected(tab: TabLayout.Tab?) {
        _model.value = RequestLocalRecipes(tab)
    }
}
