package vn.hoanguyen.stargithubsearch.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import vn.hoanguyen.stargithubsearch.model.SearchResponse
import vn.hoanguyen.stargithubsearch.model.User

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
interface GithubSearchApi {
    companion object {
        const val API_ENDPOINT = "https://api.github.com"
    }

    @GET("/search/users")
    suspend fun searchUsers(
        @Query("q") term: String,
        @Query("page") pageNumber: Int = 1,
        @Query("per_page") pageSize: Int = PagingSourceSearch.PAGE_SIZE
    ): Response<SearchResponse<User>>
}