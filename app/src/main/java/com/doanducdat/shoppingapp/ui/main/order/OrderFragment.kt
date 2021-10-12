package com.doanducdat.shoppingapp.ui.main.order

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentOrderBinding
import com.doanducdat.shoppingapp.model.order.PurchasedProduct
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.dialog.MyBasicDialog
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding>(), MyActionApp {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderBinding = FragmentOrderBinding.inflate(inflater, container, false)

    val viewModel: OrderViewModel by viewModels()
    val myBasicDialog by lazy { MyBasicDialog(requireContext()) }
    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.user = InfoLocalUser.currentUser
        listenLoadingForm()
        setUpBackFragment()
        setUpClickRadio()
        setUpInfo()
        setUpActionClick()
    }

    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            setStateEnableViews(!it, binding.btnOrder)
            if (it) {
                setStateVisibleView(View.VISIBLE, binding.spinKitProgressBar)
            } else {
                setStateVisibleView(View.GONE, binding.spinKitProgressBar)
            }
        })
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controller.popBackStack()
        }
    }

    private fun setUpClickRadio() {
        binding.layoutCod.setOnClickListener {
            binding.rdoCod.isChecked = true
            binding.rdoMomo.isChecked = false
        }
        binding.layoutMomo.setOnClickListener {
            binding.rdoCod.isChecked = false
            binding.rdoMomo.isChecked = true
        }
        binding.rdoMomo.setOnClickListener {
            binding.rdoCod.isChecked = false
        }
        binding.rdoCod.setOnClickListener {
            binding.rdoMomo.isChecked = false
        }
    }

    private fun setUpInfo() {
        with(viewModel) {
            costDelivery = 15000
            InfoLocalUser.currentUser?.cart?.forEach {
                totalPrice += it.price
            }
            totalPayment = costDelivery + totalPrice
            binding.txtCostDelivery.text = FormValidation.formatMoney(costDelivery)
            binding.txtTotalPrice.text = FormValidation.formatMoney(totalPrice)
            binding.txtTotalPayment.text = FormValidation.formatMoney(totalPayment)
        }
    }

    private fun setUpActionClick() {
        binding.btnOrder.setOnClickListener {
            doActionClick(AppConstants.ActionClick.ORDER)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.ORDER -> {
                preOrderCheckInput()
            }
        }
    }

    private fun preOrderCheckInput() {
        val nameUser = binding.edtName.text.toString().trim()
        val addressUser = binding.edtAddress.text.toString().trim()
        val msgErr = FormValidation.checkValidationName(nameUser)

        if (msgErr != null) {
            binding.edtName.requestFocus()
            showLongToast(msgErr)
            return
        }
        if (TextUtils.isEmpty(addressUser)) {
            binding.edtAddress.requestFocus()
            showLongToast(AppConstants.MsgErr.ADDRESS_ERR_MSG)
            return
        }
        preOrderSetInput(nameUser, addressUser)
    }

    private fun preOrderSetInput(nameUser: String, addressUser: String) {
        if (InfoLocalUser.currentUser == null) return

        viewModel.isLoading.value = true

        val purchasedProductsInCart: MutableList<PurchasedProduct> = mutableListOf()
        InfoLocalUser.currentUser!!.cart.forEach {
            purchasedProductsInCart.add(
                PurchasedProduct(
                    false,
                    it.color,
                    it.infoProduct.getUnFormatDiscount(),
                    it.price,
                    it.infoProduct.getImageCover(),
                    it.infoProduct.name,
                    it.getOneItemPrice(),
                    it.infoProduct.id,
                    it.quantity,
                    it.size,
                    null
                )
            )
        }
        with(viewModel) {
            name = nameUser
            address = addressUser
            email = InfoLocalUser.currentUser!!.email
            phone = InfoLocalUser.currentUser!!.phone
            purchasedProducts = purchasedProductsInCart
            note = binding.edtNote.text.toString().trim()
        }
        listenOrder()
        viewModel.order()
    }

    private fun listenOrder() {
        viewModel.dataStateOrder.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    viewModel.isLoading.value = false
                    Log.d(AppConstants.TAG.ORDER, "listenOrder: ${it.message}")
                    showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                }
                Status.SUCCESS -> {
                    viewModel.isLoading.value = false
                    showDialog(it.response!!.data.addressDelivery, it.response.data.totalPayment)
                    InfoLocalUser.currentUser?.cart?.clear()
                }
            }
        })
    }

    private fun showDialog(addressDelivery: String, totalPayment: Int) {
        val price = FormValidation.formatMoney(totalPayment)
        myBasicDialog.setText(
            "${AppConstants.MsgInfo.MSG_ORDER_SUCCESS} đơn hàng ${price}đ đến địa chỉ: $addressDelivery"
        )
        myBasicDialog.setTextButton(AppConstants.MsgInfo.CLOSE)
        myBasicDialog.setOnClick {
            controller.popBackStack()
        }
        myBasicDialog.show()
    }
}