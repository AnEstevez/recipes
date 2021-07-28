package com.andresestevez.recipes.models

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TheMealDbClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(TheMealDbService::class.java)
}