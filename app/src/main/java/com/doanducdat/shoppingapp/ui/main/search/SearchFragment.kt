package com.doanducdat.shoppingapp.ui.main.search

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.KeyWordAdapter
import com.doanducdat.shoppingapp.databinding.FragmentSearchBinding
import com.doanducdat.shoppingapp.room.entity.KeyWordCacheEntity
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    val viewModel: SearchViewModel by viewModels()
    private val adapterKeyWord by lazy { KeyWordAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenUpdateBadgeCountNotify(binding.myAppBarLayout.layoutNotification.imgRedDot)

        setUpSearchView()
        setUpRcvKeyWord()
        loadHistoryKeyWord()
        setUpClickSearch()
    }


    private fun setUpSearchView() {
        binding.myAppBarLayout.searchView.requestFocus()
    }

    private fun setUpRcvKeyWord() {
        binding.rcvHistoryKeyWord.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvHistoryKeyWord.setHasFixedSize(true)
        binding.rcvHistoryKeyWord.adapter = adapterKeyWord
    }

    private fun loadHistoryKeyWord() {
        viewModel.getAllKeyWord().observe(viewLifecycleOwner, { historyKeyWord ->
            if (historyKeyWord.isNotEmpty()) {
                Log.e("TEST", "loadHistoryKeyWord: do it")
                adapterKeyWord.setKeyWordList(historyKeyWord)
            }
        })
    }

    private fun setUpClickSearch() {
        binding.myAppBarLayout.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        val querySearch = query.trim()
                        if (!TextUtils.isEmpty(querySearch)) {
                            viewModel.insertKeyWord(KeyWordCacheEntity(querySearch))
                            // navigate product list
                            val str = AppConstants.ActionClick
                            controllerMain.navigate(
                                R.id.productListFragment, bundleOf(
                                    str.NAME_EVENT to str.SEE_PRODUCT_BY_SEARCH,
                                    str.SEE_PRODUCT_BY_SEARCH to querySearch
                                )
                            )
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })
    }

}