package com.example.proyecto_movil_crisol.api

import com.example.proyecto_movil_crisol.model.Producto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductoService {
    @GET("libro/listar")
    suspend fun listarLibros(): Response<List<Producto>>

    @GET("libro/buscar/{id_libro}")
    suspend fun buscarLibroPorId(@Path("id_libro") idLibro: Int): Response<Producto>

    @GET("libro/buscarNombreAutorEditorial/{nombre}")
    suspend fun buscarPorNombreAutorEditorial(@Path("nombre") nombre: String): Response<List<Producto>>

    @GET("libro/buscarLibroporSubgenero/{subgenero}")
    suspend fun buscarLibroPorSubgenero(@Path("subgenero") subgenero: String): Response<List<Producto>>
}