package vn.hoanguyen.stargithubsearch.service

import androidx.paging.PagingSource
import org.json.JSONObject
import retrofit2.HttpException
import vn.hoanguyen.stargithubsearch.model.User

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */

class PagingSourceSearch(
    private val api: GithubSearchService,
    private val searchInput: String = ""
) :
    PagingSource<Int, User>() {

    companion object {
        const val PAGE_SIZE = 50
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {

        if (searchInput.isEmpty()) {
            return LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        }

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
                if (response.code() == 403) {
                    val jObjError = JSONObject(response.errorBody()?.string().orEmpty())
                    LoadResult.Error(
                        GithubException(mess = jObjError.getString("message"))
                    )
                } else {
                    LoadResult.Error(HttpException(response))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}