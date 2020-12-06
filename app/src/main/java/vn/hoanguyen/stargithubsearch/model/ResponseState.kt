package vn.hoanguyen.stargithubsearch.model

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
sealed class ResponseState<out T : Any> {
    object Loading : ResponseState<Nothing>()
    data class Error(val message: String) : ResponseState<Nothing>()
    data class Loaded<out T : Any>(val data: T) : ResponseState<T>()
    object Empty : ResponseState<Nothing>()
}