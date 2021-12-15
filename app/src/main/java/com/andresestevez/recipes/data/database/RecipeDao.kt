package com.andresestevez.recipes.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao {

    @Query("SELECT * from recipe WHERE favorite = 1 order by dateModified desc")
    fun getFavorites(): List<RecipeEntity>

    @Query("SELECT * from recipe WHERE id = :idMeal")
    fun findByIdMeal(idMeal: String): RecipeEntity

    @Insert(onConflict = IGNORE)
    fun insertRecipe(recipes: List<RecipeEntity>)

    @Update
    fun updateRecipe(recipe: RecipeEntity)
}