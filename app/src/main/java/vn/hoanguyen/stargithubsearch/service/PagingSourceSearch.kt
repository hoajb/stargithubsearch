package vn.hoanguyen.stargithubsearch.service

import androidx.paging.PagingSource
import vn.hoanguyen.stargithubsearch.model.User
import java.io.IOException

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */

class PagingSourceSearch(private val api: GithubSearchService, private val searchInput: String = "") :
    PagingSource<Int, User>() {

    companion object{
        const val PAGE_SIZE = 50
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {

        return try {
            val nextPage = params.key ?: 1
            val response = api.getSearchUserList(term = searchInput, pageNumber = nextPage)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                LoadResult.Page(
                    data = body.item,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (nextPage * PAGE_SIZE < body.TotalCount)
                        nextPage.plus(1)
                    else null
                )
            } else {
                LoadResult.Error(IOException())
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}