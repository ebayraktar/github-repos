package com.bayraktar.githubrepos.ui.home

import androidx.lifecycle.*
import com.bayraktar.githubrepos.model.Repo
import com.bayraktar.githubrepos.network.GitHubRepository
import com.bayraktar.githubrepos.room.Favorite
import com.bayraktar.githubrepos.room.FavoriteRepository
import com.bayraktar.githubrepos.utils.common.subscribeOnBackground
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: FavoriteRepository,
    private val gitHubRepository: GitHubRepository
) : ViewModel() {

    fun getRepos(user: String): Observable<List<Repo>> {
        return gitHubRepository.getRepos(user)
    }

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

    val allFavorites: LiveData<List<Favorite>> = repository.allFavorites.asLiveData()

}

class HomeViewModelFactory(
    private val repository: FavoriteRepository,
    private val gitHubRepository: GitHubRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository, gitHubRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}
