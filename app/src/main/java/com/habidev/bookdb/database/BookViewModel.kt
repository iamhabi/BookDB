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

    var searchQuery: MutableLiveData<String> = MutableLiveData()

    fun search(query: String): LiveData<List<BookItem>> {
        val resultList = MutableLiveData<List<BookItem>>()

        viewModelScope.launch {
            val searchResult = repository.search(query)

            resultList.postValue(searchResult)
        }

        return resultList
    }
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