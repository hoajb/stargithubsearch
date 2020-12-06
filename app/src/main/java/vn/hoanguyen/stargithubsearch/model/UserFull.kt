package vn.hoanguyen.stargithubsearch.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */

data class UserFull(
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val userName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("site_admin")
    val isAdmin: Boolean,
    @SerializedName("name")
    val name: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("public_repos")
    val public_repos: Int?,
    @SerializedName("public_gists")
    val public_gists: Int?,
    @SerializedName("followers")
    val followers: Int?,
    @SerializedName("following")
    val following: Int?,
)