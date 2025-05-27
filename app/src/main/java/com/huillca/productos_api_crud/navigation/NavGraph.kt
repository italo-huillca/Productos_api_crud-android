package com.huillca.productos_api_crud.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.huillca.productos_api_crud.view.screens.*
import com.huillca.productos_api_crud.viewmodel.ProductoViewModel
import com.huillca.productos_api_crud.viewmodel.CategoriaViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    productoViewModel: ProductoViewModel = viewModel(),
    categoriaViewModel: CategoriaViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ProductosList.route
    ) {
        // Pantalla de lista de productos
        composable(Screen.ProductosList.route) {
            ProductosListScreen(
                navController = navController,
                viewModel = productoViewModel
            )
        }
        
        // Pantalla de detalle de producto
        composable(
            route = Screen.ProductoDetail.route,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            ProductoDetailScreen(
                navController = navController,
                productoId = productoId,
                viewModel = productoViewModel
            )
        }
        
        // Pantalla de crear producto
        composable(Screen.CreateProducto.route) {
            CreateEditProductoScreen(
                navController = navController,
                productoViewModel = productoViewModel,
                categoriaViewModel = categoriaViewModel
            )
        }
        
        // Pantalla de editar producto
        composable(
            route = Screen.EditProducto.route,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            CreateEditProductoScreen(
                navController = navController,
                productoViewModel = productoViewModel,
                categoriaViewModel = categoriaViewModel,
                productoId = productoId
            )
        }
        
        // Pantalla de lista de categorías
        composable(Screen.CategoriasList.route) {
            CategoriasListScreen(
                navController = navController,
                viewModel = categoriaViewModel
            )
        }
        
        // Pantalla de crear categoría
        composable(Screen.CreateCategoria.route) {
            CreateEditCategoriaScreen(
                navController = navController,
                viewModel = categoriaViewModel
            )
        }
        
        // Pantalla de editar categoría
        composable(
            route = Screen.EditCategoria.route,
            arguments = listOf(navArgument("categoriaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoriaId = backStackEntry.arguments?.getInt("categoriaId") ?: 0
            CreateEditCategoriaScreen(
                navController = navController,
                viewModel = categoriaViewModel,
                categoriaId = categoriaId
            )
        }
    }
}

sealed class Screen(val route: String) {
    object ProductosList : Screen("productos_list")
    object ProductoDetail : Screen("producto_detail/{productoId}") {
        fun createRoute(productoId: Int) = "producto_detail/$productoId"
    }
    object CreateProducto : Screen("create_producto")
    object EditProducto : Screen("edit_producto/{productoId}") {
        fun createRoute(productoId: Int) = "edit_producto/$productoId"
    }
    object CategoriasList : Screen("categorias_list")
    object CreateCategoria : Screen("create_categoria")
    object EditCategoria : Screen("edit_categoria/{categoriaId}") {
        fun createRoute(categoriaId: Int) = "edit_categoria/$categoriaId"
    }
} 