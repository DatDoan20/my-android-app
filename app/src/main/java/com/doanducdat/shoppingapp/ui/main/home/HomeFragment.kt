package com.doanducdat.shoppingapp.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CategoryBasicAdapter
import com.doanducdat.shoppingapp.adapter.ProductBasicAdapter
import com.doanducdat.shoppingapp.adapter.SlideImageIntroAdapter
import com.doanducdat.shoppingapp.databinding.FragmentHomeBinding
import com.doanducdat.shoppingapp.module.SlideImage
import com.doanducdat.shoppingapp.module.category.Category
import com.doanducdat.shoppingapp.module.category.CategoryListFactory
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.response.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), MyActionApp {

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }

    private val viewModel: HomeViewModel by viewModels()

    private val newProductAdapter by lazy { ProductBasicAdapter() }
    private val saleProductAdapter by lazy { ProductBasicAdapter() }
    private val hotCategoryAdapter by lazy { CategoryBasicAdapter() }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide underline of search view
        hideSearchPlate(binding.myAppBarLayout.searchView)

        // when collapsing -> disable refresh layout, expand fully -> enable refresh layout
        subsCribCollapsingListen()

        setUpSlideImageIntro()

        //New product
        setUpRecycleViewNewProduct()
        subscribeLoadNewProduct()
        loadIntroNewProduct()

        //Sale product
        setUpRecycleViewSaleProduct()
        subscribeLoadSaleProduct()
        loadIntroSaleProducts()

        //Hot Category
        setUpRecyclerviewHotCategory()
        loadHotCategory()

        //when refresh layout -> load all data relate
        setUpSwipeRefreshLayout()

        //Event click
        setUpActionClick()
    }

    private fun subsCribCollapsingListen() {
        binding.myAppBarLayout.appBarLayout.addOnOffsetChangedListener(
            collapsingListen(binding.myAppBarLayout.searchView, binding.swipeRefreshLayout)
        )
    }

    //region Slide Image Intro
    private fun setUpSlideImageIntro() {
        setUpViewPager()
        setUpIndicator()
    }

    private fun setUpViewPager() {
        val adapter: SlideImageIntroAdapter = SlideImageIntroAdapter()
        adapter.addImage(SlideImage(AppConstants.LinkImg.SALE))
        adapter.addImage(SlideImage(AppConstants.LinkImg.SPRING))
        adapter.addImage(SlideImage(AppConstants.LinkImg.SUMMER))
        adapter.addImage(SlideImage(AppConstants.LinkImg.AUTUMN))
        binding.viewPagerIntroTop.adapter = adapter
        binding.viewPagerIntroTop.offscreenPageLimit = 5
    }

    private fun setUpIndicator() {
        binding.indicateIntroTop.setViewPager(binding.viewPagerIntroTop)
    }
    //endregion

    //region New Product
    private fun setUpRecycleViewNewProduct() {
//        binding.rcvNewProduct.layoutManager =
//            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
//
//        binding.rcvNewProduct.setHasFixedSize(false)
//        binding.rcvNewProduct.adapter = newProductAdapter.withLoadStateHeaderAndFooter(
//            header = LoadStatePagingAdapter { newProductAdapter::retry },
//            footer = LoadStatePagingAdapter { newProductAdapter::retry }
//        )
        binding.rcvNewProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvNewProduct.setHasFixedSize(true)
        binding.rcvNewProduct.isNestedScrollingEnabled = false
        binding.rcvNewProduct.adapter = newProductAdapter
    }

    //    private var jobGetNewProducts: Job? = null
    private fun subscribeLoadNewProduct() {
//        binding.swipeRefreshLayout.isRefreshing = true
//        jobGetNewProducts?.cancel()
//        jobGetNewProducts = lifecycleScope.launch {
//            viewModel.getNewProducts().collectLatest {
//                newProductAdapter.submitData(it)
//                binding.swipeRefreshLayout.isRefreshing = false
//            }
//        }
        viewModel.dataStateNewProducts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                Status.ERROR -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    showToast(it.message!!)
                    Log.e("TAG", "subscribeLoadNewProduct: ${it.message}")
                }
                Status.SUCCESS -> {
                    newProductAdapter.setProducts(it.response!!.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    private fun loadIntroNewProduct() {
        viewModel.getIntroNewProducts()
    }
    //endregion

    //region Sale Product
    private fun setUpRecycleViewSaleProduct() {
        binding.rcvSaleProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvSaleProduct.setHasFixedSize(true)
        binding.rcvSaleProduct.isNestedScrollingEnabled = false
        binding.rcvSaleProduct.adapter = saleProductAdapter
    }

    private fun subscribeLoadSaleProduct() {
        viewModel.dataStateSaleProducts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                Status.ERROR -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    showToast(it.message!!)
                    Log.e("TAG", "subscribeLoadSaleProduct: ${it.message}")
                }
                Status.SUCCESS -> {
                    saleProductAdapter.setProducts(it.response!!.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    private fun loadIntroSaleProducts() {
        viewModel.getIntroSaleProducts()
    }
    //endregion

    //region Hot Categories
    private fun loadHotCategory() {
        val hotCategory: MutableList<Category> = CategoryListFactory.getInstance().hotCategory()
        hotCategoryAdapter.setCategoryList(hotCategory)
    }

    private fun setUpRecyclerviewHotCategory() {
        binding.rcvHotCategory.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
        binding.rcvHotCategory.setHasFixedSize(true)
        binding.rcvHotCategory.isNestedScrollingEnabled = false
        binding.rcvHotCategory.adapter = hotCategoryAdapter
    }
    //endregion

    private fun setUpSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            subscribeLoadNewProduct()
        }
    }

    private fun setUpActionClick() {
        //open all category
        binding.txtSeeAllHotCategory.setOnClickListener {
            doActionClick(AppConstants.ActionClick.NAV_CATEGORY)
        }
        //click searchView
        val callbackOnSearch: () -> Unit = {
            controller.navigate(R.id.searchFragment)
        }
        setOnSearchView(binding.myAppBarLayout.searchView, callbackOnSearch)
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.NAV_CATEGORY -> {
                controller.navigate(R.id.categoryFragment)
            }
        }
    }

}
