package com.example.cocktail.ui

import ApiService
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cocktail.R
import com.example.cocktail.databinding.CocktailInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CocktailDataActivity : AppCompatActivity() {
    private lateinit var binding: CocktailInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CocktailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Call API to fetch data
        val id = intent.getStringExtra("ID")
        if (id != null) {
            fetchDataFromApi(id)
        } else {
            // Gérez le cas où l'ID n'est pas présent
        }    }

    private fun fetchDataFromApi(id : String) {
        // Initialisation de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)


        api.getDetails(id).enqueue(object : Callback<CocktailResponse> {
            override fun onResponse(
                call: Call<CocktailResponse>,
                response: Response<CocktailResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.drinks?.get(0)?.let { drink ->
                        binding.cocktailName.text = drink.strDrink
                        binding.Category.text = getString(R.string.category_label, drink.strCategory)
                        binding.ServedIn.text = getString(R.string.Served_in_label, drink.strGlass)
                        binding.instructionDetails.text = drink.strInstructions
                        Glide.with(this@CocktailDataActivity).load(drink.strDrinkThumb).into(binding.cocktailImage)

                        val ingredients = StringBuilder()
                        for (i in 1..15) {
                            val ingredient = when (i) {
                                1 -> drink.strIngredient1
                                2 -> drink.strIngredient2
                                3 -> drink.strIngredient3
                                4 -> drink.strIngredient4
                                5 -> drink.strIngredient5
                                6 -> drink.strIngredient6
                                7 -> drink.strIngredient7
                                8 -> drink.strIngredient8
                                9 -> drink.strIngredient9
                                10 -> drink.strIngredient10
                                11 -> drink.strIngredient11
                                12 -> drink.strIngredient12
                                13 -> drink.strIngredient13
                                14 -> drink.strIngredient14
                                15 -> drink.strIngredient15
                                else -> null
                            }

                            val measure = when (i) {
                                1 -> drink.strMeasure1
                                2 -> drink.strMeasure2
                                3 -> drink.strMeasure3
                                4 -> drink.strMeasure4
                                5 -> drink.strMeasure5
                                6 -> drink.strMeasure6
                                7 -> drink.strMeasure7
                                8 -> drink.strMeasure8
                                9 -> drink.strMeasure9
                                10 -> drink.strMeasure10
                                11 -> drink.strMeasure11
                                12 -> drink.strMeasure12
                                13 -> drink.strMeasure13
                                14 -> drink.strMeasure14
                                15 -> drink.strMeasure15
                                else -> null
                            }

                            if (!ingredient.isNullOrEmpty()) {
                                ingredients.append(ingredient).append(" (").append(measure ?: "").append(")\n")
                            }
                        }

                        binding.IngredientsList.text = ingredients.toString().trim()
                    }
                } else {
                    Log.e("API_ERROR", "Échec de la récupération des données: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                Log.e("API_FAILURE", "La requête API a échoué: ${t.message}")
            }
        })

    }
}

