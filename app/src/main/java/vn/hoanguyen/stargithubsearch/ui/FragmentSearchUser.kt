package vn.hoanguyen.stargithubsearch.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import vn.hoanguyen.stargithubsearch.databinding.FragmentSearchUserBinding
import vn.hoanguyen.stargithubsearch.viewmodel.ViewModelGithub

/**
 * Created by Hoa Nguyen on Dec 06 2020.
 */
class FragmentSearchUser : Fragment() {
    private val viewModel by viewModels<ViewModelGithub>()

    private lateinit var binding: FragmentSearchUserBinding
    private lateinit var mainListAdapter: AdapterUser

    companion object {
        fun newInstance() = FragmentSearchUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupView()
    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        val currentSearch = savedInstanceState?.getString("Query", "").orEmpty()
//
//        if (currentSearch.isEmpty().not()) {
//            binding.editQuery.setText(currentSearch)
//        }
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        val currentSearch = binding.editQuery.text.toString()
//        if (currentSearch.isEmpty().not()) {
//            outState.putString("Query", currentSearch)
//        }
//
//        super.onSaveInstanceState(outState)
//    }

    private fun setupView() {
        lifecycleScope.launch {
            @OptIn(ExperimentalCoroutinesApi::class)
            viewModel.listDataSearchUser.collectLatest {
                mainListAdapter.submitData(it)
            }
        }

        binding.editQuery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.search(s.toString())
            }
        })
    }

    private fun setupList() {
        mainListAdapter = AdapterUser() { item, pos ->
            Toast.makeText(requireContext(), item.userName, Toast.LENGTH_SHORT).show()
            item.id.let { id ->
//                ActivityResidentServiceComplaintDetails.instance(requireContext(), id, pos)
//                    .also { intent ->
//                        startActivityForResult(intent, 100)
//                    }
            }
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = mainListAdapter.withLoadStateFooter(
                footer = LoadStateAdapterUser { mainListAdapter.retry() }
            )
        }

        // show the loading state for te first load
        mainListAdapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {
                // Show ProgressBar
                binding.progressBar.visibility = View.VISIBLE
                binding.textStatus.isVisible = false
            } else {
                // Hide ProgressBar
                binding.progressBar.visibility = View.GONE

                //Handle empty
                binding.textStatus.isVisible =
                    loadState.append.endOfPaginationReached && mainListAdapter.itemCount < 1

                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}