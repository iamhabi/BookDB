package com.habidev.bookdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.habidev.bookdb.R
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.databinding.BookListItemBinding

class BookListAdapter(
    private val context: Context
): RecyclerView.Adapter<BookListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onClick(position: Int, bookItem: BookItem)
        fun onMoreClick(position: Int, bookItem: BookItem)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val items: MutableList<BookItem> = mutableListOf()
    private var onItemClickListener: OnItemClickListener

    init {
        onItemClickListener = object : OnItemClickListener {
            override fun onClick(position: Int, bookItem: BookItem) {

            }

            override fun onMoreClick(position: Int, bookItem: BookItem) {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.book_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewBinding = BookListItemBinding.bind(holder.itemView)

        val item = items[position]

        Glide.with(viewBinding.imageViewBookCover)
            .load(item.imageUrl)
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.imageViewBookCover)

        viewBinding.textViewTitle.text = item.title
        viewBinding.textViewAuthor.text = item.author

        viewBinding.root.setOnClickListener {
            onItemClickListener.onClick(position, item)
        }

        viewBinding.btnMore.setOnClickListener {
            onItemClickListener.onMoreClick(position, item)
        }
    }

    override fun getItemCount() = items.size

    fun add(item: BookItem) {
        if (!items.contains(item)) {
            items.add(item)

            notifyItemInserted(itemCount - 1)
        }
    }

    fun add(items: List<BookItem>) {
        for (item in items) {
            add(item)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(0, itemCount - 1)

        items.clear()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}