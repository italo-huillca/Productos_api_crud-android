package com.huillca.productos_api_crud.data.repository

import android.content.Context
import android.net.Uri
import com.huillca.productos_api_crud.data.api.ApiClient
import com.huillca.productos_api_crud.data.model.*
import com.huillca.productos_api_crud.util.FileUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductoRepository {
    private val apiService = ApiClient.apiService
    
    fun getProductos(): Flow<Result<List<Producto>>> = flow {
        try {
            val response = apiService.getProductos()
            if (response.isSuccessful) {
                response.body()?.let { productos ->
                    emit(Result.success(productos))
                } ?: emit(Result.failure(Exception("Respuesta vacía")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun getProducto(id: Int): Flow<Result<Producto>> = flow {
        try {
            val response = apiService.getProducto(id)
            if (response.isSuccessful) {
                response.body()?.let { producto ->
                    emit(Result.success(producto))
                } ?: emit(Result.failure(Exception("Producto no encontrado")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun createProducto(producto: ProductoCreate): Flow<Result<Producto>> = flow {
        try {
            val response = apiService.createProducto(producto)
            if (response.isSuccessful) {
                response.body()?.let { nuevoProducto ->
                    emit(Result.success(nuevoProducto))
                } ?: emit(Result.failure(Exception("Error al crear producto")))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Result.failure(Exception("Error ${response.code()}: ${response.message()}${errorBody?.let { " - $it" } ?: ""}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun createProductoWithImage(
        context: Context,
        producto: ProductoCreateWithImage,
        imageUri: Uri
    ): Flow<Result<Producto>> = flow {
        try {
            val imagePart = FileUtils.uriToMultipartBody(context, imageUri, "imagen")
            
            if (imagePart != null) {
                val response = apiService.createProductoWithImage(
                    nombre = FileUtils.createRequestBody(producto.nombre),
                    precio = FileUtils.createRequestBody(producto.precio),
                    stock = FileUtils.createRequestBody(producto.stock.toString()),
                    categoria = FileUtils.createRequestBody(producto.categoria.toString()),
                    pubDate = FileUtils.createRequestBody(FileUtils.getCurrentDateTime()),
                    imagen = imagePart
                )
                
                if (response.isSuccessful) {
                    response.body()?.let { nuevoProducto ->
                        emit(Result.success(nuevoProducto))
                    } ?: emit(Result.failure(Exception("Error al crear producto")))
                } else {
                    val errorBody = response.errorBody()?.string()
                    emit(Result.failure(Exception("Error ${response.code()}: ${response.message()}${errorBody?.let { " - $it" } ?: ""}")))
                }
            } else {
                emit(Result.failure(Exception("Error al procesar la imagen")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun updateProducto(id: Int, producto: ProductoUpdate): Flow<Result<Producto>> = flow {
        try {
            val response = apiService.updateProducto(id, producto)
            if (response.isSuccessful) {
                response.body()?.let { productoActualizado ->
                    emit(Result.success(productoActualizado))
                } ?: emit(Result.failure(Exception("Error al actualizar producto")))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Result.failure(Exception("Error ${response.code()}: ${response.message()}${errorBody?.let { " - $it" } ?: ""}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun deleteProducto(id: Int): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.deleteProducto(id)
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