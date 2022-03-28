package com.andresestevez.recipes.framework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface RecipeDao {

    @Query("SELECT * from recipe WHERE favorite = 1 order by dateModified desc")
    fun getFavorites(): Flow<List<RecipeEntity>>
    fun getFavoritesDistinctUntilChanged() = getFavorites().distinctUntilChanged()

    @Query("SELECT * from recipe WHERE id = :recipeId")
    fun findById(recipeId: String): Flow<RecipeEntity>
    fun findByIdDistinctUntilChanged(recipeId: String) = findById(recipeId).distinctUntilChanged()

    @Query("SELECT * from recipe WHERE country = :country")
    fun searchByCountry(country: String): Flow<List<RecipeEntity>>
    fun searchByCountryDistinctUntilChanged(country: String) = searchByCountry(country).distinctUntilChanged()

    @Query("SELECT * from recipe WHERE name like  '%' || :name || '%' ")
    fun searchByName(name: String): Flow<List<RecipeEntity>>
    fun searchByNameDistinctUntilChanged(name: String) = searchByName(name).distinctUntilChanged()

    @Insert(onConflict = IGNORE)
    suspend fun insertAll(recipes: List<RecipeEntity>)

    @Update
    suspend fun update(recipe: RecipeEntity)


}