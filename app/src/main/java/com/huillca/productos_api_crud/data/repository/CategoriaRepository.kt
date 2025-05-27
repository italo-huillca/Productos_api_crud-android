package com.huillca.productos_api_crud.data.repository

import com.huillca.productos_api_crud.data.api.ApiClient
import com.huillca.productos_api_crud.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoriaRepository {
    private val apiService = ApiClient.apiService
    
    fun getCategorias(): Flow<Result<List<Categoria>>> = flow {
        try {
            val response = apiService.getCategorias()
            if (response.isSuccessful) {
                response.body()?.let { categorias ->
                    emit(Result.success(categorias))
                } ?: emit(Result.failure(Exception("Respuesta vacía")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun getCategoria(id: Int): Flow<Result<Categoria>> = flow {
        try {
            val response = apiService.getCategoria(id)
            if (response.isSuccessful) {
                response.body()?.let { categoria ->
                    emit(Result.success(categoria))
                } ?: emit(Result.failure(Exception("Categoría no encontrada")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun createCategoria(categoria: CategoriaCreate): Flow<Result<Categoria>> = flow {
        try {
            val response = apiService.createCategoria(categoria)
            if (response.isSuccessful) {
                response.body()?.let { nuevaCategoria ->
                    emit(Result.success(nuevaCategoria))
                } ?: emit(Result.failure(Exception("Error al crear categoría")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun updateCategoria(id: Int, categoria: CategoriaUpdate): Flow<Result<Categoria>> = flow {
        try {
            val response = apiService.updateCategoria(id, categoria)
            if (response.isSuccessful) {
                response.body()?.let { categoriaActualizada ->
                    emit(Result.success(categoriaActualizada))
                } ?: emit(Result.failure(Exception("Error al actualizar categoría")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun deleteCategoria(id: Int): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.deleteCategoria(id)
            if (response.isSuccessful) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 