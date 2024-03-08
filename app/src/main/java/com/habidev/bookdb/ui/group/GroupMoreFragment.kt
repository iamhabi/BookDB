package com.habidev.bookdb.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.habidev.bookdb.data.GroupItem
import com.habidev.bookdb.databinding.GroupListMoreBinding
import com.habidev.bookdb.viewmodel.BookDBViewModel

class GroupMoreFragment: BottomSheetDialogFragment() {
    private val bookDBViewModel: BookDBViewModel by activityViewModels()

    private lateinit var viewBinding: GroupListMoreBinding

    private var groupItem: GroupItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = GroupListMoreBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewListener()
    }

    override fun onResume() {
        super.onResume()

        updateInfo()
    }

    fun setGroup(groupItem: GroupItem) {
        this.groupItem = groupItem
    }

    private fun updateInfo() {
        viewBinding.textViewTitle.text = groupItem?.title
    }

    private fun initViewListener() {
        viewBinding.btnDelete.setOnClickListener {
            groupItem?.let {
                bookDBViewModel.deleteGroup(it)

                dismiss()
            }
        }
    }
}