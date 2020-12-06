package vn.hoanguyen.stargithubsearch.service

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
class GithubException(private val mess: String) : RuntimeException() {
    override val message: String get() = mess
}