package com.example.proyecto_movil_crisol.model

data class Producto (
    val id_libro: Int,
    val nombre: String,
    val peso: Double,
    val editorial: String,
    val nombre_autor: String,
    val alto: Double,
    val ancho: Double,
    val stock: Int,
    val precio: Double,
    val aedicion: String,
    val npaginas: Int,
    val img: String,
    val subgenero: String

)