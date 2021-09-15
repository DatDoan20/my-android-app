package com.doanducdat.shoppingapp.ui.main.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.ProductPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentProductListBinding
import com.doanducdat.shoppingapp.module.category.Category
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : BaseFragment<FragmentProductListBinding>() {

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    private lateinit var categorySearch: Category

    private val productAdapter = ProductPagingAdapter()
    private var jobLoadProducts: Job? = null
    private val viewModel: ProductViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductListBinding = FragmentProductListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBackFragment()
        setUpMyCart()
        //get category form another fragment to search in here
        getDataFromAnotherFragment()

        setUpSearchView()

        setUpRcvListProduct()
        setUpRefreshLayout()
        listenStateLoadProduct()
        if (productAdapter.itemCount == 0) {
            loadProductSearched()
        }
    }

    private fun setUpBackFragment() {
        binding.myAppBarLayout.imgBack.visibility = View.VISIBLE
        binding.myAppBarLayout.imgBack.setOnClickListener {
            controller.popBackStack()
        }
    }

    private fun setUpMyCart() {
        binding.myAppBarLayout.imgMyCard.visibility = View.VISIBLE
        //set click...
    }

    private fun getDataFromAnotherFragment() {
        val bundle = arguments
        if (bundle != null) {
            categorySearch = bundle.getSerializable("CATEGORY") as Category
            binding.txtTitleNameListProduct.text = categorySearch.name
        }
    }

    private fun setUpSearchView() {
        val callbackOnSearch: () -> Unit = {
            controller.navigate(R.id.searchFragment)
        }
        hideSearchPlate(binding.myAppBarLayout.searchView)
        setOnSearchView(binding.myAppBarLayout.searchView, callbackOnSearch)
    }

    private fun setUpRcvListProduct() {
        binding.rcvListProductByCategory.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

        binding.rcvListProductByCategory.setHasFixedSize(false)
        binding.rcvListProductByCategory.adapter = productAdapter
        productAdapter.mySetOnClickProduct {
            controller.navigate(R.id.productFragment, bundleOf("PRODUCT" to it))
        }
    }

    private fun setUpRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadProductSearched()
        }
    }

    private fun listenStateLoadProduct() {
        lifecycleScope.launch {
            productAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                    is LoadState.Error -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    }
                    else -> binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun loadProductSearched() {
        jobLoadProducts = lifecycleScope.launch {
            viewModel.getProductPaging(categorySearch.category, categorySearch.type).collectLatest {
                productAdapter.submitData(it)
            }
        }

    }
}