package com.example.cocktail.ui.ingredients

import ApiService
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktail.R
import com.example.cocktail.databinding.FragmentIngredientsBinding
import com.example.cocktail.ui.IngredientsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private lateinit var rv: RecyclerView
    private lateinit var IngredientsAdapter: IngredientsAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rv = root.findViewById(R.id.Ingredients)
        IngredientsAdapter = IngredientsAdapter(requireContext(), emptyArray())
        rv.adapter = IngredientsAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        fetchDataFromApi()

        return root
    }

    private fun fetchDataFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)


        api.getIngredients().enqueue(object : Callback<IngredientsResponse> {
            override fun onResponse(
                call: Call<IngredientsResponse>,
                response: Response<IngredientsResponse>
            ) {
                if (response.isSuccessful) {
                    val ingredients = response.body()?.drinks?.mapNotNull { it.strIngredient1 }
                    ingredients?.let {
                        Log.d("API_DATA", it.toString())

                        IngredientsAdapter.setData(it.toTypedArray())
                    }
                } else {
                    // Handle error cases
                    Log.e("API_ERROR", "Failed to fetch data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<IngredientsResponse>, t: Throwable) {
                // Handle request failures
                Log.e("API_FAILURE", "API request failed: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}