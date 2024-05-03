package com.example.delta

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarrinhoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalTextView: TextView
    private lateinit var goToPaymentButton: Button
    private var total: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)

        recyclerView = findViewById(R.id.cartRecyclerView)
        totalTextView = findViewById(R.id.totalTextView)
        goToPaymentButton = findViewById(R.id.goToPaymentButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchCartItems()

        goToPaymentButton.setOnClickListener {
            // Ir para tela de pagamento enviando os dados
        }
    }

    private fun fetchCartItems() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://de54397e-a23c-4def-9eb4-b07dde659faa-00-14c35j45l3isu.picard.repl.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(CarrinhoApiService::class.java)
        api.getCartItems(userId = 271).enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    val cartItems = response.body()?.toMutableList() ?: mutableListOf()
                    recyclerView.adapter = CarrinhoAdapter(cartItems, this@CarrinhoActivity) {
                        total = cartItems.sumOf { it.produtoPreco.toDouble() * it.quantidadeDisponivel }
                        totalTextView.text = "Total: R$${String.format("%.2f", total)}"
                    }
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                // Tratamento de falhas
            }
        })
    }
}
