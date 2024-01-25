package com.habidev.bookdb.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    val groupLiveData: MutableLiveData<BookGroupItem> = MutableLiveData()

    val allBooksLiveData: LiveData<List<BookItem>> = repository.allBooksFlow.asLiveData()

    val allGroupsLiveData: LiveData<List<BookGroupItem>> = repository.allGroupsFlow.asLiveData()

    fun setGroup(groupItem: BookGroupItem) {
        groupLiveData.value = groupItem
    }

    suspend fun insertBook(bookItem: BookItem): Boolean = repository.insertBook(bookItem)

    fun insertGroup(groupItem: BookGroupItem) = viewModelScope.launch {
        repository.insertGroup(groupItem)
    }

    fun updateBook(bookItem: BookItem) = viewModelScope.launch {
        repository.updateBook(bookItem)
    }

    fun updateGroup(groupItem: BookGroupItem) = viewModelScope.launch {
        repository.updateGroup(groupItem)
    }

    fun deleteBook(bookItem: BookItem) = viewModelScope.launch {
        repository.deleteBook(bookItem)
    }

    fun deleteGroup(groupItem: BookGroupItem) = viewModelScope.launch {
        repository.deleteGroup(groupItem)
    }

    fun searchBook(query: String): List<BookItem> = repository.searchBook(query)

    fun getBooksByGroup(groupItem: BookGroupItem): List<BookItem> = repository.getBooksByGroup(groupItem)
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