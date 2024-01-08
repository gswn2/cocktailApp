package com.example.cocktail.ui.categories

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
import com.example.cocktail.databinding.FragmentCategoriesBinding
import com.example.cocktail.ui.CategoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private lateinit var rv: RecyclerView
    private lateinit var CategoryAdapter: CategoryAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rv = root.findViewById(R.id.Categories)
        CategoryAdapter = CategoryAdapter(requireContext(), emptyArray())
        rv.adapter = CategoryAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        fetchDataFromApi()

        return root
    }

    private fun fetchDataFromApi() {
        // Initialisation de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)


        api.getCategories().enqueue(object : Callback<CategoriesResponse> {
            override fun onResponse(
                call: Call<CategoriesResponse>,
                response: Response<CategoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val ingredients = response.body()?.drinks?.mapNotNull { it.strCategory }
                    ingredients?.let {
                        // Log the data retrieved from the API
                        Log.d("API_DATA", it.toString())

                        // Update adapter data and notify data set changed
                        CategoryAdapter.setData(it.toTypedArray())
                    }
                } else {
                    // Handle error cases
                    Log.e("API_ERROR", "Failed to fetch data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
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