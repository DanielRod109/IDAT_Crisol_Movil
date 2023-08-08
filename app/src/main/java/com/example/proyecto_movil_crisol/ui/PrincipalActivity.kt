package com.example.proyecto_movil_crisol.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.proyecto_movil_crisol.R
import com.example.proyecto_movil_crisol.databinding.ActivityPrincipalBinding

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun loadFragment(fragment: Fragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }

        // opciones del botton-nav
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.accion_inicio -> {
                loadFragment(CatalagoFragment())
                true
            }
                R.id.accion_carrito -> {
                loadFragment(CarritoFragment())
                true
            }
                R.id.accion_list -> {
                loadFragment(ComprasFragment())
                true
            }
                R.id.accion_perfil -> {
                loadFragment(PerfilFragment())
                true
            }
            else -> false
            }
        }

    }
}