package com.doanducdat.shoppingapp.ui.main.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
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

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    val viewModel: OrderViewModel by viewModels()
    val myBasicDialog by lazy { MyBasicDialog(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.user = InfoLocalUser.currentUser
        listenLoadingForm()
        setUpBackFragment()
        setUpClickRadio()
        setUpInfo()
        setUpActionClick()
        initResultLauncher()
        listenOrder()
    }

    private fun initResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.order(AppConstants.QueryRequest.PAYMENT_MODE_VN_PAY_PAID)
                }
            }
    }

    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            setStateEnableViews(!it, binding.btnOrder)
            if (it) {
                setStateVisibleView(View.VISIBLE, binding.spinKitProgressBar)
            } else {
                setStateVisibleView(View.GONE, binding.spinKitProgressBar)
            }
        }
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controllerMain.popBackStack()
        }
    }

    private fun setUpClickRadio() {
        binding.layoutCod.setOnClickListener {
            binding.rdoCod.isChecked = true
            binding.rdoMomo.isChecked = false
            binding.rdoVnPay.isChecked = false
        }
        binding.layoutMomo.setOnClickListener {
            binding.rdoVnPay.isChecked = false
            binding.rdoCod.isChecked = false
            binding.rdoMomo.isChecked = true
        }
        binding.layoutVnPay.setOnClickListener {
            binding.rdoVnPay.isChecked = true
            binding.rdoCod.isChecked = false
            binding.rdoMomo.isChecked = false
        }
        binding.rdoMomo.setOnClickListener {
            binding.rdoCod.isChecked = false
            binding.rdoVnPay.isChecked = false
        }
        binding.rdoCod.setOnClickListener {
            binding.rdoVnPay.isChecked = false
            binding.rdoMomo.isChecked = false
        }
        binding.rdoVnPay.setOnClickListener {
            binding.rdoCod.isChecked = false
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
            var action = AppConstants.ActionClick.ORDER
            if (binding.rdoVnPay.isChecked) {
                action = AppConstants.ActionClick.ORDER_VN_PAY
            }
            doActionClick(action)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        preOrderCheckInput(CODE_ACTION_CLICK)
    }

    private fun preOrderCheckInput(CODE_ACTION_CLICK: Int) {
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
        preOrderSetInput(nameUser, addressUser, CODE_ACTION_CLICK)
    }

    private fun preOrderSetInput(nameUser: String, addressUser: String, CODE_ACTION_CLICK: Int) {
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
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.ORDER -> {
                viewModel.order()
            }
            AppConstants.ActionClick.ORDER_VN_PAY -> {
                val intent = Intent(requireActivity(), VnPayActivity::class.java)
                intent.putExtras(bundleOf("ORDER_TOTAL_PAYMENT" to viewModel.totalPayment))
                viewModel.isLoading.value = false
                activityResultLauncher?.launch(intent)
            }
        }
    }


    private fun listenOrder() {
        viewModel.dataStateOrder.observe(viewLifecycleOwner) {
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
        }
    }

    private fun showDialog(addressDelivery: String, totalPayment: Int) {
        val price = FormValidation.formatMoney(totalPayment)
        myBasicDialog.setText(
            "${AppConstants.MsgInfo.MSG_ORDER_SUCCESS} đơn hàng ${price}đ đến địa chỉ: $addressDelivery"
        )
        myBasicDialog.setTextButton(AppConstants.MsgInfo.CLOSE)
        myBasicDialog.setOnClick {
            controllerMain.popBackStack()
        }
        myBasicDialog.show()
    }
}