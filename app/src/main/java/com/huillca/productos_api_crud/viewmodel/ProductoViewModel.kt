package com.huillca.productos_api_crud.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huillca.productos_api_crud.data.model.*
import com.huillca.productos_api_crud.data.repository.ProductoRepository
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {
    private val repository = ProductoRepository()
    
    private val _productos = mutableStateOf<List<Producto>>(emptyList())
    val productos: State<List<Producto>> = _productos
    
    private val _productoSeleccionado = mutableStateOf<Producto?>(null)
    val productoSeleccionado: State<Producto?> = _productoSeleccionado
    
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage
    
    private val _isSuccess = mutableStateOf(false)
    val isSuccess: State<Boolean> = _isSuccess
    
    init {
        loadProductos()
    }
    
    fun loadProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            repository.getProductos().collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { productos ->
                        _productos.value = productos
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun loadProducto(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            repository.getProducto(id).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { producto ->
                        _productoSeleccionado.value = producto
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun createProducto(producto: ProductoCreate) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false
            
            repository.createProducto(producto).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { nuevoProducto ->
                        _isSuccess.value = true
                        loadProductos() // Recargar la lista
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun createProductoWithImage(context: Context, producto: ProductoCreateWithImage, imageUri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false
            
            repository.createProductoWithImage(context, producto, imageUri).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { nuevoProducto ->
                        _isSuccess.value = true
                        loadProductos() // Recargar la lista
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun updateProducto(id: Int, producto: ProductoUpdate) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false
            
            repository.updateProducto(id, producto).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { productoActualizado ->
                        _isSuccess.value = true
                        _productoSeleccionado.value = productoActualizado
                        loadProductos() // Recargar la lista
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun deleteProducto(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            repository.deleteProducto(id).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = {
                        loadProductos() // Recargar la lista
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun clearSuccess() {
        _isSuccess.value = false
    }
    
    fun clearSelectedProducto() {
        _productoSeleccionado.value = null
    }
} 