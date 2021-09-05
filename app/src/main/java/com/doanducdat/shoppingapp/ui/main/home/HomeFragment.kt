package com.doanducdat.shoppingapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CategoryBasicAdapter
import com.doanducdat.shoppingapp.adapter.ProductAdapterBasic
import com.doanducdat.shoppingapp.adapter.SlideImageIntroAdapter
import com.doanducdat.shoppingapp.databinding.FragmentHomeBinding
import com.doanducdat.shoppingapp.module.SlideImage
import com.doanducdat.shoppingapp.module.category.Category
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.response.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }

    private val viewModel: HomeViewModel by viewModels()

    private val newProductAdapter = ProductAdapterBasic()
    private val saleProductAdapter = ProductAdapterBasic()
    private val hotCategoryAdapter = CategoryBasicAdapter()
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
        loadNewProduct()

        //Sale product
        setUpRecycleViewSaleProduct()
        subscribeLoadSaleProduct()
        loadSaleProduct()

        //Hot Category
        setUpRecyclerviewHotCategory()
        loadHotCategory()

        //when refresh layout -> load all data relate
        setUpSwipeRefreshLayout()
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
        binding.rcvNewProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvNewProduct.setHasFixedSize(true)
        binding.rcvNewProduct.isNestedScrollingEnabled = false
        binding.rcvNewProduct.adapter = newProductAdapter
    }

    private fun subscribeLoadNewProduct() {
        viewModel.dataStateNewProducts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                Status.ERROR -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    showToast(it.message!!)
                }
                Status.SUCCESS -> {
                    newProductAdapter.setProducts(it.response!!.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    private fun loadNewProduct() {
        viewModel.getSaleProducts()
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
                }
                Status.SUCCESS -> {
                    saleProductAdapter.setProducts(it.response!!.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    private fun loadSaleProduct() {
        viewModel.getProducts()
    }
    //endregion

    //region Hot Categories
    fun loadHotCategory(){
        val categories:MutableList<Category> = mutableListOf()
        categories.add(Category(R.drawable.vay, "Váy nữ"))
        categories.add(Category(R.drawable.chan_vay, "Chân váy"))
        categories.add(Category(R.drawable.ao_thun_tay_ngan_nu, "Áo thun nữ"))
        categories.add(Category(R.drawable.do_the_thao_nu, "Đồ thể thao nữ"))
        categories.add(Category(R.drawable.quan_thun_dai_nu, "Quần dài nữ"))

        categories.add(Category(R.drawable.ao_so_mi_tay_dai_nam, "Áo sơ mi nam"))
        categories.add(Category(R.drawable.ao_thun_tay_ngan_nam, "Áo thun nam"))
        categories.add(Category(R.drawable.do_the_thao_nam, "Đồ thể thao nam"))
        categories.add(Category(R.drawable.quan_tay_dai_nam, "Quần tây nam"))
        categories.add(Category(R.drawable.non, "Nón"))
        hotCategoryAdapter.setCategories(categories)

    }
    fun setUpRecyclerviewHotCategory() {
        binding.rcvHotCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvHotCategory.setHasFixedSize(true)
        binding.rcvHotCategory.isNestedScrollingEnabled = false
        binding.rcvHotCategory.adapter = hotCategoryAdapter
    }
    //endregion

    private fun setUpSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
//            loadNewProduct()
        }
    }

}
