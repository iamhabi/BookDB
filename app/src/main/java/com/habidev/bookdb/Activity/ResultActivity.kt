package com.habidev.bookdb.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.habidev.bookdb.*
import com.habidev.bookdb.databinding.BookResultBinding
import org.json.JSONObject

class ResultActivity: AppCompatActivity() {
    private val tag: String = "Result Activity"

    private lateinit var viewBinding: BookResultBinding

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as BooksApplication).repository)
    }

    private lateinit var item: JSONObject

    private var barcode: Int = 0
    private lateinit var link: String
    private lateinit var title: String
    private lateinit var author: String
    private lateinit var imageUrl: String
    private lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = BookResultBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initOnClickListener()

        barcode = getBarcodeFromBundle()
        val resultJson = readJsonFromBundle()

        if (resultJson != null) {
            getInfoFromJsonObject(resultJson)
            setInfo()
        } else {
            Log.e(tag, "Result is null")
        }
    }

    private fun getBarcodeFromBundle(): Int {
        val bundle = intent.extras

        return bundle?.getInt("barcode") ?: -1
    }

    private fun readJsonFromBundle(): String? {
        val bundle = intent.extras

        return bundle?.getString("result")
    }

    private fun getInfoFromJsonObject(resultJson: String) {
        item = JSONObject(resultJson).getJSONArray("items").getJSONObject(0)

        link = item.get("link") as String
        title = item.get("title") as String
        author = item.get("author") as String
        imageUrl = item.get("image") as String
        description = item.get("description") as String
    }

    private fun setInfo() {
        Glide
            .with(this)
            .load(Uri.parse(imageUrl))
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.resultImage)

        viewBinding.resultTitle.text = title
        viewBinding.resultAuthor.text = author
        viewBinding.resultDescription.text = description
    }

    private fun addToDatabase() {
        bookViewModel.insert(
            BookItem(barcode, link, title, author, imageUrl, description)
        )

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun initOnClickListener() {
        viewBinding.btnOpenInBrowser.setOnClickListener {
            val linkUri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, linkUri)
            startActivity(intent)
        }

        viewBinding.btnAddBookmark.setOnClickListener {
            addToDatabase()
        }
    }
}