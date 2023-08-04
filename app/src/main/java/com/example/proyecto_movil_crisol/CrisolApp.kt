package com.example.proyecto_movil_crisol

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrisolApp : Application() {

    companion object {
        lateinit var retrofit: Retrofit
    }

    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/crisol/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}