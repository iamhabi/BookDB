package com.habidev.bookdb.ui.book

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.habidev.bookdb.R
import com.habidev.bookdb.adapter.GroupListAdapter
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.viwemodel.BookDBViewModel
import com.habidev.bookdb.data.GroupItem
import com.habidev.bookdb.databinding.GroupSelectBinding

class GroupSelectFragment : BottomSheetDialogFragment(R.layout.group_select) {
    companion object {
        private const val TAG = "GroupSelect"
    }

    private val bookDBViewModel: BookDBViewModel by activityViewModels()

    private lateinit var viewBinding: GroupSelectBinding

    private lateinit var adapter: GroupListAdapter

    private var bookItem: BookItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = GroupSelectBinding.bind(view)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED

        initGroupList()
        initViewListener()
    }

    override fun onStart() {
        super.onStart()

        bookDBViewModel.allGroupsLiveData.observe(requireActivity()) { groups ->
            adapter.add(groups)
        }
    }

    override fun onStop() {
        super.onStop()

        bookDBViewModel.allGroupsLiveData.removeObservers(requireActivity())
    }

    fun setBookItem(bookItem: BookItem) {
        this.bookItem = bookItem
    }

    private fun initGroupList() {
        val context = requireContext()

        adapter = GroupListAdapter(context)

        adapter.useMore(false)

        viewBinding.recyclerViewGroup.adapter = adapter
        viewBinding.recyclerViewGroup.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter.setOnItemClickListener(object : GroupListAdapter.OnItemClickListener {
            override fun onClick(position: Int, item: GroupItem) {
                Log.d(TAG, "onClick $position")

                bookItem?.let {
                    bookDBViewModel.insertBookIntoGroup(it, item)
                }

                dismiss()
            }

            override fun onMoreClick(position: Int, item: GroupItem) {
            }
        })
    }

    private fun initViewListener() {
        viewBinding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}