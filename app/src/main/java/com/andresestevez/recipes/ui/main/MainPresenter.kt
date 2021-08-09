package com.andresestevez.recipes.ui.main

import android.Manifest
import android.app.Activity
import androidx.activity.ComponentActivity
import com.andresestevez.recipes.R
import com.andresestevez.recipes.models.PermissionRequester
import com.andresestevez.recipes.ui.common.toast
import com.google.android.material.tabs.TabLayout

class MainPresenter(private val activity: Activity) {

    interface View {
        fun findLocalRecipes(tab: TabLayout.Tab?)
    }

    companion object {
        const val LOCAL_RECIPES_FRAGMENT = 2
    }

    private var view : View? = null

    val permissionRequester =
        PermissionRequester(activity as ComponentActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            onRationale = {activity.toast(activity.getString(R.string.rationale_local_dishes))}
        )

    fun onCreate(view: View) {
        this.view = view
    }

    fun onDestroy() {
        this.view = null
    }

    fun onTabSelected(tab: TabLayout.Tab?) {
        view?.findLocalRecipes(tab)
    }

    fun onTabReselected(tab: TabLayout.Tab?) {
        view?.findLocalRecipes(tab)
    }
}