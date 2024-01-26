package com.habidev.bookdb

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {
    val query: MutableLiveData<String> = MutableLiveData()

    fun setQuery(query: String) {
        this.query.value = query
    }
}