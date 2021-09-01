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
import com.doanducdat.shoppingapp.adapter.ProductAdapterBasic
import com.doanducdat.shoppingapp.adapter.SlideImageIntroAdapter
import com.doanducdat.shoppingapp.databinding.FragmentHomeBinding
import com.doanducdat.shoppingapp.module.SlideImage
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.response.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }

    val viewModel: HomeViewModel by viewModels()

    private val newProductAdapter = ProductAdapterBasic()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSearchPlate(binding.myAppBarLayout.searchView)

        subscribeListenLoadingForm()
        subsCribCollapsingListen()
        setUpSlideImageIntro()

        setUpRecycleViewNewProduct()
        subscribeLoadNewProduct()
        loadNewProduct()

        setUpSwipeRefreshLayout()
    }


    private fun subscribeListenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            with(binding) {
//                setStateViews(!it, btnSignIn, txtInputEdtPhone, txtInputEdtPassword)
                if (it) {
//                    setStateProgressBar(View.VISIBLE, spinKitProgressBar)
                } else {
//                    setStateProgressBar(View.GONE, spinKitProgressBar)
                }
            }
        })
    }

    private fun subsCribCollapsingListen() {
        binding.myAppBarLayout.appBarLayout.addOnOffsetChangedListener(

            collapsingListen(binding.myAppBarLayout.searchView, binding.swipeRefreshLayout)
        )
    }

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

    private fun setUpRecycleViewNewProduct() {
        binding.rcvNewProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvNewProduct.setHasFixedSize(true)
        binding.rcvNewProduct.isNestedScrollingEnabled = false
        binding.rcvNewProduct.adapter = newProductAdapter
    }

    private fun subscribeLoadNewProduct() {
        viewModel.dataState.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
//                    binding.swipeRefreshLayout.isRefreshing = true
                }
                Status.ERROR -> {
//                    binding.swipeRefreshLayout.isRefreshing = false
                    showToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                }
                Status.SUCCESS -> {
                    newProductAdapter.setProducts(it.response!!.data)
//                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    private fun loadNewProduct() {
        viewModel.getProducts()
    }

    private fun setUpSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadNewProduct()
        }
    }
}
