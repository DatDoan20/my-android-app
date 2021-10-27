package com.doanducdat.shoppingapp.ui.main.product

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.ProductColorAdapter
import com.doanducdat.shoppingapp.adapter.ProductSizeAdapter
import com.doanducdat.shoppingapp.adapter.SlideImageProductAdapter
import com.doanducdat.shoppingapp.databinding.FragmentProductBinding
import com.doanducdat.shoppingapp.model.cart.Cart
import com.doanducdat.shoppingapp.model.product.Product
import com.doanducdat.shoppingapp.model.product.ProductColor
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.dialog.MyBasicDialog
import com.doanducdat.shoppingapp.utils.handler.HandlerBgCustom
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : BaseFragment<FragmentProductBinding>(), MyActionApp {

    private lateinit var productSelected: Product

    private val productColorAdapter = ProductColorAdapter()
    private val productSizeAdapter = ProductSizeAdapter()

    private val myBasicDialog by lazy { MyBasicDialog(requireContext()) }
    private val viewModel: ProductViewModel by viewModels()

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

        //listen click
        listenLoadingForm()

        //click
        setUpActionClick()
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
                binding.layoutMyCart.imgMyCart.background = null
                binding.imgReview.background = null
                binding.imgAddToCart.background = null
            }
            //scroll up and < 50
            if (scrollY < 50 && oldScrollY > scrollY) {
                binding.imgBack.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_back)
                binding.layoutMyCart.imgMyCart.background =
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
            controllerMain.popBackStack()
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
            controllerMain.navigate(
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

    //region set info product
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
            if (productSelected.getUnFormatDiscount() != 0) {
                txtDiscountProduct.text = productSelected.getDiscount()
                txtPriceNotDiscount.text = productSelected.getPriceUnDiscount()
                txtPriceNotDiscount.paintFlags =
                    txtPriceNotDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                txtDiscountProduct.visibility = View.VISIBLE
                txtPriceNotDiscount.visibility = View.VISIBLE
            }
            if (InfoLocalUser.currentUser?.cart != null) {
                binding.layoutMyCart.imgRedDot.text = InfoLocalUser.currentUser!!.cart.size.toString()
            }
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
    //endregion

    // region color product
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

            val bgItemColor = HandlerBgCustom.getInstance()
                .bgOvalStroke(requireContext(), clickedColor.getHexColor(), 13)

            txtColorHex.background = bgItemColor
        }
    }
    //endregion

    //region size product
    private fun loadSizeProduct() {
        //: ... , -> split(",")
        val productSizes: MutableList<String> = mutableListOf()
        productSelected.size.split(",").forEach {
            productSizes.add(it)
        }
        productSizeAdapter.setProductSizes(productSizes)
    }

    private fun listenClickSize() {
        productSizeAdapter.mySetOnClickSizeProduct { sizeItem, layoutTxtSize ->
            layoutTxtSize.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.sizeProductPicked)
            )
            binding.txtSize.text = sizeItem
        }
    }
    //endregion


    //region listen
    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                with(binding) {
                    setStateVisibleView(View.VISIBLE, spinKitProgressBar)
                    setStateEnableViews(
                        false,
                        imgBack,
                        layoutMyCart.imgMyCart,
                        imgReview,
                        imgAddToCart,
                        viewPagerImgsProduct
                    )
                }
            } else {
                with(binding) {
                    setStateVisibleView(View.GONE, spinKitProgressBar)
                    setStateEnableViews(
                        true,
                        imgBack,
                        layoutMyCart.imgMyCart,
                        imgReview,
                        imgAddToCart,
                        viewPagerImgsProduct
                    )
                }
            }
        })
    }

    private fun listenAddToCart() {
        viewModel.dataStateHandleProductInCart.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    myBasicDialog.setText(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    myBasicDialog.show()
                    Log.e(AppConstants.TAG.ADD_TO_CART, "listenAddToCart: ${it.message}" )
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    myBasicDialog.setText("${productSelected.name} Đã được thêm vào giỏ hàng")
                    myBasicDialog.setTextButton("OK")
                    myBasicDialog.show()
                    InfoLocalUser.currentUser = it.response!!.data
                    binding.layoutMyCart.imgRedDot.text =
                        InfoLocalUser.currentUser!!.cart.size.toString()
                    viewModel.isLoading.value = false
                }
            }
        })
    }
    //endregion

    private fun setUpActionClick() {
        binding.imgAddToCart.setOnClickListener {
//            val flag = AppConstants.ActionClick.NAV_ADD_TO_CARD
            doActionClick(AppConstants.ActionClick.ADD_TO_CART)
        }
        binding.imgReview.setOnClickListener {
//            val flag = AppConstants.ActionClick.NAV_REVIEW_PRODUCT
            controllerMain.navigate(R.id.reviewFragment, bundleOf("PRODUCT_ID" to productSelected.id))
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.ADD_TO_CART -> {
                checkPreAddToCart()
            }
        }
    }

    private fun checkPreAddToCart() {
        var msgErr: String = ""
        var isErr: Boolean = false
        if (TextUtils.isEmpty(binding.txtColorName.text.toString().trim())) {
            msgErr = AppConstants.MsgInfo.COLOR_ERR_MSG + "\n"
            isErr = true
        }
        if (TextUtils.isEmpty(binding.txtSize.text.toString().trim())) {
            msgErr += AppConstants.MsgInfo.SIZE_ERR_MSG
            isErr = true
        }
        if (isErr) {
            myBasicDialog.setText(msgErr)
            myBasicDialog.show()
            return
        }
        //check if product is exist in cart
        InfoLocalUser.currentUser?.cart?.forEach {
            if (it.infoProduct.id == productSelected.id) {
                myBasicDialog.setText(AppConstants.MsgInfo.PRODUCT_IS_EXIST_IN_CART)
                myBasicDialog.show()
                return
            }
        }
        addToCart()
    }

    private fun addToCart() {
        listenAddToCart()
        val cart: Cart =
            Cart(
                binding.txtColorName.text.toString(),
                productSelected.getUnFormatPrice(),
                productSelected.id,
                1,
                binding.txtSize.text.toString()
            )
        viewModel.addToCart(cart)
    }
}


