package com.habidev.bookdb.ui.search

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.habidev.bookdb.R
import com.habidev.bookdb.adapter.SimpleViewPagerAdapter
import com.habidev.bookdb.databinding.SearchBinding
import com.habidev.bookdb.utils.Utils

class SearchFragment : Fragment() {
    companion object {
        private const val TAG = "Search"
    }

    private lateinit var viewBinding: SearchBinding

    private lateinit var searchDBFrag: SearchDBFragment
    private lateinit var searchInternetFrag: SearchInternetFragment

    private var query: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = SearchBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()

        initViewListener()

        savedInstanceState?.let { bundle ->
            query = bundle.getString("query", "")

            viewBinding.editTextSearch.setText(query)
        }
    }

    override fun onResume() {
        super.onResume()

        if (viewBinding.editTextSearch.text.toString() == "") {
            viewBinding.editTextSearch.requestFocus()

            Utils.showKeyboard(requireContext(), viewBinding.editTextSearch)
        } else {
            performSearch(query)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("query", query)
    }

    private fun performSearch(query: String) {
        searchDBFrag.performSearch(query)
        searchInternetFrag.performSearch(query)
    }

    private fun initViewPager() {
        searchDBFrag = SearchDBFragment()
        searchInternetFrag = SearchInternetFragment()

        val fragments = arrayListOf(
            searchDBFrag,
            searchInternetFrag
        )

        val adapter = SimpleViewPagerAdapter(requireActivity(), fragments)

        viewBinding.viewPagerSearch.adapter = adapter
        viewBinding.viewPagerSearch.offscreenPageLimit = fragments.size

        TabLayoutMediator(viewBinding.tabLayoutSearch, viewBinding.viewPagerSearch) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.database)
                1 -> resources.getString(R.string.internet)
                else -> ""
            }
        }.attach()
    }

    private fun initViewListener() {
        viewBinding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewBinding.editTextSearch.addTextChangedListener { text: Editable? ->
            text?.toString()?.let { query ->
                this.query = query

                performSearch(query)
            }
        }

        viewBinding.editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Utils.showKeyboard(requireContext(), view)
            } else {
                Utils.hideKeyboard(requireContext(), view)
            }
        }
    }
}