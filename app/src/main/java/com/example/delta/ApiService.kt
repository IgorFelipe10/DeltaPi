package com.example.delta

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/")
    fun getProdutos(): Call<List<Produto>>

    @GET("login")
    fun login(
        @Query("usuario") usuario: String,
        @Query("senha") senha: String
    ): Call<List<LoginResponse>>
}
