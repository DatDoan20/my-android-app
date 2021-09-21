package com.doanducdat.shoppingapp.ui.main.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CartAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCartBinding
import com.doanducdat.shoppingapp.module.response.Status
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.dialog.MyBasicDialog
import com.doanducdat.shoppingapp.utils.dialog.MyYesNoDialog
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(), MyActionApp {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false)


    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    private val dialogYesNo by lazy { MyYesNoDialog(requireContext()) }
    private val dialogBasic by lazy { MyBasicDialog(requireContext()) }
    private val cartAdapter: CartAdapter = CartAdapter()
    private val viewModel: CartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenLoadingForm()
        listenSumMoneyCart()
        setUpRcvCart()
        setUpActionClick()

    }

    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            cartAdapter.isClickAble = !it
            if (it) {
                setStateProgressBar(View.VISIBLE, binding.spinKitProgressBar)
            } else {
                setStateProgressBar(View.GONE, binding.spinKitProgressBar)
            }
        })
    }

    /**
     * when fun calculatingSumCart called , plush,
     *
     * minus -> update sumMoneyCart in viewModel
     */
    private fun listenSumMoneyCart() {
        viewModel.sumMoneyCart.observe(viewLifecycleOwner, {
            binding.btnExtendedFab.text = FormValidation.formatMoney(it)
            Log.e(AppConstants.TAG.CART, "listenSumMoneyCart: $it")
        })
    }

    /**
     * when init load, after delete product, use this fun
     */
    private fun calculatingSumCart() {
        var sumMoneyCart: Int = 0
        InfoUser.currentUser?.cart?.forEach {
            sumMoneyCart += it.price
        }
        viewModel.sumMoneyCart.value = sumMoneyCart
    }

    private fun setUpRcvCart() {
        InfoUser.currentUser?.cart?.let {
            cartAdapter.setCartList(it)
            calculatingSumCart()
        }
        binding.rcvCart.adapter = cartAdapter
        binding.rcvCart.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //event click adapter
        cartAdapter.mySetOnClickMinusOrPlush { CODE_ACTION_CLICK, populatedCart, txtQuantity, txtPrice ->
            if (cartAdapter.isClickAble) {
                val priceOneItem = populatedCart.price / populatedCart.quantity

                if (CODE_ACTION_CLICK == AppConstants.ActionClick.PLUSH_PRODUCT_IN_CART) {
                    populatedCart.quantity += 1
                    populatedCart.price += priceOneItem
                    viewModel.sumMoneyCart.value = viewModel.sumMoneyCart.value?.plus(priceOneItem)
                } else {
                    if (populatedCart.quantity == 1) {
                        showDialogConfirmDeleteProductInCart(populatedCart.infoProduct.id)
                    } else {
                        populatedCart.quantity -= 1
                        populatedCart.price -= priceOneItem
                        viewModel.sumMoneyCart.value = viewModel.sumMoneyCart.value?.minus(
                            priceOneItem
                        )
                    }
                }
                txtPrice.text = populatedCart.getFormatPrice()
                txtQuantity.text = populatedCart.quantity.toString()
            }
        }
        cartAdapter.mySetOnClickDelete {
            if (cartAdapter.isClickAble) {
                showDialogConfirmDeleteProductInCart(it)
            }
        }
    }

    private fun showDialogConfirmDeleteProductInCart(idProduct: String) {
        dialogYesNo.setText(AppConstants.MsgInfo.CONFIRM_DELETE_PRODUCT_IN_CART)
        dialogYesNo.mySetOnClickYes {
            deleteProductInCart(idProduct)
        }
        dialogYesNo.show()
    }

    private fun deleteProductInCart(idProduct: String) {
        listenDeleteProductInCart()
        viewModel.deleteProductInCart(idProduct)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun listenDeleteProductInCart() {
        viewModel.dataStateDeleteProductInCart.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                    dialogYesNo.dismiss()
                }
                Status.ERROR -> {
                    Log.e(AppConstants.TAG.CART, "listenDeleteProductInCart: ${it.message}")
                    showLongToast(it.message.toString())
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    Log.e(
                        AppConstants.TAG.CART,
                        "listenDeleteProductInCart: ${it.response!!.message}"
                    )
                    showShortToast(AppConstants.MsgInfo.MSG_INFO_DELETE_PRODUCT_IN_CART)

                    //update currentUser variable in Ram
                    InfoUser.currentUser = it.response.data

                    //update adapter rcv
                    cartAdapter.setCartList(InfoUser.currentUser!!.cart)
                    cartAdapter.notifyDataSetChanged()
                    calculatingSumCart()
                    viewModel.isLoading.value = false
                }
            }
        })
    }

    private fun setUpActionClick() {
        binding.btnExtendedFab.setOnClickListener {
            doActionClick(AppConstants.ActionClick.ORDER)
        }
    }


    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.ORDER -> {
                checkPreOrder()
            }
        }
    }

    private fun checkPreOrder() {
        //check verify email?
        if (InfoUser.currentUser?.stateVerifyEmail == false) {
            Log.e(AppConstants.TAG.CART, "checkPreOrder: ${InfoUser.currentUser?.stateVerifyEmail}")
            dialogBasic.setTextButton("Đã hiểu")
            dialogBasic.setText(AppConstants.MsgInfo.MSG_NOT_VERIFY_EMAIl)
            dialogBasic.show()
            return
        }
        if (InfoUser.currentUser?.cart?.size == 0) {
            Log.e(AppConstants.TAG.CART, "checkPreOrder: ${InfoUser.currentUser?.cart!!.size}")
            dialogBasic.setTextButton("Đã hiểu")
            dialogBasic.setText(AppConstants.MsgInfo.MSG_INFO_EMPTY_CART)
            dialogBasic.show()
            return
        }
        controller.navigate(R.id.orderFragment)
    }
}