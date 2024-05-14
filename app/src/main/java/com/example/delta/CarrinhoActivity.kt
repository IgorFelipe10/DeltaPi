package com.example.delta

import android.content.Intent
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
    private lateinit var cartAdapter: CarrinhoAdapter
    private var items: MutableList<Produto> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)

        recyclerView = findViewById(R.id.cartRecyclerView)
        totalTextView = findViewById(R.id.totalTextView)
        goToPaymentButton = findViewById(R.id.goToPaymentButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchCartItems()

        goToPaymentButton.setOnClickListener {
            val intent = Intent(this, PagamentoActivity::class.java).apply {
                putExtra("TOTAL", total)
                putExtra("USER", 271)
                putParcelableArrayListExtra("PRODUCT_LIST", ArrayList(items))
            }
            startActivity(intent)
        }
    }

    private fun fetchCartItems() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://cace88f8-32cd-4dfe-a27c-cd47f72eee6a-00-36qyzm7anwvng.kirk.replit.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(CarrinhoApiService::class.java)
        api.getCartItems(userId = 271).enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    items = response.body()?.toMutableList() ?: mutableListOf()
                    setupAdapter()
                    updateTotal()
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {

            }
        })
    }

    private fun setupAdapter() {
        cartAdapter = CarrinhoAdapter(items, this) { updateTotal() }
        recyclerView.adapter = cartAdapter
    }

    fun updateTotal() {
        total = items.sumOf { it.produtoPreco.toDouble() * it.quantidadeDisponivel }

        runOnUiThread {
            totalTextView.text = "Total: R$${String.format("%.2f", total)}"
        }
    }
}
