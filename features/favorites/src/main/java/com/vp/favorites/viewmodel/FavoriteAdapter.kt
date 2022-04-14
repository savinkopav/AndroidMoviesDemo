package com.vp.favorites.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.detail.data.local.db.model.Movie
import com.vp.favorites.R

class FavoriteAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var items: List<Movie> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val image: ImageView = itemView.findViewById(R.id.favoritePoster)
        private val title: TextView = itemView.findViewById(R.id.favoriteTitle)
        private val description: TextView = itemView.findViewById(R.id.favoriteDescription)


        fun bind(movie: Movie) {
            title.text = movie.title
            description.text = movie.plot
        }

        override fun onClick(p0: View?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_item_list, parent, false)
        )
        viewHolder.itemView.setOnClickListener {
            viewHolder.adapterPosition.also { position ->
                listener.onItemClick(items[position].uid)
            }
        }
        return viewHolder
    }

    fun setItems(items: List<Movie>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)
        if ("N/A" != item.poster) {
            Glide
                .with(holder.image)
                .load(item.poster)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }
}