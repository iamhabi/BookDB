package com.habidev.bookdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.habidev.bookdb.R
import com.habidev.bookdb.database.BookItem

class BookListAdapter(private val context: Context): RecyclerView.Adapter<BookListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onClick(position: Int, bookItem: BookItem)
        fun onLongClick(position: Int, bookItem: BookItem)
        fun onMoreClick(position: Int, bookItem: BookItem)
    }

    constructor(
        context: Context,
        layout: Int
    ): this(context) {
        this.layout = layout
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val items: MutableList<BookItem> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null

    private var layout: Int = R.layout.book_list_item
    private var isGridLayout = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = if (isGridLayout) {
            R.layout.book_list_item_grid
        } else {
            layout
        }

        val view = LayoutInflater
            .from(context)
            .inflate(layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        val view = holder.itemView

        val imageViewBookCover = view.findViewById<ImageView>(R.id.image_view_book_cover)
        val textViewTitle = view.findViewById<TextView>(R.id.text_view_title)
        val textViewAuthor = view.findViewById<TextView>(R.id.text_view_author)
        val btnMore = view.findViewById<ImageButton>(R.id.btn_more)

        Glide.with(imageViewBookCover)
            .load(item.imageUrl)
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(imageViewBookCover)

        textViewTitle.text = item.title
        textViewAuthor?.text = item.author

        view.setOnClickListener {
            onItemClickListener?.onClick(position, item)
        }

        view.setOnLongClickListener {
            onItemClickListener?.onLongClick(position, item)

            true
        }

        btnMore?.setOnClickListener {
            onItemClickListener?.onMoreClick(position, item)
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
        notifyItemRangeRemoved(0, itemCount)

        items.clear()
    }

    fun changeLayout(isGridLayout: Boolean) {
        this.isGridLayout = isGridLayout
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}