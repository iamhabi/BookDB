package com.habidev.bookdb.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.R
import com.habidev.bookdb.databinding.DetailBinding

class DetailActivity: AppCompatActivity() {
    private lateinit var viewBinding: DetailBinding

    private lateinit var bookItem: BookItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bookItem = intent.getParcelableExtra("bookItem", BookItem::class.java)!!
        }

        viewBinding.btnOpenInBrowser.setOnClickListener {
            val link: Uri = Uri.parse(bookItem.link)
            val intent = Intent(Intent.ACTION_VIEW, link)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        setInfo()
    }

    private fun setInfo() {
        Glide
            .with(this)
            .load(Uri.parse(bookItem.imageUrl))
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.detailImage)

        viewBinding.detailTitle.text = bookItem.title
        viewBinding.detailAuthor.text = bookItem.author
        viewBinding.detailDescription.text = bookItem.description
    }
}