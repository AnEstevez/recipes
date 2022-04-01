package com.andresestevez.recipes.framework

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Location
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.LocationDataSource.Companion.DEFAULT_COUNTRY_CODE
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.resume

class PlayServicesLocationDataSource(application: Application) : LocationDataSource {

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private val geocoder = Geocoder(application)

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocationNationality(): String = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                continuation.resume(
                    try {
                        it.result.toCountryCode().toNationality()
                    } catch (throwable: Throwable) {
                        Timber.d(throwable)
                        CountryCodeToNationality.XX.nationality
                    })
            }
        }
    }

    private fun Location?.toCountryCode(): String {
        if (this == null) {
            return DEFAULT_COUNTRY_CODE
        }
        var result = DEFAULT_COUNTRY_CODE

        try {
            result = geocoder.getFromLocation(
                this.latitude,
                this.longitude,
                1)?.firstOrNull()?.countryCode ?: DEFAULT_COUNTRY_CODE
        } catch (e: IOException) {
            Timber.e(e)
        }

        return result
    }

    private fun String?.toNationality(): String {
        Timber.d("Country code [%s]", this)

        val result = if (CountryCodeToNationality.values()
                .map { country -> country.name }
                .contains(this)
        )
            CountryCodeToNationality.valueOf(this!!).nationality
        else CountryCodeToNationality.XX.nationality

        Timber.d("Nationality [%s]", result)
        return result
    }
}

