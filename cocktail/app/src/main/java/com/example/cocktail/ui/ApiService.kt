import com.example.cocktail.ui.CategoriesResponse
import com.example.cocktail.ui.IngredientsResponse
import com.example.cocktail.ui.CocktailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("list.php?i=list")
    fun getIngredients(): Call<IngredientsResponse>


    @GET("list.php?c=list")
    fun getCategories(): Call<CategoriesResponse>

    @GET("search.php")
    fun getDrink(@Query("s") searchTerm: String): Call<CocktailResponse>


    @GET("lookup.php")
    fun getDetails(@Query("i") id: String): Call<CocktailResponse>

    @GET("filter.php")
    fun getDrinksByCategories(@Query("c") category: String): Call<CocktailResponse>

    @GET("filter.php")
    fun getDrinksByIngredients(@Query("i") category: String): Call<CocktailResponse>
}
