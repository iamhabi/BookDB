package com.habidev.bookdb.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository): ViewModel() {
    /**
     * Do Nothing
     * Create ViewModel
     */
    fun create() {}

    val allBooksLiveData: LiveData<List<BookItem>> = repository.allBooksFlow.asLiveData()

    fun insertBook(bookItem: BookItem) = viewModelScope.launch {
        repository.insertBook(bookItem)
    }

    fun updateBook(bookItem: BookItem) = viewModelScope.launch {
        repository.updateBook(bookItem)
    }

    fun deleteBook(bookItem: BookItem) = viewModelScope.launch {
        repository.deleteBook(bookItem)
    }

    fun searchBook(query: String): List<BookItem> = repository.searchBook(query)
}

class BookViewModelFactory(private val repository: BookRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}