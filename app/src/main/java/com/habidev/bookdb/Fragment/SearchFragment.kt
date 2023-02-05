package com.habidev.bookdb.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.habidev.bookdb.Activity.DetailActivity
import com.habidev.bookdb.BookItem
import com.habidev.bookdb.BookListAdapter
import com.habidev.bookdb.BookViewModel
import com.habidev.bookdb.databinding.SearchBinding

class SearchFragment: Fragment() {
    private lateinit var viewBinding: SearchBinding

    private val bookViewModel: BookViewModel by activityViewModels()

    private val dbResultList: MutableList<BookItem> = mutableListOf()
    private val internetResultList: MutableList<BookItem> = mutableListOf()

    private lateinit var dbListAdapter: BookListAdapter
    private lateinit var internetListAdapter: BookListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = SearchBinding.inflate(inflater, container, false)

        // TODO
        //  show keyboard

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB Search Result Recycler View
        dbListAdapter = BookListAdapter(requireContext(), dbResultList, dbOnItemClickListener)

        viewBinding.recyclerViewDbResult.adapter = dbListAdapter
        viewBinding.recyclerViewDbResult.layoutManager = LinearLayoutManager(context)

        // Internet Search Result Recycler View
        internetListAdapter = BookListAdapter(requireContext(), internetResultList, internetOnItemClickListener)

        viewBinding.recyclerViewInternetResult.adapter = internetListAdapter
        viewBinding.recyclerViewInternetResult.layoutManager = LinearLayoutManager(context)

        initListener()
    }

    private fun initListener() {
        viewBinding.btnSearch.setOnClickListener {
            // TODO
            //  hide keyboard

            val searchString: String = viewBinding.editTextSearch.text.toString()

            if (searchString == "") {
                Toast.makeText(requireContext(), "Input Search Query", Toast.LENGTH_SHORT).show()
            } else {
                viewBinding.textViewDbResult.visibility = View.VISIBLE

                bookViewModel.search(searchString).observe(requireActivity()) { resultList ->
                    resultList.let {
                        dbResultList.clear()
                        dbResultList.addAll(resultList)

                        dbListAdapter.notifyItemRangeInserted(0, dbResultList.size)
                    }
                }
            }
        }
    }

    private val dbOnItemClickListener = object: BookListAdapter.OnItemClickListener {
        override fun onClick(position: Int) {
            val bookItem = dbResultList[position]

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("bookItem", bookItem)
            startActivity(intent)
        }

        override fun onLongClick(position: Int): Boolean {
            return true
        }
    }

    private val internetOnItemClickListener = object: BookListAdapter.OnItemClickListener {
        override fun onClick(position: Int) {
            val bookItem = internetResultList[position]

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("bookItem", bookItem)
            startActivity(intent)
        }

        override fun onLongClick(position: Int): Boolean {
            return true
        }
    }
}