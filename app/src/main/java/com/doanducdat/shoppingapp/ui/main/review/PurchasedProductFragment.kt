package com.doanducdat.shoppingapp.ui.main.review

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.PurchasedProductAdapter
import com.doanducdat.shoppingapp.databinding.FragmentPurchasedProductBinding
import com.doanducdat.shoppingapp.model.order.Order
import com.doanducdat.shoppingapp.model.order.PurchasedProduct
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.ui.main.order.OrderViewModel
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.dialog.MyRatingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchasedProductFragment : BaseFragment<FragmentPurchasedProductBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPurchasedProductBinding =
        FragmentPurchasedProductBinding.inflate(inflater, container, false)

    private val purchasedProductAdapter by lazy { PurchasedProductAdapter() }
    private val myRatingDialog by lazy { MyRatingDialog(requireContext()) }

    private val reviewViewModel: ReviewViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    private var indexProductNeedRemove: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBackFragment()


        setUpRcvPurchasedProduct()

        // (1) load my receiver Orders -> (2) get product has stateRating:false in each order
        listenLoadMyReceiverOrder()
        loadMyReceiverOrder()

        listenCreateReview()
    }


    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controllerMain.popBackStack()
        }
    }


    private fun setUpRcvPurchasedProduct() {
        binding.rcvPurchasedProduct.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rcvPurchasedProduct.adapter = purchasedProductAdapter
    }

    private fun listenLoadMyReceiverOrder() {
        orderViewModel.dataStateReceivedOrder.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.spinKitProgressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.spinKitProgressBar.visibility = View.GONE
                    showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    Log.e(AppConstants.TAG.REVIEW, "listenCreateReview: ${it.message}")
                    setStateInfoToView(
                        R.drawable.error,
                        AppConstants.MsgErr.GENERIC_ERR_MSG
                    )
                }
                Status.SUCCESS -> {
                    binding.spinKitProgressBar.visibility = View.GONE
                    //(2) get product has stateRating:false in each order
                    getPurchasedProduct(it.response!!.data)
                }
            }
        })
    }

    private fun loadMyReceiverOrder() {
        orderViewModel.getMyReceivedOrder()
    }

    private fun getPurchasedProduct(orders: List<Order>) {
        val purchasedProductList: MutableList<PurchasedProduct> = mutableListOf()
        orders.forEach { order ->
            order.purchasedProducts.forEach { purchasedProduct ->
                if (!purchasedProduct.stateRating) {
                    purchasedProduct.orderId = order.id!!
                    purchasedProductList.add(purchasedProduct)
                }
            }
        }
        purchasedProductAdapter.setPurchasedProductList(purchasedProductList)
        if (purchasedProductAdapter.itemCount == 0) {
            setStateInfoToView(
                R.drawable.empty_product,
                AppConstants.MsgErr.EMPTY_PURCHASED_PRODUCT
            )
        }

        purchasedProductAdapter.mySetOnClick { purchasedProduct, position ->
            indexProductNeedRemove = position
            showDialogRating(purchasedProduct)
        }
    }

    private fun setStateInfoToView(idIcon: Int, msg: String) {
        binding.imgEmptyPurchasedProduct.setImageResource(idIcon)
        binding.txtEmptyPurchasedProduct.text = msg
        setStateVisibleView(View.VISIBLE, binding.imgEmptyPurchasedProduct)
        setStateVisibleView(View.VISIBLE, binding.txtEmptyPurchasedProduct)
    }

    private fun showDialogRating(purchasedProduct: PurchasedProduct) {
//        val flag = AppConstants.ActionClick.REVIEW_PRODUCT
        myRatingDialog.mySetOnClickYes {
            val ratingValue: Int = myRatingDialog.getRatingValue().toInt()
            val contentReview = myRatingDialog.getReview().trim()
            when {
                ratingValue == 0 -> {
                    showLongToast(AppConstants.MsgInfo.RATING_EMPTY)
                }
                TextUtils.isEmpty(contentReview) -> {
                    showLongToast(AppConstants.MsgInfo.CONTENT_REVIEW_EMPTY)
                }
                else -> {
                    myRatingDialog.setStateDialog(false)
                    myRatingDialog.setStateProgressBar(View.VISIBLE)
                    reviewViewModel.createReview(
                        purchasedProduct.productId,
                        ratingValue,
                        contentReview,
                        purchasedProduct.orderId!!
                    )
                }
            }

        }
        myRatingDialog.show()
    }

    private fun listenCreateReview() {
        reviewViewModel.dataStateCreateReview.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    myRatingDialog.setStateDialog(false)
                    myRatingDialog.setStateProgressBar(View.VISIBLE)
                }
                Status.ERROR -> {
                    myRatingDialog.setStateDialog(true)
                    myRatingDialog.setStateProgressBar(View.GONE)
                    showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    Log.e(AppConstants.TAG.REVIEW, "listenCreateReview: ${it.message}")
                }
                Status.SUCCESS -> {
                    if (indexProductNeedRemove != null) {
                        purchasedProductAdapter.deleteProductItem(indexProductNeedRemove!!)
                    }
                    myRatingDialog.setStateProgressBar(View.GONE)
                    if (purchasedProductAdapter.itemCount == 0) {
                        setStateInfoToView(
                            R.drawable.empty_product,
                            AppConstants.MsgErr.EMPTY_PURCHASED_PRODUCT
                        )
                    }
                    myRatingDialog.dismiss()
                    myRatingDialog.setStateDialog(true)

                }
            }
        })
    }

}