import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cocktail.R
import com.example.cocktail.ui.CocktailDataActivity
import java.util.Locale

class DataAdapter(
    private val context: Context,
    private var originalNames: Array<String>,
    private var originalImages: Array<String>,
    private var originalIds: Array<String>

) : RecyclerView.Adapter<DataAdapter.MyViewClass>() {

    private var filteredNames: MutableList<String> = mutableListOf()
    private var filteredImages: MutableList<String> = mutableListOf()
    private var filteredIds: MutableList<String> = mutableListOf()

    init {
        filteredNames.addAll(originalNames)
        filteredImages.addAll(originalImages)
        filteredIds.addAll(originalIds)

    }

    fun filter(text: String?) {
        filteredNames.clear()
        filteredImages.clear()
        if (text.isNullOrBlank()) {
            filteredNames.addAll(originalNames)
            filteredImages.addAll(originalImages)
        } else {
            val searchText = text.toLowerCase(Locale.getDefault())
            originalNames.forEachIndexed { index, name ->
                if (name.toLowerCase(Locale.getDefault()).contains(searchText)) {
                    filteredNames.add(name)
                    filteredImages.add(originalImages[index])
                }
            }
        }
        notifyDataSetChanged()
    }

    class MyViewClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtnom: TextView = itemView.findViewById(R.id.name)
        var logo: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewClass {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_template, parent, false)
        return MyViewClass(itemView)
    }

    override fun onBindViewHolder(holder: MyViewClass, position: Int) {
        val currentItemName = filteredNames[position]
        val currentItemImage = filteredImages[position]
        val currentItemId = filteredIds[position]

        holder.txtnom.text = currentItemName

        Glide.with(context)
            .load(currentItemImage)
            .into(holder.logo)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CocktailDataActivity::class.java)
            intent.putExtra("ID", currentItemId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredNames.size
    }

    fun setnamesData(namesData: Array<String>) {
        originalNames = namesData
        filteredNames.clear()
        filteredNames.addAll(namesData)
        notifyDataSetChanged()
    }

    fun setimagesData(imagesData: Array<String>) {
        originalImages = imagesData
        filteredImages.clear()
        filteredImages.addAll(imagesData)
        notifyDataSetChanged()
    }

    fun setIdsData(idsData: Array<String>) {
        originalIds = idsData
        filteredIds.clear()
        filteredIds.addAll(idsData)
        notifyDataSetChanged()
    }
}