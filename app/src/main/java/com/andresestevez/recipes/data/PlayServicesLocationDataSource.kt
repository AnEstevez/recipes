package com.andresestevez.recipes.data

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Location
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.LocationDataSource.Companion.DEFAULT_COUNTRY_CODE
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesLocationDataSource(application: Application) : LocationDataSource {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    private val geocoder = Geocoder(application)

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocationNationality(): String =
        suspendCancellableCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                continuation.resume(it.result.toCountryCode().toNationality())
            }
        }

    private fun Location?.toCountryCode(): String {
        if (this == null) {
            return DEFAULT_COUNTRY_CODE
        }
        return geocoder.getFromLocation(
            this.latitude,
            this.longitude,
            1
        )?.firstOrNull()?.countryCode ?: DEFAULT_COUNTRY_CODE
    }

    private fun String?.toNationality(): String {
        return if (CountryCodeToNationality.values()
                .map { country -> country.name }
                .contains(this))
            CountryCodeToNationality.valueOf(this!!).nationality
        else CountryCodeToNationality.XX.nationality
    }
}

