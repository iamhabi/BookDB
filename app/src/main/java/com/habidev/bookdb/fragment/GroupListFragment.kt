package com.habidev.bookdb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.adapter.GroupListAdapter
import com.habidev.bookdb.database.BookGroupItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.databinding.GroupListBinding
import com.habidev.bookdb.utils.Utils

class GroupListFragment: Fragment() {
    companion object {
        private const val TAG = "BookDBGroupList"
    }

    private val bookViewModel: BookViewModel by activityViewModels()

    private lateinit var viewBinding: GroupListBinding

    private lateinit var adapter: GroupListAdapter

    private val onItemClickListener = object : GroupListAdapter.OnItemClickListener {
        override fun onClick(position: Int, item: BookGroupItem) {
            bookViewModel.setGroup(item)

            parentFragmentManager.popBackStack()
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

    override fun onStart() {
        super.onStart()

        bookViewModel.allGroupsLiveData.observe(requireActivity()) { groups ->
            adapter.clear()
            adapter.add(groups)
        }
    }

    override fun onStop() {
        super.onStop()

        bookViewModel.allGroupsLiveData.removeObservers(requireActivity())
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

        viewBinding.layoutEmptySpace.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewBinding.textViewGroupAll.setOnClickListener {
            bookViewModel.setGroup(BookGroupItem(0, "All"))

            parentFragmentManager.popBackStack()
        }

        viewBinding.btnAddGroup.setOnClickListener {
            val text = viewBinding.editTextAddGroup.text.toString()

            if (text == "") {
                viewBinding.editTextAddGroup.requestFocus()
            } else {
                viewBinding.editTextAddGroup.text.clear()

                bookViewModel.insertGroup(BookGroupItem(0, text))
            }
        }

        viewBinding.editTextAddGroup.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Utils.showKeyboard(requireContext(), view)
            } else {
                view.clearFocus()
                Utils.hideKeyboard(requireContext(), view)
            }
        }
    }
}