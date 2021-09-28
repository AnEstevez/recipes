package com.andresestevez.recipes.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao {

    @Query("SELECT * from Recipe WHERE favorite = 1 order by dateModified desc")
    fun getFavorites(): List<Recipe>

    @Query("SELECT * from Recipe WHERE id = :idMeal")
    fun findByIdMeal(idMeal: String): Recipe

    @Insert(onConflict = IGNORE)
    fun insertRecipe(recipes: List<Recipe>)

    @Update
    fun updateRecipe(recipe: Recipe)
}