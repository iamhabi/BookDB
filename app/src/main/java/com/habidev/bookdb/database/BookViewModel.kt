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
    val allGroupsLiveData: LiveData<List<GroupItem>> = repository.allGroupsFlow.asLiveData()
    fun booksByGroupLiveData(group: String): LiveData<List<GroupBookItem>> = repository.booksByGroupFlow(group).asLiveData()

    suspend fun insertBook(bookItem: BookItem): Boolean = repository.insertBook(bookItem)

    fun insertBookIntoGroup(bookItem: BookItem, group: String) = viewModelScope.launch {
        repository.insertBookIntoGroup(bookItem.isbn, group)
    }

    fun insertGroup(groupItem: GroupItem) = viewModelScope.launch {
        repository.insertGroup(groupItem)
    }

    fun updateBook(bookItem: BookItem) = viewModelScope.launch {
        repository.updateBook(bookItem)
    }

    fun updateGroup(groupItem: GroupItem) = viewModelScope.launch {
        repository.updateGroup(groupItem)
    }

    fun deleteBook(bookItem: BookItem) = viewModelScope.launch {
        repository.deleteBook(bookItem)
    }

    fun deleteBookFromGroup(groupBookItem: GroupBookItem) = viewModelScope.launch {
        repository.deleteBookFromGroup(groupBookItem)
    }

    fun deleteGroup(groupItem: GroupItem) = viewModelScope.launch {
        repository.deleteGroup(groupItem)
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