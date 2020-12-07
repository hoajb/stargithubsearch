package vn.hoanguyen.stargithubsearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import vn.hoanguyen.stargithubsearch.R
import vn.hoanguyen.stargithubsearch.databinding.FragmentUserDetailsBinding
import vn.hoanguyen.stargithubsearch.model.ResponseState
import vn.hoanguyen.stargithubsearch.model.UserFull
import vn.hoanguyen.stargithubsearch.viewmodel.ViewModelGithub

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
class FragmentUserDetails : Fragment() {
    private val viewModel by viewModels<ViewModelGithub>()

    private lateinit var binding: FragmentUserDetailsBinding

    companion object {
        const val ARGS_USERNAME = "ARGS_USERNAME"
        fun newInstance(userName: String) = FragmentUserDetails().apply {
            arguments = Bundle().apply {
                putString(ARGS_USERNAME, userName)
            }
        }
    }

    lateinit var userName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userName = arguments?.getString(ARGS_USERNAME, "").orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.loadDetails(userName)
    }

    private fun setupView() {
        viewModel.liveDataUserFull.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResponseState.Loading -> {
                    binding.progressBar.isVisible = true

                    binding.avatar.isVisible = false
                    binding.userName.isVisible = false
                    binding.userId.isVisible = false
                    binding.location.isVisible = false
                    binding.publicRepos.isVisible = false
                    binding.publicGists.isVisible = false
                    binding.followers.isVisible = false
                    binding.following.isVisible = false
                }

                is ResponseState.Loaded -> {
                    binding.progressBar.isVisible = false
                    bindingView(it.data)
                }

                is ResponseState.Error -> {
                    binding.progressBar.isVisible = false

                    binding.userName.isVisible = true
                    binding.userName.text = "Error Load Data"
                    Toast.makeText(requireContext(), "Error Load Data", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun bindingView(user: UserFull) {
        user.apply {
            binding.avatar.isVisible = true
            binding.userName.isVisible = true
            binding.userId.isVisible = true
            binding.location.isVisible = true
            binding.publicRepos.isVisible = true
            binding.publicGists.isVisible = true
            binding.followers.isVisible = true
            binding.following.isVisible = true

            Glide.with(binding.avatar)
                .load(avatarUrl)
                .error(R.drawable.github)
                .into(binding.avatar)

            binding.userName.text = "Name: ${userName}"
            binding.userId.text = "Id: ${id}"
            binding.location.text = "Location: ${location}"
            binding.publicRepos.text = "Public Repos: ${public_repos}"
            binding.publicGists.text = "Public Gists: ${public_gists}"
            binding.followers.text = "Followers: ${followers}"
            binding.following.text = "Following: ${following}"


        }

    }
}