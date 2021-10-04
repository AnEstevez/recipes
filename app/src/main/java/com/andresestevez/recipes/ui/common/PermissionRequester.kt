package com.andresestevez.recipes.ui.common

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.scopes.ActivityScoped

class PermissionRequester (activity: ComponentActivity,
                                              private val permission: String,
                                              private val onRationale: () -> Unit = {},
                                              private val onDenied: () -> Unit = {}) {

    private var onGranted: () -> Unit = {}
    private val permissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> onGranted()
                activity.shouldShowRequestPermissionRationale(permission) -> onRationale()
                else -> onDenied()
            }
        }

    fun runWithPermission(body: () -> Unit) {
        onGranted = body
        permissionLauncher.launch(permission)
    }

}