package com.example.proyecto_movil_crisol.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_movil_crisol.databinding.ActivityRegistroBinding
import com.example.proyecto_movil_crisol.model.Cliente
import com.example.proyecto_movil_crisol.viewmodel.RegistroViewModel
import com.example.proyecto_movil_crisol.viewmodel.ResultadoRegistro
import mensaje

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var registroViewModel: RegistroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // inicializa el ViewModel
        registroViewModel = ViewModelProvider(this).get(RegistroViewModel::class.java)

        registroViewModel.registrationResult.observe(this, Observer { resultado ->
            when (resultado) {
                is ResultadoRegistro.Exito -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                    mensaje("Registro exitoso.")
                }
                is ResultadoRegistro.Error -> {
                    mensaje(resultado.message)
                }
            }
        })

        binding.btnRegistrarse.setOnClickListener {
            val dni = binding.edtDni.text.toString()
            val nombres = binding.edtNombres.text.toString()
            val apellidos = binding.edtApellidos.text.toString()
            val correo = binding.edtCorreo.text.toString()
            val password = binding.edtPassword.text.toString()

            if (dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || password.isEmpty()) {

                mensaje("Rellene todos los campos.")
                return@setOnClickListener
            }
            else if (dni.length != 8) {
                mensaje("El DNI debe tener 8 dígitos.")
                return@setOnClickListener
            }

            else {
                val cliente = Cliente(
                    clienteId = 0,
                    dni = dni,
                    apellidos = apellidos,
                    nombres = nombres,
                    telefono = null,
                    direccion = null,
                    email = correo,
                    password = password,
                    estado = null
                )
                registroViewModel.registrar(cliente)
            }
        }

        binding.txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}