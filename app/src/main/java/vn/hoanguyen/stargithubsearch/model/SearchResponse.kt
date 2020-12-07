package vn.hoanguyen.stargithubsearch.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
data class SearchResponse<T>(
    @SerializedName("total_count")
    val TotalCount: Int,
    @SerializedName("items")
    val item: List<T>
)