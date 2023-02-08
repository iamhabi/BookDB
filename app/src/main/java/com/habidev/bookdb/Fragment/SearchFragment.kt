package com.habidev.bookdb.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.habidev.bookdb.Adapter.SearchViewPagerAdapter
import com.habidev.bookdb.BookViewModel
import com.habidev.bookdb.R
import com.habidev.bookdb.databinding.SearchBinding

class SearchFragment: Fragment() {
    private lateinit var viewBinding: SearchBinding

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = SearchBinding.inflate(inflater, container, false)

        // TODO
        //  Request Focus to Edit Text
        //  show keyboard

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchViewPagerAdapter(requireActivity())

        viewBinding.viewPagerSearch.adapter = adapter

        TabLayoutMediator(viewBinding.viewPagerTabLayoutSearch, viewBinding.viewPagerSearch) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = ContextCompat.getDrawable(requireContext(), R.drawable.collections_bookmark)
                    tab.text = "My Library"
                }
                1 -> {
                    tab.icon = ContextCompat.getDrawable(requireContext(), R.drawable.cloud)
                    tab.text = "Internet"
                }
            }
        }.attach()

        initListener()
    }

    private fun initListener() {
        viewBinding.btnSearch.setOnClickListener {
            // TODO
            //  hide keyboard
            //  Remove Focus from Edit Text

            val query: String = viewBinding.editTextSearch.text.toString()

            if (query == "") {
                Toast.makeText(requireContext(), "Input Search Query", Toast.LENGTH_SHORT).show()
            } else {
                bookViewModel.searchQuery.value = query
            }
        }
    }
}