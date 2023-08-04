package com.example.proyecto_movil_crisol.model

data class Cliente (

    val clienteId: Int,
    val dni: String,
    val apellidos: String,
    val nombres: String,
    val telefono: String?,
    val direccion: String?,
    val email: String,
    val password: String,
    val estado: String?



)