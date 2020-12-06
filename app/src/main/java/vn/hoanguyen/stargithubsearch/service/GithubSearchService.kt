package vn.hoanguyen.stargithubsearch.service

import retrofit2.Response
import retrofit2.Retrofit
import vn.hoanguyen.stargithubsearch.model.SearchResponse
import vn.hoanguyen.stargithubsearch.model.User

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
interface GithubSearchService {
    suspend fun getSearchUserList(term: String, pageNumber: Int): Response<SearchResponse<User>>
}

class GithubSearchServiceImpl : BaseNetworkAPI<GithubSearchApi>(GithubSearchApi.API_ENDPOINT),
    GithubSearchService {
    override fun createNetworkAPI(retrofit: Retrofit): GithubSearchApi =
        retrofit.create(GithubSearchApi::class.java)

    // preparing local cache here if we need
    override suspend fun getSearchUserList(term: String, pageNumber: Int): Response<SearchResponse<User>> =
        api().searchUsers(term, pageNumber)

}