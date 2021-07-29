package com.andresestevez.recipes.models

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMealDbService {

    @GET("{apiKey}/search.php")
    suspend fun listMealsByName(@Path("apiKey") apiKey: String, @Query("s") recipeName: String): RecipesDdResult
}