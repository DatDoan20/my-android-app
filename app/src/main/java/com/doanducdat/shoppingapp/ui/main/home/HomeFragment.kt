package com.doanducdat.shoppingapp.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CategoryBasicAdapter
import com.doanducdat.shoppingapp.adapter.ProductBasicAdapter
import com.doanducdat.shoppingapp.adapter.SlideImageIntroAdapter
import com.doanducdat.shoppingapp.databinding.FragmentHomeBinding
import com.doanducdat.shoppingapp.model.SlideImage
import com.doanducdat.shoppingapp.model.category.Category
import com.doanducdat.shoppingapp.model.category.CategoryListFactory
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.room.entity.toListProductCacheEntity
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), MyActionApp {

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }

    private val viewModel: HomeViewModel by viewModels()

    private val newProductAdapter by lazy { ProductBasicAdapter() }
    private val saleProductAdapter by lazy { ProductBasicAdapter() }
    private val slideImageIntroAdapter: SlideImageIntroAdapter by lazy { SlideImageIntroAdapter() }
    private val hotCategoryAdapter by lazy { CategoryBasicAdapter() }

    private val callbackOnPageChange = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            switchSlide(position)
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenUpdateBadgeCountNotify(binding.myAppBarLayout.layoutNotification.imgRedDot)
        //hide underline of search view
        hideSearchPlate(binding.myAppBarLayout.searchView)

        // when collapsing -> disable refresh layout, expand fully -> enable refresh layout
        subsCribCollapsingListen()

        setUpMyInfo()

        setUpSlideImageIntro()

        //New product
        setUpRecycleViewNewProduct()
        subscribeLoadNewProduct()
        // ==0 is : fragment not in backstack, open fragment in backstack not load again data
        if (newProductAdapter.itemCount == 0) {
            loadIntroNewProduct()
        }

        //Sale product
        setUpRecycleViewSaleProduct()
        subscribeLoadSaleProduct()
        if (saleProductAdapter.itemCount == 0) {
            loadIntroSaleProducts()
        }

        //Hot Category
        setUpRecyclerviewHotCategory()
        if (hotCategoryAdapter.itemCount == 0) {
            loadHotCategory()
        }

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

    private fun setUpMyInfo() {
        binding.myAppBarLayout.imgAvatar.load(InfoLocalUser.currentUser?.getUrlAvatar())
    }

    //region Slide Image Intro
    private fun setUpSlideImageIntro() {
        if (slideImageIntroAdapter.itemCount == 0) {
            loadSlideImageIntro()
        }
        setUpViewPager()
        setUpIndicator()
        setUpAutoSwitchSlide()
    }

    private fun loadSlideImageIntro() {
        slideImageIntroAdapter.addImage(SlideImage(AppConstants.LinkImg.SALE))
        slideImageIntroAdapter.addImage(SlideImage(AppConstants.LinkImg.SPRING))
        slideImageIntroAdapter.addImage(SlideImage(AppConstants.LinkImg.SUMMER))
        slideImageIntroAdapter.addImage(SlideImage(AppConstants.LinkImg.AUTUMN))
    }

    private fun setUpViewPager() {

        binding.viewPagerIntroTop.adapter = slideImageIntroAdapter
        binding.viewPagerIntroTop.offscreenPageLimit = 5

    }

    private fun setUpIndicator() {
        binding.indicateIntroTop.setViewPager(binding.viewPagerIntroTop)
    }

    private fun setUpAutoSwitchSlide() {
        binding.viewPagerIntroTop.registerOnPageChangeCallback(callbackOnPageChange)
    }

    override fun onPause() {
        super.onPause()
        binding.viewPagerIntroTop.unregisterOnPageChangeCallback(callbackOnPageChange)

    }

    fun switchSlide(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
            with(binding.viewPagerIntroTop) {
                if (position == adapter!!.itemCount - 1) {
                    currentItem = 0
                } else {
                    currentItem++
                }
            }
        }
    }
    //endregion

    //region New Product
    private fun setUpRecycleViewNewProduct() {
        binding.rcvNewProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvNewProduct.setHasFixedSize(true)
        binding.rcvNewProduct.isNestedScrollingEnabled = false
        binding.rcvNewProduct.adapter = newProductAdapter
        newProductAdapter.mySetOnClick {
            controller.navigate(R.id.productFragment, bundleOf("PRODUCT" to it))
        }
    }

    private fun subscribeLoadNewProduct() {
        viewModel.dataStateNewProducts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                Status.ERROR -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    showLongToast(it.message!!)
                    Log.e("TAG", "subscribeLoadNewProduct: ${it.message}")
                }
                Status.SUCCESS -> {
                    //set to adapter for rendering
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
        saleProductAdapter.mySetOnClick {
            controller.navigate(R.id.productFragment, bundleOf("PRODUCT" to it))
        }
    }

    private fun subscribeLoadSaleProduct() {
        viewModel.dataStateSaleProducts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                Status.ERROR -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    showLongToast(it.message!!)
                    Log.e("TAG", "subscribeLoadSaleProduct: ${it.message}")
                }
                Status.SUCCESS -> {
                    saleProductAdapter.setProducts(it.response!!.data)
                    binding.swipeRefreshLayout.isRefreshing = false

                    //add product room
                    //note here res is success from get data in cache -> call here (nologic)
                    val parentJob = viewModel.insertAllProduct(it.response.data)
                    parentJob.invokeOnCompletion {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
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
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        binding.rcvHotCategory.setHasFixedSize(true)
        binding.rcvHotCategory.isNestedScrollingEnabled = false
        binding.rcvHotCategory.adapter = hotCategoryAdapter

        //set event click recyclerview
        hotCategoryAdapter.mySetOnClickCategoryAdapter {
            val bundleCategory = bundleOf("CATEGORY" to it)
            controller.navigate(R.id.productListFragment, bundleCategory)
        }
    }
    //endregion

    private fun setUpSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadIntroNewProduct()
            loadIntroSaleProducts()
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
        //
        binding.myAppBarLayout.layoutNotification.imgNotification.setOnClickListener {
            controller.navigate(R.id.notificationFragment)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.NAV_CATEGORY -> {
                controller.navigate(R.id.categoryFragment)
            }
        }
    }

}
