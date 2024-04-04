package com.habidev.bookdb.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.adapter.GroupListAdapter
import com.habidev.bookdb.api.BookDBClient
import com.habidev.bookdb.data.GroupItem
import com.habidev.bookdb.databinding.GroupListBinding
import com.habidev.bookdb.utils.Utils
import com.habidev.bookdb.viewmodel.BookDBViewModel

class GroupListFragment: Fragment() {
    interface OnGroupListener {
        fun onAllSelected()
        fun onGroupSelected(groupItem: GroupItem)
    }

    private val bookDBViewModel: BookDBViewModel by activityViewModels()

    private lateinit var viewBinding: GroupListBinding

    private lateinit var adapter: GroupListAdapter

    private lateinit var groupMoreFragment: GroupMoreFragment

    private var listener: OnGroupListener? = null

    private val onItemClickListener = object : GroupListAdapter.OnItemClickListener {
        override fun onClick(position: Int, item: GroupItem) {
            listener?.onGroupSelected(item)

            parentFragmentManager.popBackStack()
        }

        override fun onMoreClick(position: Int, item: GroupItem) {
            groupMoreFragment.setGroup(item)
            groupMoreFragment.show(childFragmentManager, null)
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

        groupMoreFragment = GroupMoreFragment()

        initRecyclerView()
        initViewListener()
    }

    override fun onStart() {
        super.onStart()

        bookDBViewModel.groups.observe(requireActivity()) { groups ->
            adapter.clear()
            adapter.add(groups)
        }
    }

    override fun onStop() {
        super.onStop()

        bookDBViewModel.allGroupsLiveData.removeObservers(requireActivity())
    }

    fun setOnGroupListener(listener: OnGroupListener) {
        this.listener = listener
    }

    private fun initRecyclerView() {
        adapter = GroupListAdapter(requireContext())

        adapter.setOnItemClickListener(onItemClickListener)

        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun createGroup() {
        val text = viewBinding.editTextAddGroup.text.toString()

        if (text == "") {
            return
        }

        BookDBClient.createGroup(text)

        viewBinding.editTextAddGroup.text.clear()

        Utils.closeKeyboard(requireActivity())

        // TODO refresh groups
    }

    private fun initViewListener() {
        Utils.setUpEditTextCloseKeyboard(requireActivity(), viewBinding.root)

        viewBinding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewBinding.layoutEmptySpace.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewBinding.textViewGroupAll.setOnClickListener {
            listener?.onAllSelected()

            parentFragmentManager.popBackStack()
        }

        viewBinding.btnAddGroup.setOnClickListener {
            createGroup()
        }

        viewBinding.editTextAddGroup.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Utils.showKeyboard(requireActivity(), view)
            } else {
                Utils.closeKeyboard(requireActivity())
            }
        }

        viewBinding.editTextAddGroup.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createGroup()
            }

            true
        }
    }
}