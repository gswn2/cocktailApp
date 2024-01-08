package com.example.cocktail.ui.ingredients

import ApiService
import DataAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktail.databinding.FragmentIngredientsBinding
import com.example.cocktail.ui.CocktailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Ingredientsfilter : AppCompatActivity() {

    private lateinit var binding: FragmentIngredientsBinding
    private lateinit var rv: RecyclerView
    private lateinit var dataAdapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rv = binding.Ingredients
        dataAdapter = DataAdapter(this, emptyArray(), emptyArray(), emptyArray())
        rv.adapter = dataAdapter
        rv.layoutManager = LinearLayoutManager(this)

        val ingredient = intent.getStringExtra("ingredient")
        if (ingredient != null) {
            fetchDataFromApi(ingredient)
        } else {
        }
    }

    private fun fetchDataFromApi(searchTerm: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getDrinksByIngredients(searchTerm).enqueue(object : Callback<CocktailResponse> {
            override fun onResponse(
                call: Call<CocktailResponse>, response: Response<CocktailResponse>
            ) {
                if (response.isSuccessful) {
                    val images = response.body()?.drinks?.mapNotNull { it.strDrinkThumb }
                    val names = response.body()?.drinks?.mapNotNull { it.strDrink }
                    val ids = response.body()?.drinks?.mapNotNull { it.idDrink }

                    images?.let { dataAdapter.setimagesData(it.toTypedArray()) }
                    names?.let { dataAdapter.setnamesData(it.toTypedArray()) }
                    ids?.let { dataAdapter.setIdsData(it.toTypedArray()) }
                } else {
                    Log.e("API_ERROR", "Failed to fetch data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                Log.e("API_FAILURE", "API request failed: ${t.message}")
            }
        })
    }
}
