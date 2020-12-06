package vn.hoanguyen.stargithubsearch.ui

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.hoanguyen.stargithubsearch.R
import vn.hoanguyen.stargithubsearch.databinding.ItemUserBinding
import vn.hoanguyen.stargithubsearch.extension.safeClick
import vn.hoanguyen.stargithubsearch.model.User

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
class AdapterUser(private val onItemClick: (User, Int) -> Unit) :
    PagingDataAdapter<User, AdapterUser.ViewHolderUser>(UserComparator) {
    override fun onBindViewHolder(holder: ViewHolderUser, position: Int) {
        val item = getItem(position)!!
        holder.binding.userName.text = "Name: ${item.userName}"
        holder.binding.userId.text = "Id: ${item.id}"

        if (TextUtils.isEmpty(item.avatarUrl)) {
            holder.binding.avatar.setImageResource(R.drawable.github)
        } else {
            Glide.with(holder.binding.avatar)
                .load(item.avatarUrl)
                .error(R.drawable.github)
                .into(holder.binding.avatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUser {
        val viewHolder = ViewHolderUser(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
        viewHolder.binding.root.safeClick {
            getItem(viewHolder.layoutPosition)?.let { user ->
                onItemClick.invoke(user, viewHolder.layoutPosition)
            }

        }
        return viewHolder
    }

    class ViewHolderUser(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemUserBinding.bind(view)
    }

    companion object {
        private val UserComparator = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                (oldItem.id == newItem.id)

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }

}