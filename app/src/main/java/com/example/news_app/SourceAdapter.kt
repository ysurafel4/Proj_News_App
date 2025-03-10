package com.example.news_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SourceAdapter(
    private var sources: MutableList<Source>,
    private val onSourceClick: (Source) -> Unit
) : RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout) {
        val name: TextView = rootLayout.findViewById(R.id.Name_source)
        val description: TextView = rootLayout.findViewById(R.id.Description_source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sources, parent, false)
        return ViewHolder(rootLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val source = sources[position]
        holder.name.text = source.name
        holder.description.text = source.description

        holder.itemView.setOnClickListener {
            onSourceClick(source) // Handle click event
        }
    }

    override fun getItemCount() = sources.size

    fun updateSources(newSources: List<Source>) {
        sources.clear()
        sources.addAll(newSources)
        notifyDataSetChanged()
    }
}
