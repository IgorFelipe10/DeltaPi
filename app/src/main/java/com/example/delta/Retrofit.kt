package com.example.delta

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class Retrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://80f937eb-5579-48c7-ab02-528b0a91c0b3-00-2w3b8ix9w0kuu.spock.replit.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}
