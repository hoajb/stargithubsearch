package vn.hoanguyen.stargithubsearch.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
@SuppressLint("CheckResult")
fun View.safeClick(time: Long = 500L, invoke: (view: View) -> Unit) {
    RxView.clicks(this)
        .throttleFirst(time, TimeUnit.MILLISECONDS)
        .subscribe { invoke(this) }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}