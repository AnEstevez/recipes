package com.andresestevez.recipes.data.server

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMealDbService {

    @GET("{apiKey}/search.php")
    suspend fun listMealsByName(@Path("apiKey") apiKey: String, @Query("s") recipeName: String): RecipesDdResult

    @GET("{apiKey}/filter.php")
    suspend fun listMealsByNationality(@Path("apiKey") apiKey: String, @Query("a") nationality: String): RecipesDdResult

    @GET("{apiKey}/lookup.php")
    suspend fun findMealById(@Path("apiKey") apiKey: String, @Query("i") recipeId: String): RecipesDdResult
}