package com.huillca.productos_api_crud.data.model

import com.google.gson.annotations.SerializedName

data class Categoria(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("nombre")
    val nombre: String = "",
    
    @SerializedName("pub_date")
    val pubDate: String = "",
    
    @SerializedName("imagen")
    val imagen: String = ""
)

data class CategoriaCreate(
    val nombre: String,
    val imagen: String? = null
)

data class CategoriaUpdate(
    val nombre: String? = null,
    val imagen: String? = null
) 