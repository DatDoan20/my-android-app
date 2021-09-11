package com.doanducdat.shoppingapp.ui.main.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.SlideImageProductAdapter
import com.doanducdat.shoppingapp.databinding.FragmentProductBinding
import com.doanducdat.shoppingapp.module.product.Product
import com.doanducdat.shoppingapp.ui.base.BaseFragment


class ProductFragment : BaseFragment<FragmentProductBinding>() {

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    lateinit var productSelected: Product

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductBinding = FragmentProductBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBackFragment()

        //get product selected
        getDataFromAnotherFragment()
        setUpSlideImageProduct()
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controller.popBackStack()
        }
    }

    private fun getDataFromAnotherFragment() {
        val bundle = arguments
        if (bundle != null) {
            productSelected = bundle.getSerializable("PRODUCT") as Product
        }
    }

    //region Slide Image Intro
    private fun setUpSlideImageProduct() {
        setUpViewPager()
        setUpIndicator()
    }

    private fun setUpViewPager() {
        val adapter: SlideImageProductAdapter = SlideImageProductAdapter()
        adapter.setUrlImagesProduct(productSelected.getUrlImages())
        binding.viewPagerImgsProduct.adapter = adapter
        binding.viewPagerImgsProduct.offscreenPageLimit = 5
    }

    private fun setUpIndicator() {
        binding.indicatorSlideImgsProduct.setViewPager(binding.viewPagerImgsProduct)
    }
    //endregion
}


