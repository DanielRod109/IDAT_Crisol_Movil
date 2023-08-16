package com.example.proyecto_movil_crisol.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_movil_crisol.CrisolApp
import com.example.proyecto_movil_crisol.api.ProductoService
import com.example.proyecto_movil_crisol.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogoViewModel : ViewModel(){

    // servicio
    val service = CrisolApp.retrofit.create(ProductoService::class.java)

    // variables LiveData para comunicar los resultados a la UI
    val resultadoListar= MutableLiveData<ResultadoListar>()
    val resultadoBuscarLibroPorId= MutableLiveData<ResultadoBuscarLibroPorId>()
    val resultadoBuscarPorNombre= MutableLiveData<ResultadoBuscarPorNombre>()
    val resultadoBuscarPorSubgenero= MutableLiveData<ResultadoBuscarPorSubgenero>()



    fun listarProductos() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.listarLibros()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    resultadoListar.value = ResultadoListar.Exito(response.body()!!)
                } else {
                    resultadoListar.value = ResultadoListar.Error("Error al obtener la lista de libros")
                }
            }
        }
    }

    fun buscarLibroPorId(idLibro: Int) {
            // llama al API
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.buscarLibroPorId(idLibro)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        resultadoBuscarLibroPorId.value = ResultadoBuscarLibroPorId.Exito(body)
                    } else {
                        resultadoBuscarLibroPorId.value = ResultadoBuscarLibroPorId.Error("Error de red o del servidor")
                    }
                }
            }
        }

    fun buscarPorNombreAutorEditorial(nombre: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.buscarPorNombreAutorEditorial(nombre)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    resultadoBuscarPorNombre.value = ResultadoBuscarPorNombre.Exito(response.body()!!)
                } else {
                    resultadoBuscarPorNombre.value = ResultadoBuscarPorNombre.Error("No hay resultados para su búsqueda.")
                }
            }
        }
    }

    fun buscarLibroPorSubgenero(subgenero: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.buscarLibroPorSubgenero(subgenero)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    resultadoBuscarPorSubgenero.value = ResultadoBuscarPorSubgenero.Exito(response.body()!!)
                } else {
                    resultadoBuscarPorSubgenero.value = ResultadoBuscarPorSubgenero.Error("Error al buscar libros por subgénero")
                }
            }
        }
    }


}

sealed class ResultadoListar {
    data class Exito(val libros: List<Producto>) : ResultadoListar()
    data class Error(val message: String) : ResultadoListar()
}
sealed class ResultadoBuscarLibroPorId {
    data class Exito(val producto: Producto) : ResultadoBuscarLibroPorId()
    data class Error(val message: String) : ResultadoBuscarLibroPorId()
}

sealed class ResultadoBuscarPorNombre {
    data class Exito(val libros: List<Producto>) : ResultadoBuscarPorNombre()
    data class Error(val message: String) : ResultadoBuscarPorNombre()
}

sealed class ResultadoBuscarPorSubgenero {
    data class Exito(val libros: List<Producto>) : ResultadoBuscarPorSubgenero()
    data class Error(val message: String) : ResultadoBuscarPorSubgenero()
}