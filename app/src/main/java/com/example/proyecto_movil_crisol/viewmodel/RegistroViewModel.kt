package com.example.proyecto_movil_crisol.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_movil_crisol.CrisolApp
import com.example.proyecto_movil_crisol.api.ClienteService
import com.example.proyecto_movil_crisol.model.Cliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroViewModel : ViewModel() {

    val service = CrisolApp.retrofit.create(ClienteService::class.java)

    val registrationResult = MutableLiveData<ResultadoRegistro>()

    fun registrar(cliente: Cliente) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.crearCliente(cliente)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    registrationResult.value = ResultadoRegistro.Exito(body["message"] ?: "")
                } else if(response.errorBody() != null){
                    val errorBody = response.errorBody()!!.string() // Asegúrate de manejar esto adecuadamente
                    registrationResult.value = ResultadoRegistro.Error(errorBody)
                } else {
                    registrationResult.value = ResultadoRegistro.Error("Error desconocido")
                }
            }
        }
    }
}

// Clase para representar los posibles resultados del registro
sealed class ResultadoRegistro {
    data class Exito(val message: String) : ResultadoRegistro()
    data class Error(val message: String) : ResultadoRegistro()
}