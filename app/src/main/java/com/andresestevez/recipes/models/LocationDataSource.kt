package com.andresestevez.recipes.models

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface LocationDataSource {
    suspend fun getLastLocation(): Location?
}

class PlayServicesLocationDataSource(activity: Activity) : LocationDataSource {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                continuation.resume(it.result)
            }
        }

}