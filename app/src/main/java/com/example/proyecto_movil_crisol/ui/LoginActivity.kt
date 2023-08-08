package com.example.proyecto_movil_crisol.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_movil_crisol.databinding.ActivityLoginBinding
import com.example.proyecto_movil_crisol.model.Cliente
import com.example.proyecto_movil_crisol.viewmodel.LoginViewModel
import com.example.proyecto_movil_crisol.viewmodel.ResultadoAutenticacion
import mensaje

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val isUserLogged = sharedPref.getBoolean("isUserLogged", false)
        val nombreCliente = sharedPref.getString("nombreCliente", "")

        if (isUserLogged) {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            if (!nombreCliente.isNullOrEmpty()) {
                mensaje("Bienvenido de nuevo, $nombreCliente!")
            }
            finish()
        } else {
            setContentView(binding.root)


            // inicializa el ViewModel
            loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

            loginViewModel.authenticationResult.observe(this, Observer { resultado ->
                when (resultado) {
                    is ResultadoAutenticacion.Exito -> {
                        val sharedPrefe = getSharedPreferences("myPref", Context.MODE_PRIVATE)
                        with (sharedPrefe.edit()) {
                            putBoolean("isUserLogged", true)
                            putString("nombreCliente", resultado.nombreCliente)
                            putInt("idCliente", resultado.id.toInt())
                            apply()
                        }

                        val intent = Intent(this, PrincipalActivity::class.java)
                        startActivity(intent)
                        mensaje("Bienvenido(a), ${resultado.nombreCliente}!")
                        finish()


                    }
                    is ResultadoAutenticacion.Error -> {
                        mensaje("Email o contraseña incorrectos.")
                    }
                }
            })

            binding.btnIngresar.setOnClickListener {
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPass.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    mensaje("Rellene todos los campos.")
                    return@setOnClickListener
                } else {
                    val cliente = Cliente(
                        clienteId = 0,
                        dni = "",
                        apellidos = "",
                        nombres = "",
                        telefono = null,
                        direccion = null,
                        email = email,
                        password = password,
                        estado = null
                    )
                    loginViewModel.autenticar(cliente)
                }
            }

            binding.txtRegistrarse.setOnClickListener {
                val intent2 = Intent(this, RegistroActivity::class.java)
                startActivity(intent2)
            }
        }
    }
}