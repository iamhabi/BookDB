package com.habidev.bookdb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.habidev.bookdb.databinding.BookListItemBinding

class BookListAdapter(
    private val context: Context,
    private var items: List<BookItem>,
    private var onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<BookListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])

        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onItemClickListener.onLongClick(position)
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val viewBinding = BookListItemBinding.bind(itemView)

        fun bind(item: BookItem) {
            val title: String? = item.getTitle()
            val imageUrl: String? = item.getImageUrl()

            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.drawable.book)
                .error(R.drawable.book)
                .into(viewBinding.bookListImage)

            viewBinding.bookListTitle.text = title
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
        fun onLongClick(position: Int): Boolean
    }
}