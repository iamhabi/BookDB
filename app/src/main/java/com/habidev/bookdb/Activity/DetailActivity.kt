package com.habidev.bookdb.Activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.habidev.bookdb.BookItem
import com.habidev.bookdb.R
import com.habidev.bookdb.databinding.BookDetailBinding

class DetailActivity: AppCompatActivity() {
    private lateinit var viewBinding: BookDetailBinding

    private lateinit var bookItem: BookItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = BookDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bookItem = intent.getParcelableExtra("bookItem", BookItem::class.java)!!
        }

        viewBinding.btnOpenInBrowser.setOnClickListener {
            val link: Uri = Uri.parse(bookItem.getLink())
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
            .load(Uri.parse(bookItem.getImageUrl()))
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.detailImage)

        viewBinding.detailTitle.text = bookItem.getTitle()
        viewBinding.detailAuthor.text = bookItem.getAuthor()
        viewBinding.detailDescription.text = bookItem.getDescription()
    }
}