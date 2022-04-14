package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.model.ListItem
import java.util.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var listItems: MutableList<ListItem> = Collections.emptyList()
    private val EMPTY_ON_ITEM_CLICK_LISTENER: OnItemClickListener =
        object : OnItemClickListener {
            override fun onItemClick(imdbID: String?) {

            }
        }
    private var onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem: ListItem = listItems[position]
        if (listItem.poster != null && NO_IMAGE != listItem.poster) {
            val density: Float = holder.image.resources.displayMetrics.density
            GlideApp
                .with(holder.image)
                .load(listItem.poster)
                .override((300 * density).toInt(), (600 * density).toInt())
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: MutableList<ListItem>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(@Nullable onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
        }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView
        override fun onClick(v: View?) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)
    }

    companion object {
        private const val NO_IMAGE = "N/A"
    }
}