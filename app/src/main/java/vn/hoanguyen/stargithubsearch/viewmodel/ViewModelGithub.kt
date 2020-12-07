package vn.hoanguyen.stargithubsearch.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import vn.hoanguyen.stargithubsearch.model.ResponseState
import vn.hoanguyen.stargithubsearch.model.UserFull
import vn.hoanguyen.stargithubsearch.service.GithubSearchService
import vn.hoanguyen.stargithubsearch.service.GithubSearchServiceImpl
import vn.hoanguyen.stargithubsearch.service.PagingSourceSearch

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
class ViewModelGithub(application: Application) : AndroidViewModel(application) {
    private val api: GithubSearchService = GithubSearchServiceImpl()
    private val searchInput = MutableLiveData<String>()

    private val _liveDataUserFull = MutableLiveData<ResponseState<UserFull>>()
     val liveDataUserFull: LiveData<ResponseState<UserFull>>
        get() = _liveDataUserFull

    @FlowPreview
    @ExperimentalCoroutinesApi
    val listDataSearchUser = searchInput.asFlow()
        .debounce(300)
        .filter {
            it.trim().isEmpty().not()
        }
        .distinctUntilChanged()
        .flatMapLatest {
            Pager(
                PagingConfig(
                    pageSize = PagingSourceSearch.PAGE_SIZE,
                    enablePlaceholders = false,
                    initialLoadSize = 2
                )
            ) {
                PagingSourceSearch(api, searchInput = searchInput.value!!)
            }.flow.cachedIn(viewModelScope)
        }

    fun search(input: String) {
        searchInput.value = input
    }

    fun loadDetails(userName: String) {
        viewModelScope.launch {
            try {
                _liveDataUserFull.value = ResponseState.Loading

                val response = api.getUsers(userName)
                if (response.isSuccessful && response.body() != null) {
                    _liveDataUserFull.value = ResponseState.Loaded(response.body()!!)
                } else {
                    _liveDataUserFull.value = ResponseState.Error("")
                }
            } catch (ex: Exception) {
                _liveDataUserFull.value = ResponseState.Error(ex.localizedMessage)
                Timber.e(ex.localizedMessage)
            }
        }
    }
}