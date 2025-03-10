package com.example.news_app

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TopHeadlinesAdapter(val articles: List<Article>) : RecyclerView.Adapter<TopHeadlinesAdapter.ViewHolder>() {

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val title: TextView = rootLayout.findViewById(R.id.Name_source)
        val source: TextView = rootLayout.findViewById(R.id.Description_source)
        val image: ImageView = rootLayout.findViewById(R.id.icon)
        val realtitle:TextView = rootLayout.findViewById(R.id.mapstitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news_article, parent, false)
        return ViewHolder(rootLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.realtitle.text=article.title
        holder.title.text = article.source
        holder.source.text = article.content

        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = articles.size
}
