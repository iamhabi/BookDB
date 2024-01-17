package com.habidev.bookdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.database.BookGroupItem
import com.habidev.bookdb.databinding.GroupListItemBinding

class GroupListAdapter(
    private val context: Context
): RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onClick(position: Int, item: BookGroupItem)
        fun onMoreClick(position: Int, item: BookGroupItem)
    }

    class ViewHolder(
        private val itemBinding: GroupListItemBinding,
        private val onItemClickListener: OnItemClickListener
    ): RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(position: Int, item: BookGroupItem) {
            itemBinding.textViewTitle.text = item.title

            itemBinding.btnMore.setOnClickListener {
                onItemClickListener.onMoreClick(position, item)
            }
        }
    }

    private val items: MutableList<BookGroupItem> = mutableListOf()
    private var onItemClickListener: OnItemClickListener

    init {
        onItemClickListener = object : OnItemClickListener {
            override fun onClick(position: Int, item: BookGroupItem) {

            }

            override fun onMoreClick(position: Int, item: BookGroupItem) {

            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)

        val itemBinding = GroupListItemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(itemBinding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView

        val item = items[position]

        holder.onBind(position, item)

        view.setOnClickListener {
            onItemClickListener.onClick(position, item)
        }
    }

    fun checkItemExist(bookItems: List<BookGroupItem>) {
        val deletedItems = items.filter { groupItem ->
            !bookItems.contains(groupItem)
        }

        val deletedIndex = mutableListOf<Int>()

        for (item in deletedItems) {
            val position = items.indexOf(item)

            deletedIndex.add(position)

            notifyItemRemoved(position)
        }

        for (index in deletedIndex) {
            items.removeAt(index)
        }
    }

    fun add(item: BookGroupItem) {
        if (!items.contains(item)) {
            items.add(item)

            notifyItemInserted(itemCount - 1)
        }
    }

    fun add(items: List<BookGroupItem>) {
        for (item in items) {
            add(item)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(0, itemCount)

        items.clear()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}