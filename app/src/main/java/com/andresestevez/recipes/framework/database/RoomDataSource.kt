package com.andresestevez.recipes.framework.database

import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.recipes.framework.toDomain
import com.andresestevez.recipes.framework.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import com.andresestevez.domain.Recipe as DomainRecipe

internal class RoomDataSource @Inject constructor(db: RecipeDatabase) : LocalDataSource {

    private val recipeDao = db.recipeDao()

    override fun findById(recipeId: String): Flow<DomainRecipe> =
        recipeDao.findByIdDistinctUntilChanged(recipeId).map { it.toDomain() }

    override suspend fun saveRecipe(recipe: DomainRecipe?) {
        withContext(Dispatchers.IO) {
            recipe?.let {
                recipeDao.insertAll(listOf(it.toEntity().copy(dateModified = Date())))
            }
        }
    }

    override suspend fun saveAll(recipes: List<DomainRecipe>) {
        withContext(Dispatchers.IO) {
            recipeDao.insertAll(recipes.map { it.toEntity().copy(dateModified = Date()) })
        }
    }

    override suspend fun updateRecipe(recipe: DomainRecipe) = withContext(Dispatchers.IO) {
        recipeDao.update(recipe.toEntity().copy(dateModified = Date()))
    }

    override fun getFavorites(): Flow<List<DomainRecipe>> =
        recipeDao.getFavoritesDistinctUntilChanged()
            .map { it.map { recipeEntity -> recipeEntity.toDomain() } }

    override fun searchByCountry(country: String): Flow<List<DomainRecipe>> =
        recipeDao.searchByCountryDistinctUntilChanged(country)
            .map { it.map { recipeEntity -> recipeEntity.toDomain() } }

    override fun searchByName(name: String): Flow<List<DomainRecipe>> =
        recipeDao.searchByNameDistinctUntilChanged(name).map {
            it.map { recipeEntity -> recipeEntity.toDomain() }
        }

}
