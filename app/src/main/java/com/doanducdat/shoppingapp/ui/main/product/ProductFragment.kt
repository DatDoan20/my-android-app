package com.doanducdat.shoppingapp.ui.main.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.ProductColorAdapter
import com.doanducdat.shoppingapp.adapter.ProductSizeAdapter
import com.doanducdat.shoppingapp.adapter.SlideImageProductAdapter
import com.doanducdat.shoppingapp.databinding.FragmentProductBinding
import com.doanducdat.shoppingapp.module.product.Product
import com.doanducdat.shoppingapp.module.product.ProductColor
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.MyBgCustom


class ProductFragment : BaseFragment<FragmentProductBinding>() {

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    lateinit var productSelected: Product
    private val productColorAdapter = ProductColorAdapter()
    private val productSizeAdapter = ProductSizeAdapter()

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
        setUpInfoProduct()
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

    //region slide Image Product
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

    @SuppressLint("SetTextI18n")
    private fun setUpInfoProduct() {
        binding.txtName.text = productSelected.name
        binding.txtPrice.text = productSelected.getPrice()
        binding.txtRating.text = "${productSelected.ratingsAverage}"
        binding.txtRatingQuantity.text = productSelected.getRatingsQuantity()

        //rcv color + listen click
        binding.rcvColors.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvColors.adapter = productColorAdapter
        loadColorProduct()
        listenClickColor()

        //rcv size + listen click
        binding.rcvSize.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvSize.adapter = productSizeAdapter
        loadSizeProduct()
        listenClickSize()
    }

    private fun loadColorProduct() {
        //red - #242342, ...
        val productColors: MutableList<ProductColor> = mutableListOf()
        productSelected.color.split(",").forEach {
            val arr: List<String> = it.split("-")
            val colorProduct = ProductColor(arr[1], arr[0])
            productColors.add(colorProduct)
        }
        productColorAdapter.setProductColors(productColors)
    }

    private fun listenClickColor() {
        productColorAdapter.mySetOnClickColorProduct { clickedColor, txtColorHex ->
            //make clicked color has stroke
            binding.txtColorName.text = clickedColor.name

            val bgItemColor = MyBgCustom.getInstance()
                .bgOvalStroke(requireContext(), clickedColor.getHexColor(), 13)

            txtColorHex.background = bgItemColor
        }
    }

    private fun loadSizeProduct() {
        //: ... , -> split(",")
        val productSizes: MutableList<String> = mutableListOf()
        productSelected.size.split(",").forEach {
            productSizes.add(it)
        }
        productSizeAdapter.setProductSizes(productSizes)
    }

    private fun listenClickSize() {
        productSizeAdapter.mySetOnClickSizeProduct { clickedSize, txtSize ->
            binding.txtSize.text = clickedSize
            val bgItemSize =
                MyBgCustom.getInstance().bgRadiusSize(requireContext(), R.color.sizeProductPicked)
            txtSize.background = bgItemSize
        }
    }

}


