package com.habidev.bookdb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.habidev.bookdb.data.BookItem

class DetailInfoViewModel : ViewModel() {
    private val _bookItem: MutableLiveData<BookItem> = MutableLiveData()

    private val _imageUrl: MutableLiveData<String> = MutableLiveData()
    private val _title: MutableLiveData<String> = MutableLiveData()
    private val _author: MutableLiveData<String> = MutableLiveData()
    private val _link: MutableLiveData<String> = MutableLiveData()
    private val _description: MutableLiveData<String> = MutableLiveData()
    private val _comment: MutableLiveData<String?> = MutableLiveData()

    val bookItem: LiveData<BookItem> = _bookItem

    val imageUrl: LiveData<String> = _imageUrl
    val title: LiveData<String> = _title
    val author: LiveData<String> = _author
    val link: LiveData<String> = _link
    val description: LiveData<String> = _description
    val comment: LiveData<String?> = _comment

    fun setBookItem(bookItem: BookItem) {
        _bookItem.value = bookItem

        _imageUrl.value = bookItem.imageUrl
        _title.value = bookItem.title
        _author.value = bookItem.author
        _link.value = bookItem.link
        _description.value = bookItem.description
        _comment.value = bookItem.comment
    }
}