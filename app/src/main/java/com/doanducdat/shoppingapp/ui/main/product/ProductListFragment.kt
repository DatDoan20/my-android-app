package com.doanducdat.shoppingapp.ui.main.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.ProductPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentProductListBinding
import com.doanducdat.shoppingapp.model.category.Category
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.handler.HandlerSwitch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : BaseFragment<FragmentProductListBinding>() {

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

        setUpSearchView()

        setUpRcvListProduct()
        setUpRefreshLayout()
        listenStateLoadProduct()

        //get category form another fragment to search in here
        getDataFromAnotherFragment()
    }

    private fun setUpBackFragment() {
        binding.myAppBarLayout.imgBack.visibility = View.VISIBLE
        binding.myAppBarLayout.imgBack.setOnClickListener {
            controllerMain.popBackStack()
        }
    }

    private fun setUpMyCart() {
        binding.myAppBarLayout.imgMyCard.visibility = View.VISIBLE
        //set click...
    }

    private fun setUpSearchView() {
        val callbackOnSearch: () -> Unit = {
            HandlerSwitch.navigationToFragment(R.id.searchFragment, controllerMain)
        }
        setOnSearchView(binding.myAppBarLayout.searchView, callbackOnSearch)
    }

    private fun setUpRcvListProduct() {
        binding.rcvListProductByCategory.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

        binding.rcvListProductByCategory.setHasFixedSize(false)
        binding.rcvListProductByCategory.adapter = productAdapter
        productAdapter.mySetOnClickProduct {
            controllerMain.navigate(R.id.productFragment, bundleOf("PRODUCT" to it))
        }
    }

    private fun setUpRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
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

    private fun getDataFromAnotherFragment() {
        val bundle = arguments
        val str = AppConstants.ActionClick
        if (bundle != null) {
            when (bundle.getString(str.NAME_EVENT, "")) {
                str.SEE_PRODUCT_BY_CATEGORY -> {
                    categorySearch =
                        bundle.getSerializable(str.SEE_PRODUCT_BY_CATEGORY) as Category
                    binding.txtTitleNameListProduct.text = categorySearch.name
                    loadProductByCategory()
                }
                str.SEE_ALL_NEW_PRODUCT -> {
                    loadNewProduct()
                    binding.txtTitleNameListProduct.text = "Sản phẩm mới"
                }
                str.SEE_ALL_SALE_PRODUCT -> {
                    loadSaleProduct()
                    binding.txtTitleNameListProduct.text = "Sản phẩm giảm giá"
                }
                str.SEE_PRODUCT_BY_SEARCH -> {
                    val nameProduct = bundle.getString(str.SEE_PRODUCT_BY_SEARCH, null)
                    loadProductBySearch(nameProduct)
                    binding.txtTitleNameListProduct.text = nameProduct ?: "Có lỗi xảy ra..."
                }
            }
        }
    }

    private fun loadProductByCategory() {
        if (productAdapter.itemCount != 0) return
        lifecycleScope.launch {
            viewModel.getProductPaging(categorySearch.category, categorySearch.type, null, null)
                .collectLatest {
                    productAdapter.submitData(it)
                }
        }
    }

    private fun loadNewProduct() {
        if (productAdapter.itemCount != 0) return
        lifecycleScope.launch {
            viewModel.getProductPaging(null, null, null, null)
                .collectLatest {
                    productAdapter.submitData(it)
                }
        }
    }

    private fun loadSaleProduct() {
        if (productAdapter.itemCount != 0) return
        lifecycleScope.launch {
            viewModel.getProductPaging(null, null, 0, null)
                .collectLatest {
                    productAdapter.submitData(it)
                }
        }
    }

    private fun loadProductBySearch(name: String?) {
        if (productAdapter.itemCount != 0) return
        lifecycleScope.launch {
            viewModel.getProductPaging(null, null, null, name)
                .collectLatest {
                    productAdapter.submitData(it)
                }
        }
    }
}