package com.example.proyecto_movil_crisol.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_movil_crisol.CrisolApp
import com.example.proyecto_movil_crisol.api.ClienteService
import com.example.proyecto_movil_crisol.model.Cliente
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers

class LoginViewModel :ViewModel(){
    // instancia del servicio
    val service = CrisolApp.retrofit.create(ClienteService::class.java)

    // variable LiveData para comunicar los resultados a la UI
    val authenticationResult = MutableLiveData<ResultadoAutenticacion>()

    fun autenticar(cliente: Cliente) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.autenticarCliente(cliente)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body["message"] == "Inicio de sesión exitoso") {
                        authenticationResult.value = ResultadoAutenticacion.Exito(body["nombreCliente"] ?: "")
                    } else {
                        //error de autenticacion
                        authenticationResult.value = ResultadoAutenticacion.Error(body["message"] ?: "Error desconocido")
                    }
                } else {
                    // error de red o del servidor
                    authenticationResult.value = ResultadoAutenticacion.Error("Error de red o del servidor")
                }
            }
        }
    }

}

// Clase para representar los posibles resultados de la autenticación
sealed class ResultadoAutenticacion {
    data class Exito(val nombreCliente: String): ResultadoAutenticacion()
    data class Error(val mensajeError: String): ResultadoAutenticacion()
}