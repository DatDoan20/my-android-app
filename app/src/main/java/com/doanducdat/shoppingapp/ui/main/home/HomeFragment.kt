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
import androidx.recyclerview.widget.RecyclerView
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
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.handler.HandlerErrRes
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
        listenLoadingForm()
        //listen value count notify from shareViewModel
        listenUpdateBadgeCountNotify(binding.myAppBarLayout.layoutNotification.imgRedDot)

        //hide underline of search view
        hideSearchPlate(binding.myAppBarLayout.searchView)

        // when collapsing -> disable refresh layout, expand fully -> enable refresh layout
        listenCollapsing()

        setUpMyInfo()
        setUpSlideImageIntro()

        //New product
        setUpRCVNewProduct()
        listenLoadNewProduct()
        listenFreshNewProduct()
        // ==0 is : fragment not in backstack, open fragment in backstack not load again data
        if (newProductAdapter.itemCount == 0) loadNewProduct()

        //Sale product
        setUpRCVSaleProduct()
        listenLoadSaleProduct()
        listenFreshSaleProduct()
        if (saleProductAdapter.itemCount == 0) loadSaleProduct()

        //Hot Category
        setUpRecyclerviewHotCategory()
        if (hotCategoryAdapter.itemCount == 0) loadHotCategory()

        //when refresh layout -> load all data relate
        setUpSwipeRefreshLayout()

        //Event click
        setUpActionClick()
    }

    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            binding.swipeRefreshLayout.isRefreshing = it
        })
    }

    private fun listenCollapsing() {
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

    private fun setUpRcvGeneric(rcv: RecyclerView, adapter: ProductBasicAdapter) {
        rcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        rcv.isNestedScrollingEnabled = false
        rcv.adapter = adapter
    }

    //region New Product
    private fun setUpRCVNewProduct() {
        setUpRcvGeneric(binding.rcvNewProduct, newProductAdapter)
    }

    private fun listenLoadNewProduct() {
        viewModel.dataStateNewProducts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    viewModel.isLoading.value = false
                    showLongToast(HandlerErrRes.checkMsg(it.response?.message))
                    Log.e("TAG", "subscribeLoadNewProduct: ${it.message}")
                }
                Status.SUCCESS -> {
                    //set to adapter for rendering
                    newProductAdapter.setProducts(it.response!!.data)
                    viewModel.isLoading.value = false
                }
            }
        })
    }

    private fun loadNewProduct() {
        viewModel.getNewProducts()
    }

    private fun listenFreshNewProduct() {
        viewModel.freshNewProducts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    viewModel.isLoading.value = false
                    showLongToast(HandlerErrRes.checkMsg(it.response?.message))
                    Log.e("TAG", "listenFreshNewProduct: ${it.message}")
                }
                Status.SUCCESS -> {
                    newProductAdapter.setProducts(it.response!!.data)
                    viewModel.isLoading.value = false
                }
            }
        })
    }
    //endregion

    //region Sale Product
    private fun setUpRCVSaleProduct() {
        setUpRcvGeneric(binding.rcvSaleProduct, saleProductAdapter)
    }

    private fun listenLoadSaleProduct() {
        viewModel.dataStateSaleProducts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    viewModel.isLoading.value = false
                    showLongToast(HandlerErrRes.checkMsg(it.response?.message))
                    Log.e("TAG", "subscribeLoadSaleProduct: ${it.message}")
                }
                Status.SUCCESS -> {
                    saleProductAdapter.setProducts(it.response!!.data)
                    viewModel.isLoading.value = false
                }
            }
        })
    }

    private fun loadSaleProduct() {
        viewModel.getSaleProduct()
    }

    private fun listenFreshSaleProduct() {
        viewModel.freshSaleProducts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    viewModel.isLoading.value = false
                    showLongToast(HandlerErrRes.checkMsg(it.response?.message))
                    Log.e("TAG", "listenFreshSaleProduct: ${it.message}")
                }
                Status.SUCCESS -> {
                    saleProductAdapter.setProducts(it.response!!.data)
                    viewModel.isLoading.value = false
                }
            }
        })
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
    }
    //endregion

    private fun setUpSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.freshNewProduct()
            viewModel.freshSaleProduct()
        }
    }

    private fun setUpActionClick() {
        //open all category
        binding.txtSeeAllHotCategory.setOnClickListener {
            doActionClick(AppConstants.ActionClick.NAV_CATEGORY)
        }

        //set event click recyclerview
        hotCategoryAdapter.mySetOnClickCategoryAdapter { nameCategory ->
            val bundleCategory = bundleOf("CATEGORY" to nameCategory)
            controller.navigate(R.id.productListFragment, bundleCategory)
        }

        //click searchView
        val callbackOnSearch: () -> Unit = {
            controller.navigate(R.id.searchFragment)
        }
        setOnSearchView(binding.myAppBarLayout.searchView, callbackOnSearch)

        //click notify icon
        binding.myAppBarLayout.layoutNotification.imgNotification.setOnClickListener {
            controller.navigate(R.id.notificationFragment)
        }

        newProductAdapter.mySetOnClick { product ->
            controller.navigate(R.id.productFragment, bundleOf("PRODUCT" to product))
        }

        saleProductAdapter.mySetOnClick { product ->
            controller.navigate(R.id.productFragment, bundleOf("PRODUCT" to product))
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
