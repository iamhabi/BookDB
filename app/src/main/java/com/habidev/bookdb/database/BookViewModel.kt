package com.habidev.bookdb.database

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository): ViewModel() {
    /**
     * Do Nothing
     * Create ViewModel
     */
    fun create() {}

    val allBooks: LiveData<List<BookItem>> = repository.allBooks.asLiveData()

    fun insert(book: BookItem) = viewModelScope.launch {
        repository.insert(book)
    }

    fun update(book: BookItem) = viewModelScope.launch {
        repository.update(book)
    }

    fun delete(bookItem: BookItem) = viewModelScope.launch {
        repository.delete(bookItem)
    }

    fun search(query: String): List<BookItem> = repository.search(query)
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