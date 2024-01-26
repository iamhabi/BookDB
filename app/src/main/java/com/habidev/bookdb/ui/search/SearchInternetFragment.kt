package com.habidev.bookdb.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.R
import com.habidev.bookdb.SearchViewModel
import com.habidev.bookdb.adapter.BookListAdapter
import com.habidev.bookdb.api.ApiClient
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.databinding.RecyclerViewBaseBinding
import com.habidev.bookdb.ui.main.SomeInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class SearchInternetFragment : Fragment() {
    companion object {
        private const val TAG = "SearchInternet"
    }

    private val searchViewModel: SearchViewModel by activityViewModels()

    private lateinit var viewBinding: RecyclerViewBaseBinding

    private lateinit var adapter: BookListAdapter

    private var someInterface: SomeInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        someInterface = context as? SomeInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = RecyclerViewBaseBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        searchViewModel.query.observe(requireActivity()) { query ->
            performSearch(query)
        }
    }

    override fun onStop() {
        super.onStop()

        searchViewModel.query.removeObservers(requireActivity())
    }

    private fun performSearch(query: String) {
        if (!this::adapter.isInitialized) {
            return
        }

        adapter.clear()

        if (query == "") {
            return
        }

        ApiClient.search(
            query = query,
            listener =object : ApiClient.Companion.OnResultListener {
                override fun onResult(result: String) {
                    showResult(result)
                }
            }
        )
    }

    private fun showResult(resultJson: String) {
        val resultJsonArray = JSONObject(resultJson).getJSONArray("items")

        for (i in 0 until resultJsonArray.length()) {
            val jsonObject = resultJsonArray.getJSONObject(i)

            val isbn = (jsonObject.get("isbn") as String).toLong()
            val link = jsonObject.get("link") as String
            val title = jsonObject.get("title") as String
            val author = jsonObject.get("author") as String
            val imageUrl = jsonObject.get("image") as String
            val description = jsonObject.get("description") as String

            val bookItem = BookItem(
                isbn,
                link,
                title,
                author,
                imageUrl,
                description
            )

            CoroutineScope(Dispatchers.Main).launch  {
                adapter.add(bookItem)
            }
        }
    }

    private fun initRecyclerView() {
        adapter = BookListAdapter(requireContext(), R.layout.book_list_item_vanilla)

        adapter.setOnItemClickListener(object : BookListAdapter.OnItemClickListener {
            override fun onClick(position: Int, bookItem: BookItem) {
                someInterface?.showResultInfo(bookItem.isbn.toString())
            }

            override fun onLongClick(position: Int, bookItem: BookItem) {
                someInterface?.showResultInfo(bookItem.isbn.toString())
            }

            override fun onMoreClick(position: Int, bookItem: BookItem) {
            }
        })

        viewBinding.recyclerViewBase.adapter = adapter
        viewBinding.recyclerViewBase.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }
}
