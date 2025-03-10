import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news_app.Article
import com.example.news_app.R

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var newsList: List<Article> = listOf()

    fun updateNews(news: List<Article>) {
        newsList = news
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.articlemaps, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = newsList[position]
        holder.title.text = article.title
        holder.description.text = article.description ?: "No description available"
        Glide.with(holder.image.context).load(article.urlToImage).into(holder.image)
    }

    override fun getItemCount(): Int = newsList.size

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.mapstitle)
        val description: TextView = view.findViewById(R.id.mapsDescription_article)
        val image: ImageView = view.findViewById(R.id.mapsimagearticle)
    }
}
