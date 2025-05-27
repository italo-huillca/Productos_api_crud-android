package com.huillca.productos_api_crud.data.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Producto(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("nombre")
    val nombre: String = "",
    
    @SerializedName("precio")
    val precio: String = "",
    
    @SerializedName("stock")
    val stock: Int = 0,
    
    @SerializedName("pub_date")
    val pubDate: String = "",
    
    @SerializedName("imagen")
    val imagen: String = "",
    
    @SerializedName("categoria")
    val categoria: Int = 0
)

// Para crear sin imagen (JSON simple)
data class ProductoCreate(
    val nombre: String,
    val precio: String,
    val stock: Int,
    val categoria: Int,
    val pub_date: String = Instant.now().toString()
)

// Para crear con imagen (se usa multipart)
data class ProductoCreateWithImage(
    val nombre: String,
    val precio: String,
    val stock: Int,
    val categoria: Int,
    val imagenUri: String? = null // URI local para la UI
)

// Modelo para actualización - solo campos editables
data class ProductoUpdate(
    val nombre: String,
    val precio: String,
    val stock: Int,
    val categoria: Int
) 