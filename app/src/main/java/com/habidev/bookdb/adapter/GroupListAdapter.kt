package com.habidev.bookdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.data.GroupItem
import com.habidev.bookdb.databinding.GroupListItemBinding

class GroupListAdapter(
    private val context: Context
): RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onClick(position: Int, item: GroupItem)
        fun onMoreClick(position: Int, item: GroupItem)
    }

    inner class ViewHolder(
        private val itemBinding: GroupListItemBinding,
        private val onItemClickListener: OnItemClickListener
    ): RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(position: Int, item: GroupItem) {
            itemBinding.textViewTitle.text = item.title

            if (useMore) {
                itemBinding.btnMore.setOnClickListener {
                    onItemClickListener.onMoreClick(position, item)
                }
            } else {
                itemBinding.btnMore.visibility = View.GONE
            }
        }
    }

    private val items: MutableList<GroupItem> = mutableListOf()
    private var onItemClickListener: OnItemClickListener

    private var useMore: Boolean = true

    init {
        onItemClickListener = object : OnItemClickListener {
            override fun onClick(position: Int, item: GroupItem) {

            }

            override fun onMoreClick(position: Int, item: GroupItem) {

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

    fun useMore(useMore: Boolean) {
        this.useMore = useMore
    }

    fun checkItemExist(bookItems: List<GroupItem>) {
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

    fun add(item: GroupItem) {
        if (!items.contains(item)) {
            items.add(item)

            notifyItemInserted(itemCount - 1)
        }
    }

    fun add(items: List<GroupItem>) {
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