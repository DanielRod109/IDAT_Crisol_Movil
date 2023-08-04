package com.example.proyecto_movil_crisol.api

import com.example.proyecto_movil_crisol.model.Cliente
import retrofit2.Response
import retrofit2.http.*

interface ClienteService {
    @GET("cliente/listar")
    suspend fun getClientes(): Response<List<Cliente>>

    @POST("cliente/crear")
    suspend fun crearCliente(@Body nuevoCliente: Cliente): Response<Map<String, String>>

    @PUT("cliente/editar/{clienteId}")
    suspend fun editarCliente(@Path("clienteId") clienteId: Int, @Body cliente: Cliente): Response<String>

    @DELETE("cliente/eliminar/{clienteId}")
    suspend fun eliminarCliente(@Path("clienteId") clienteId: Int): Response<String>

    @GET("cliente/buscar/{clienteId}")
    suspend fun buscarCliente(@Path("clienteId") clienteId: Int): Response<Cliente>

    @POST("cliente/autenticar")
    suspend fun autenticarCliente(@Body cliente: Cliente): Response<Map<String, String>>
}