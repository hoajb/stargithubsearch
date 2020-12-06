package vn.hoanguyen.stargithubsearch.extension

import android.annotation.SuppressLint
import android.view.View
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