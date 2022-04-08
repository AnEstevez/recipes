package com.andresestevez.data.repository

import com.andresestevez.data.source.LocalDataSource
import com.andresestevez.data.source.LocationDataSource
import com.andresestevez.data.source.RemoteDataSource
import com.andresestevez.domain.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val locationDataSource: LocationDataSource,
) {

    fun findRecipeById(recipeId: String): Flow<Result<Recipe>> = flow {
        localDataSource.findById(recipeId).collect {
            if (it.instructions.isEmpty()) {
                val remoteResult = remoteDataSource.findById(recipeId)
                if (remoteResult.isSuccess) {
                    localDataSource.updateRecipe(remoteResult.getOrThrow()
                        .copy(favorite = it.favorite))
                } else {
                    emit(Result.success(it))
                    emit(remoteResult)
                }
            } else {
                emit(Result.success(it))
            }
        }
    }.catch { emit(Result.failure(it)) }

    fun getRecipesByRegion(): Flow<Result<List<Recipe>>> = flow {
        val nationality = locationDataSource.getLastLocationNationality()
        localDataSource.searchByCountry(nationality)
            .catch { emit(Result.success<List<Recipe>>(emptyList())) }.collect { recipes ->
                if (recipes.isNullOrEmpty()) {
                    emit(Result.success<List<Recipe>>(emptyList()))
                } else {
                    emit(Result.success(recipes))
                }

                val remoteResult = remoteDataSource.listMealsByNationality(nationality)
                if (remoteResult.isSuccess) {
                    // listMealsByNationality doesn't return the country
                    localDataSource.saveAll(remoteResult.getOrThrow()
                        .map { it.copy(country = nationality) })
                } else {
                    remoteResult.onFailure { throwable ->
                        emit(Result.failure<List<Recipe>>(throwable))
                    }
                }
            }
    }.catch {
        emit(Result.failure<List<Recipe>>(it))
    }

    fun getRecipesByName(name: String): Flow<Result<List<Recipe>>> = flow {
        val recipeName = name.trim().lowercase()
        localDataSource.searchByName(recipeName)
            .catch { emit(Result.success<List<Recipe>>(emptyList())) }.collect {
                if (it.isNullOrEmpty()) {
                    emit(Result.success<List<Recipe>>(emptyList()))
                } else {
                    emit(Result.success(it))
                }

                val remoteResult = remoteDataSource.listMealsByName(recipeName)
                if (remoteResult.isSuccess) {
                    localDataSource.saveAll(remoteResult.getOrThrow())
                } else {
                    remoteResult.onFailure { throwable ->
                        emit(Result.failure<List<Recipe>>(throwable))
                    }
                }
            }
    }.catch {
        emit(Result.failure<List<Recipe>>(it))
    }

    suspend fun updateRecipe(recipe: Recipe) {
        localDataSource.updateRecipe(recipe)
    }

    fun getFavorites(): Flow<Result<List<Recipe>>> = flow {
        localDataSource.getFavorites().collect {
            emit(Result.success(it))
        }
    }.catch { emit(Result.failure(it)) }

}
