package com.huillca.productos_api_crud.data.api

import com.huillca.productos_api_crud.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Endpoints para Productos
    @GET("productos/")
    suspend fun getProductos(): Response<List<Producto>>
    
    @GET("productos/{id}/")
    suspend fun getProducto(@Path("id") id: Int): Response<Producto>
    
    @POST("productos/")
    suspend fun createProducto(@Body producto: ProductoCreate): Response<Producto>
    
    // Crear producto con imagen (multipart)
    @Multipart
    @POST("productos/")
    suspend fun createProductoWithImage(
        @Part("nombre") nombre: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part("pub_date") pubDate: RequestBody,
        @Part imagen: MultipartBody.Part
    ): Response<Producto>
    
    @PATCH("productos/{id}/")
    suspend fun updateProducto(
        @Path("id") id: Int,
        @Body producto: ProductoUpdate
    ): Response<Producto>
    
    // Actualizar producto con imagen (multipart)
    @Multipart
    @PATCH("productos/{id}/")
    suspend fun updateProductoWithImage(
        @Path("id") id: Int,
        @Part("nombre") nombre: RequestBody?,
        @Part("precio") precio: RequestBody?,
        @Part("stock") stock: RequestBody?,
        @Part("categoria") categoria: RequestBody?,
        @Part imagen: MultipartBody.Part?
    ): Response<Producto>
    
    @DELETE("productos/{id}/")
    suspend fun deleteProducto(@Path("id") id: Int): Response<Unit>
    
    // Endpoints para Categorías
    @GET("categorias/")
    suspend fun getCategorias(): Response<List<Categoria>>
    
    @GET("categorias/{id}/")
    suspend fun getCategoria(@Path("id") id: Int): Response<Categoria>
    
    @POST("categorias/")
    suspend fun createCategoria(@Body categoria: CategoriaCreate): Response<Categoria>
    
    @PATCH("categorias/{id}/")
    suspend fun updateCategoria(
        @Path("id") id: Int,
        @Body categoria: CategoriaUpdate
    ): Response<Categoria>
    
    @DELETE("categorias/{id}/")
    suspend fun deleteCategoria(@Path("id") id: Int): Response<Unit>
} 