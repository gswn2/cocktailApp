package com.example.cocktail.ui.recherche

import ApiService
import DataAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import com.example.cocktail.R
import com.example.cocktail.databinding.FragmentRechercheBinding
import com.example.cocktail.ui.CocktailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RechercheFragment : Fragment() {

    private var _binding: FragmentRechercheBinding? = null
    private lateinit var rv: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var DataAdapter: DataAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rechercheViewModel =
            ViewModelProvider(this)[RechercheViewModel::class.java]

        _binding = FragmentRechercheBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rv = root.findViewById(R.id.list_recherche)
        DataAdapter = DataAdapter(requireContext(), emptyArray(), emptyArray(), emptyArray())
        rv.adapter = DataAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())


        searchView = root.findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetchDataFromApi(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                DataAdapter.filter(newText)
                return true
            }
        })

        return root
    }

    private fun fetchDataFromApi(searchTerm: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getDrink(searchTerm).enqueue(object : Callback<CocktailResponse> {
            override fun onResponse(
                call: Call<CocktailResponse>, response: Response<CocktailResponse>
            ) {
                if (response.isSuccessful) {
                    val images = response.body()?.drinks?.mapNotNull { it.strDrinkThumb }
                    val names = response.body()?.drinks?.mapNotNull { it.strDrink }
                    val ids = response.body()?.drinks?.mapNotNull { it.idDrink }

                    images?.let { DataAdapter.setimagesData(it.toTypedArray()) }
                    names?.let { DataAdapter.setnamesData(it.toTypedArray()) }
                    ids?.let { DataAdapter.setIdsData(it.toTypedArray()) }
                } else {
                    Log.e("API_ERROR", "Failed to fetch data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                Log.e("API_FAILURE", "API request failed: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
