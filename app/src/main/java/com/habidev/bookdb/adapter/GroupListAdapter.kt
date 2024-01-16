package com.habidev.bookdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.databinding.GroupListItemBinding

class GroupListAdapter(
    private val context: Context
): RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onClick(position: Int, item: String)
        fun onMoreClick(position: Int, item: String)
    }

    class ViewHolder(
        private val itemBinding: GroupListItemBinding,
        private val onItemClickListener: OnItemClickListener
    ): RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(position: Int, item: String) {
            itemBinding.textViewTitle.text = item

            itemBinding.btnMore.setOnClickListener {
                onItemClickListener.onMoreClick(position, item)
            }
        }
    }

    private val items: MutableList<String> = mutableListOf()
    private val onItemClickListener: OnItemClickListener

    init {
        onItemClickListener = object : OnItemClickListener {
            override fun onClick(position: Int, item: String) {

            }

            override fun onMoreClick(position: Int, item: String) {

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
}