package com.andresestevez.data.source


interface LocationDataSource {

    companion object {
        const val DEFAULT_COUNTRY_CODE = "XX"
    }

    suspend fun getLastLocationNationality(): String
}