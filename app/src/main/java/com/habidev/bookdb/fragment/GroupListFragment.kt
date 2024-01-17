package com.habidev.bookdb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.adapter.GroupListAdapter
import com.habidev.bookdb.database.BookGroupItem
import com.habidev.bookdb.databinding.GroupListBinding

class GroupListFragment: Fragment() {
    companion object {
        private const val TAG = "BookDBGroupList"
    }

    private lateinit var viewBinding: GroupListBinding

    private lateinit var adapter: GroupListAdapter

    private val onItemClickListener = object : GroupListAdapter.OnItemClickListener {
        override fun onClick(position: Int, item: BookGroupItem) {

        }

        override fun onMoreClick(position: Int, item: BookGroupItem) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = GroupListBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initViewListener()
    }

    private fun initRecyclerView() {
        adapter = GroupListAdapter(requireContext())

        adapter.setOnItemClickListener(onItemClickListener)

        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun initViewListener() {
        viewBinding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewBinding.btnAddGroup.setOnClickListener {

        }

        viewBinding.layoutEmptySpace.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}