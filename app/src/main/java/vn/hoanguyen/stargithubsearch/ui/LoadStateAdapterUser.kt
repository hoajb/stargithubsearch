package vn.hoanguyen.stargithubsearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_load_state.view.*
import vn.hoanguyen.stargithubsearch.R
import vn.hoanguyen.stargithubsearch.extension.safeClick

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
class LoadStateAdapterUser(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateAdapterUser.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

        val progress = holder.itemView.load_state_progress
        val btnRetry = holder.itemView.load_state_retry
        val txtErrorMessage = holder.itemView.load_state_errorMessage

        if (loadState is LoadState.Error) {
            txtErrorMessage.text = loadState.error.localizedMessage
        }

        progress.isVisible = loadState is LoadState.Loading
        btnRetry.isVisible = loadState is LoadState.Error
        txtErrorMessage.isVisible = loadState is LoadState.Error

        btnRetry.safeClick {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state, parent, false)
        )
    }

    class LoadStateViewHolder(private val view: View) : RecyclerView.ViewHolder(view)
}