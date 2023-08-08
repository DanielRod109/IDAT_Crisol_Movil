package com.example.proyecto_movil_crisol.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_movil_crisol.CrisolApp
import com.example.proyecto_movil_crisol.api.ClienteService
import com.example.proyecto_movil_crisol.model.Cliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilViewModel : ViewModel(){

    // servicio
    val service = CrisolApp.retrofit.create(ClienteService::class.java)

    // variables LiveData para comunicar los resultados a la UI
    val resultadoEditar= MutableLiveData<ResultadoEditar>()
    val resultadoBuscar= MutableLiveData<ResultadoBuscar>()

    fun editarCliente(cliente: Cliente, idCliente: Int) {
        // llama al API
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.editarCliente(cliente, idCliente)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    resultadoEditar.value = ResultadoEditar.Exito(body["message"] ?: "")
                } else {
                    resultadoEditar.value = ResultadoEditar.Error("Error de red o del servidor")
                }
            }
        }
    }

    fun buscarCliente(idCliente: Int) {
        // llama al API
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.buscarCliente(idCliente)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    resultadoBuscar.value = ResultadoBuscar.Exito(body)
                } else {
                    resultadoBuscar.value = ResultadoBuscar.Error("Error de red o del servidor")
                }
            }
        }
    }

}
sealed class ResultadoEditar {
    data class Exito(val message: String) : ResultadoEditar()
    data class Error(val message: String) : ResultadoEditar()
}

sealed class ResultadoBuscar {
    data class Exito(val cliente: Cliente) : ResultadoBuscar()
    data class Error(val message: String) : ResultadoBuscar()
}