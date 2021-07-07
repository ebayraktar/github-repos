package com.bayraktar.githubrepos.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bayraktar.githubrepos.room.Favorite
import com.bayraktar.githubrepos.room.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepoDetailViewModel(
    private val repository: FavoriteRepository
) : ViewModel() {
    fun insert(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(favorite)
        }
    }

    fun delete(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(favorite)
        }
    }
}

class RepoDetailViewModelFactory(
    private val repository: FavoriteRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepoDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepoDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}
