package com.andresestevez.recipes.models

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Geocoder
import android.location.Location

class CountryCodeRepository(activity: Activity) {

    companion object {
        private const val DEFAULT_COUNTRY_CODE = "XX"
    }

    private val fusedLocationProviderClient: LocationDataSource = PlayServicesLocationDataSource(activity)
    private val geocoder = Geocoder(activity)

    suspend fun findLastLocationNationality(): String? {
           return getLastLocationSuspended().toCountryCode().toNationality()
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocationSuspended(): Location? = fusedLocationProviderClient.getLastLocation()

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