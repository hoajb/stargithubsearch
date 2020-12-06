package vn.hoanguyen.stargithubsearch.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val userName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("site_admin")
    val isAdmin: Boolean
)