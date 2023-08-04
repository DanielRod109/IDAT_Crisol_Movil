package com.example.proyecto_movil_crisol.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyecto_movil_crisol.CrisolApp
import com.example.proyecto_movil_crisol.api.ClienteService
import com.example.proyecto_movil_crisol.model.Cliente
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers

class ClienteViewModel :ViewModel(){


    // Crear la instancia del servicio.
    val service = CrisolApp.retrofit.create(ClienteService::class.java)

    fun autenticar(cliente: Cliente) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.autenticarCliente(cliente)

            withContext(Dispatchers.Main) {
                // Actualizar la interfaz de usuario en el hilo principal.
                // Aquí puedes comprobar el código de estado y actualizar la UI en consecuencia.
            }
        }
    }


}