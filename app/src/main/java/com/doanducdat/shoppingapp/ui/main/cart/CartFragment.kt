package com.doanducdat.shoppingapp.ui.main.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CartAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCartBinding
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
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

        //check: it will work if order sucess -> cart local was cleared -> sumMoneyCart = 0
    }

    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            cartAdapter.isClickAble = !it
            if (it) {
                setStateVisibleView(View.VISIBLE, binding.spinKitProgressBar)
            } else {
                setStateVisibleView(View.GONE, binding.spinKitProgressBar)
            }
        })
    }

    /**
     * when fun calculatingSumCart called , plush,
     *
     * minus -> update sumMoneyCart in viewModel
     */
    @SuppressLint("SetTextI18n")
    private fun listenSumMoneyCart() {
        viewModel.sumMoneyCart.observe(viewLifecycleOwner, {
            binding.btnExtendedFab.text = "${FormValidation.formatMoney(it)}/Tiến hành đặt hàng"
            Log.e(AppConstants.TAG.CART, "listenSumMoneyCart: $it")
        })
    }

    private fun setUpRcvCart() {
        loadDataAdapter()

        binding.rcvCart.adapter = cartAdapter
        binding.rcvCart.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //event click adapter
        setEventClickAdapter()

    }

    private fun loadDataAdapter() {
        calculatingSumCart()
        if (InfoLocalUser.currentUser?.cart == null || InfoLocalUser.currentUser!!.cart.size == 0) {
            setStateVisibleView(View.VISIBLE, binding.imgEmptyCart)
            setStateVisibleView(View.VISIBLE, binding.txtEmptyCart)
            return
        }

        cartAdapter.setCartList(InfoLocalUser.currentUser!!.cart)
    }

    /**
     * when init load or after delete product -> use this fun
     */
    private fun calculatingSumCart() {
        Log.e(AppConstants.TAG.CART, "calculatingSumCart: do it")
        var sumMoneyCart: Int = 0
        InfoLocalUser.currentUser?.cart?.forEach {
            sumMoneyCart += it.price
        }
        viewModel.sumMoneyCart.value = sumMoneyCart
    }

    private fun setEventClickAdapter() {
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
                    showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    Log.e(
                        AppConstants.TAG.CART,
                        "listenDeleteProductInCart: ${it.response!!.message}"
                    )
                    showShortToast(AppConstants.MsgInfo.MSG_INFO_DELETE_PRODUCT_IN_CART)

                    //update currentUser variable in Ram
                    InfoLocalUser.currentUser = it.response.data

                    //update adapter rcv
                    cartAdapter.setCartList(InfoLocalUser.currentUser!!.cart)
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
        if (InfoLocalUser.currentUser?.stateVerifyEmail == false) {
            Log.e(AppConstants.TAG.CART, "checkPreOrder: ${InfoLocalUser.currentUser?.stateVerifyEmail}")
            dialogBasic.setTextButton("Đã hiểu")
            dialogBasic.setText(AppConstants.MsgInfo.MSG_NOT_VERIFY_EMAIl)
            dialogBasic.show()
            return
        }
        if (InfoLocalUser.currentUser?.cart?.size == 0) {
            Log.e(AppConstants.TAG.CART, "checkPreOrder: ${InfoLocalUser.currentUser?.cart!!.size}")
            dialogBasic.setTextButton("Đã hiểu")
            dialogBasic.setText(AppConstants.MsgInfo.MSG_INFO_EMPTY_CART)
            dialogBasic.show()
            return
        }
        controllerMain.navigate(R.id.orderFragment)
    }
}