package com.habidev.bookdb.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.R
import com.habidev.bookdb.activity.SomeInterface
import com.habidev.bookdb.adapter.BookListAdapter
import com.habidev.bookdb.api.ApiClient
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.databinding.RecyclerViewBaseBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class SearchInternetFragment : Fragment() {
    companion object {
        private const val TAG = "SearchInternet"
    }

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
        viewBinding.recyclerViewBase.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    fun performSearch(query: String) {
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
}
