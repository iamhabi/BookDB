package com.habidev.bookdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.habidev.bookdb.R
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.databinding.BookListItemBinding
import com.habidev.bookdb.databinding.SearchHeaderBinding

class SearchAdapter(
    private val context: Context,
    private val titleId: Int
): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private class ViewTypes {
        companion object {
            const val HEADER: Int = 0
            const val NORMAL: Int = 1
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onTitle(view: View) {
            SearchHeaderBinding.bind(view).textViewSearchHeader.text = context.resources.getString(titleId)
        }

        fun onBind(item: BookItem, view: View, position: Int) {
            BookListItemBinding.bind(view).let { binding ->
                Glide.with(binding.imageViewBookCover)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.book)
                    .error(R.drawable.book)
                    .into(binding.imageViewBookCover)

                binding.textViewTitle.text = item.title
                binding.textViewAuthor.text = item.author

                view.setOnClickListener {
                    onItemClickListener?.onClick(position, item)
                }

                view.setOnLongClickListener {
                    onItemClickListener?.onLongClick(position, item)

                    true
                }

                binding.btnMore.setOnClickListener {
                    onItemClickListener?.onMoreClick(position, item)
                }
            }
        }
    }

    private val items: MutableList<BookItem> = mutableListOf()
    private var onItemClickListener: BookListAdapter.OnItemClickListener? = null

    private var layout: Int = R.layout.book_list_item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = when (viewType) {
            ViewTypes.HEADER -> LayoutInflater
                .from(context)
                .inflate(R.layout.search_header, parent, false)
            else -> LayoutInflater
                .from(context)
                .inflate(layout, parent, false)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ViewTypes.HEADER -> holder.onTitle(holder.itemView)
            else -> holder.onBind(items[position - 1], holder.itemView, position)
        }
    }

    override fun getItemCount() = items.size + 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewTypes.HEADER
            else -> ViewTypes.NORMAL
        }
    }

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

    fun deleteNotMatchedItems(query: String) {
        items.filter { item ->
            !item.title.contains(query)
        }.let { notMatchedItems ->
            notMatchedItems.forEach { remove(it) }
        }
    }

    fun remove(item: BookItem) {
        items.indexOf(item).let { index ->
            if (index == -1) {
                return
            }

            notifyItemRemoved(index)

            items.removeAt(index)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(1, items.size)

        items.clear()
    }

    fun setOnItemClickListener(listener: BookListAdapter.OnItemClickListener) {
        this.onItemClickListener = listener
    }
}