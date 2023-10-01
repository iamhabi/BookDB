package com.habidev.bookdb.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.habidev.bookdb.R
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.database.BookViewModelFactory
import com.habidev.bookdb.database.BooksApplication
import com.habidev.bookdb.databinding.DetailBinding

class DetailActivity: AppCompatActivity() {
    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as BooksApplication).repository)
    }

    private lateinit var viewBinding: DetailBinding

    private lateinit var bookItem: BookItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bookItem = intent.getParcelableExtra("bookItem", BookItem::class.java)!!
        }

        initViewListener()
    }

    override fun onStart() {
        super.onStart()

        updateInfo()
    }

    private fun updateInfo() {
        Glide
            .with(this)
            .load(Uri.parse(bookItem.imageUrl))
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.imageViewBookCover)

        viewBinding.textViewTitle.text = bookItem.title
        viewBinding.textViewAuthor.text = bookItem.author
        viewBinding.textViewDescription.text = bookItem.description

        viewBinding.spinnerReadingState.setSelection(bookItem.readingState)
        viewBinding.spinnerOwnState.setSelection(bookItem.ownState)
    }

    private fun initViewListener() {
        viewBinding.btnOpenInBrowser.setOnClickListener {
            val link: Uri = Uri.parse(bookItem.link)
            val intent = Intent(Intent.ACTION_VIEW, link)
            startActivity(intent)
        }

        viewBinding.btnClose.setOnClickListener {
            finish()
        }

        val readStateAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            BookItem.READ_STATE
        )

        val ownStateAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            BookItem.OWN_STATE
        )

        readStateAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        ownStateAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)

        viewBinding.spinnerReadingState.adapter = readStateAdapter
        viewBinding.spinnerOwnState.adapter = ownStateAdapter

        viewBinding.spinnerReadingState.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                bookItem.readingState = position

                bookViewModel.update(bookItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        viewBinding.spinnerOwnState.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                bookItem.ownState = position

                bookViewModel.update(bookItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}