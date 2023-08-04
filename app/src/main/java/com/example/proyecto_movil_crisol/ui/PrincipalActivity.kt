package com.example.proyecto_movil_crisol.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyecto_movil_crisol.R

class PrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
    }
    /*val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

bottomNavigationView.setOnNavigationItemSelectedListener { item ->
    when(item.itemId) {
        R.id.action_home -> {
            // Acción para "home"
            true
        }
        R.id.action_search -> {
            // Acción para "search"
            true
        }
        R.id.action_profile -> {
            // Acción para "profile"
            true
        }
        else -> false
    }
}*/
}