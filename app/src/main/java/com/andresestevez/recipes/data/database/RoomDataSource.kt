package com.andresestevez.recipes.data.database

import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.recipes.data.toEntity
import com.andresestevez.recipes.data.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.andresestevez.domain.Recipe as DomainRecipe

class RoomDataSource(db: RecipeDatabase) : LocalDataSource {

    private val recipeDao = db.recipeDao()

    override suspend fun findById(recipeId: String): DomainRecipe? = withContext(Dispatchers.IO) {
        recipeDao.findByIdMeal(recipeId)?.toDomain()
    }

    override suspend fun saveRecipe(recipe: DomainRecipe?) {
        withContext(Dispatchers.IO){
            recipe?.let {
                recipeDao.insertRecipe(listOf(it.toEntity()))
            }
        }
    }

    override suspend fun getFavorites(): List<DomainRecipe> = withContext(Dispatchers.IO) {
        recipeDao.getFavorites().map { it.toDomain() }
    }

    override suspend fun updateRecipe(recipe: DomainRecipe) = withContext(Dispatchers.IO){
        recipeDao.updateRecipe(recipe.toEntity())
    }
}
