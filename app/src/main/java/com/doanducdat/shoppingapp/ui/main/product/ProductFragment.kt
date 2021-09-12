package com.doanducdat.shoppingapp.ui.main.product

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.os.bundleOf
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
    private lateinit var productSelected: Product
    private val productColorAdapter = ProductColorAdapter()
    private val productSizeAdapter = ProductSizeAdapter()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductBinding = FragmentProductBinding.inflate(inflater, container, false)


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBackFragment()

        //get product selected
        getDataFromAnotherFragment()
        setUpSlideImageProduct()
        setUpInfoProduct()
        //this fun require > android 6
        listenNestedScroll()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun listenNestedScroll() {
        binding.nestedScrollViewProduct.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY in 0..500) {
                val alpha = scrollY / 500F
                val resultColor = ColorUtils.blendARGB(
                    ContextCompat.getColor(requireContext(), R.color.colorTransparent0),
                    ContextCompat.getColor(requireContext(), R.color.bgColorBtnGeneric), alpha
                )
                binding.layoutToolBarProduct.setBackgroundColor(resultColor)
                binding.imgBack.background = null
                binding.imgMyCart.background = null
                binding.imgReview.background = null
                binding.imgAddToCart.background = null
            }
            if (scrollY < 50 && oldScrollY > scrollY) {
                showToast("top")
                binding.imgBack.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_back)
                binding.imgMyCart.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_back)
                binding.imgReview.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_back)
                binding.imgAddToCart.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_back)
            }
        }
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
        adapter.mySetOnclickImage {
            //click any one img in viewpager -> navigate
            controller.navigate(
                R.id.productPhotoViewFragment,
                bundleOf("URL_IMAGES" to productSelected.getUrlImages())
            )
        }
        binding.viewPagerImgsProduct.adapter = adapter
        binding.viewPagerImgsProduct.offscreenPageLimit = 5
    }

    private fun setUpIndicator() {
        binding.indicatorSlideImgsProduct.setViewPager(binding.viewPagerImgsProduct)
    }

    //endregion

    @SuppressLint("SetTextI18n")
    private fun setUpInfoProduct() {
        with(binding) {
            txtName.text = productSelected.name
            txtPrice.text = productSelected.getPrice()
            txtRating.text = "${productSelected.ratingsAverage}"
            txtRatingQuantity.text = productSelected.getRatingsQuantity()
            txtBrand.text = productSelected.brand
            txtMaterial.text = productSelected.material
            txtPattern.text = productSelected.pattern
            txtDes.text = productSelected.description
        }

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


